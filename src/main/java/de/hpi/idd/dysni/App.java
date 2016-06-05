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

import de.hpi.idd.BruteForceEntityResolver;
import de.hpi.idd.EntityResolver;
import de.hpi.idd.Evaluator;
import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.data.Dataset;
import de.hpi.idd.dysni.sim.IDDSimilarityClassifier;
import de.hpi.idd.dysni.sim.SimilarityClassifier;
import de.hpi.idd.dysni.store.MemoryStore;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.SymmetricTable;

public class App {

	private enum ERType {
		BRUTE_FORCE, DYSNI
	}

	private static final String DATA_DIR = "data/";
	private static final String DATASET_NAME = "cd";
	private static final ERType ER_TYPE = ERType.DYSNI;
	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withFirstRecordAsHeader();
	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	private static EntityResolver<Map<String, String>, String> getEntityResolver(Dataset dataset,
			SimilarityClassifier<Map<String, String>> similarityMeasure,
			RecordStore<String, Map<String, String>> store) {
		switch (App.ER_TYPE) {
		case DYSNI:
			Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> configs = dataset.getConfigs();
			return new DynamicSortedNeighborhoodIndexer<>(store, similarityMeasure, configs);
		case BRUTE_FORCE:
			return new BruteForceEntityResolver<>(store, similarityMeasure);
		default:
			return null;
		}
	}

	public static void main(String[] args) {
		long start = System.nanoTime();
		int i = 0;
		Dataset dataset = Dataset.getForName(DATASET_NAME);
		SimilarityMeasure similarityMeasure = dataset.getSimilarityMeasure();
		SymmetricTable<String, Boolean> duplicatesToCheck = new SymmetricTable<>();
		try (Reader in = new FileReader(DATA_DIR + dataset.getFileName());
				CSVParser parser = FORMAT.parse(in);
				RecordStore<String, Map<String, String>> store = new MemoryStore<>()) {
			EntityResolver<Map<String, String>, String> er = App.getEntityResolver(dataset,
					new IDDSimilarityClassifier(similarityMeasure), store);
			for (CSVRecord record : parser) {
				Map<String, String> rec = record.toMap();
				String id = rec.get(dataset.getIdField());
				er.add(rec, id);
				Collection<String> duplicates = er.findDuplicates(rec, id);
				for (String duplicate : duplicates) {
					duplicatesToCheck.put(id, duplicate, true);
				}
				i++;
				System.out.println(i + " Duplicates for " + id + ": " + duplicates);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error parsing CSV", e);
		} catch (StoreException e) {
			throw new RuntimeException("Error accessing storage", e);
		} catch (Exception e) {
			LOGGER.warning("Exception when closing store: " + e.getMessage());
		}
		long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		Evaluator evaluator = new Evaluator("data/" + dataset.getGroundThruth());
		evaluator.evaluate(duplicatesToCheck);
	}
}
