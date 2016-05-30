package de.hpi.idd.dysni;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.Dataset;
import de.hpi.idd.DatasetManager;
import de.hpi.idd.Evaluator;
import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.dysni.sim.IDDSimilarityAssessor;
import de.hpi.idd.dysni.store.MemoryStore;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.SymmetricTable;

public class App {

	private static final String DATA_DIR = "data/";
	private static final String DATASET_NAME = "cd";
	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withFirstRecordAsHeader();
	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	public static void main(String[] args) {
		long start = System.nanoTime();
		int i = 0;
		Dataset dataset = Dataset.getForName(App.DATASET_NAME);
		Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> keyHandlers = DatasetManager
				.getKeyHandlers(dataset);
		SimilarityMeasure similarityMeasure = DatasetManager.getSimilarityMeasure(dataset);
		SymmetricTable<String, Boolean> duplicatesToCheck = new SymmetricTable<>();
		try (Reader in = new FileReader(App.DATA_DIR + DatasetManager.getFileName(dataset));
				CSVParser parser = App.FORMAT.parse(in);
				RecordStore<String, Map<String, String>> store = new MemoryStore<>()) {
			DynamicSortedNeighborhoodIndexer<Map<String, String>, String> dysni = new DynamicSortedNeighborhoodIndexer<>(
					store, new IDDSimilarityAssessor(similarityMeasure), keyHandlers);
			for (CSVRecord record : parser) {
				Map<String, String> rec = record.toMap();
				String id = rec.get(DatasetManager.getIdField(dataset));
				dysni.add(rec, id);
				Collection<String> duplicates = dysni.findDuplicates(rec, id);
				for (String duplicate : duplicates) {
					duplicatesToCheck.put(id, duplicate, true);
				}
				System.out.println("Duplicates for " + id + ": " + duplicates);
				i++;
			}
		} catch (IOException e) {
			throw new RuntimeException("Error parsing CSV", e);
		} catch (StoreException e) {
			throw new RuntimeException("Error accessing storage", e);
		} catch (Exception e) {
			App.LOGGER.warning("Exception when closing store: " + e.getMessage());
		}
		long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		Evaluator evaluator = new Evaluator("data/" + DatasetManager.getGroundThruth(dataset));
		evaluator.evaluate(duplicatesToCheck);
	}
}
