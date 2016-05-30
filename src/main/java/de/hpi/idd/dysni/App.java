package de.hpi.idd.dysni;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.Dataset;
import de.hpi.idd.DatasetManager;
import de.hpi.idd.Evaluator;
import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.dysni.sim.IDDAssessor;
import de.hpi.idd.dysni.store.MemoryStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.SymmetricTable;

public class App {

	private static final String DATA_DIR = "data/";
	private static final String DATASET_NAME = "cd";

	public static void main(final String[] args) {
		final long start = System.nanoTime();
		int i = 0;
		final Dataset dataset = Dataset.getForName(App.DATASET_NAME);
		final Collection<KeyHandler<Map<String, String>, ?>> keyHandlers = DatasetManager.getKeyHandlers(dataset);
		final SimilarityMeasure similarityMeasure = DatasetManager.getSimilarityMeasure(dataset);
		final DynamicSortedNeighborhoodIndexer<String, Map<String, String>> dysni = new DynamicSortedNeighborhoodIndexer<>(
				new MemoryStore<>(), new IDDAssessor(similarityMeasure), keyHandlers);
		final SymmetricTable<String, Boolean> duplicatesToCheck = new SymmetricTable<>();
		try (final CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
				.parse(new FileReader(App.DATA_DIR + DatasetManager.getFileName(dataset)))) {
			for (final CSVRecord record : parser) {
				final Map<String, String> rec = record.toMap();
				final String id = rec.get(DatasetManager.getIdField(dataset));
				dysni.add(rec, id);
				final Collection<String> duplicates = dysni.findDuplicates(rec, id);
				for (final String duplicate : duplicates) {
					duplicatesToCheck.put(id, duplicate, true);
				}
				System.out.println("Duplicates for " + id + ": " + duplicates);
				i++;
			}
		} catch (final IOException e) {
			throw new RuntimeException("Error parsing CSV", e);
		} catch (final StoreException e) {
			throw new RuntimeException("Error accessing storage", e);
		}
		final long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		final Evaluator evaluator = new Evaluator("data/" + DatasetManager.getGroundThruth(dataset));
		evaluator.evaluate(duplicatesToCheck);
	}
}
