package de.hpi.idd;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.data.Dataset;
import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.DynamicSortedNeighborhoodIndexer;
import de.hpi.idd.sim.SimilarityClassifier;
import de.hpi.idd.store.MemoryStore;
import de.hpi.idd.store.RecordStore;
import de.hpi.idd.store.StoreException;
import de.hpi.idd.util.SymmetricTable;

public class App {

	private enum ERType {
		BRUTE_FORCE, DYSNI
	}

	private static final String DATASET_NAME = "cd";
	private static final ERType ER_TYPE = ERType.DYSNI;
	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withFirstRecordAsHeader();
	private static final Logger LOGGER = Logger.getLogger(App.class.getName());

	private static EntityResolver<Map<String, Object>, String> getEntityResolver(Dataset dataset,
			SimilarityClassifier<Map<String, Object>> similarityMeasure,
			RecordStore<String, Map<String, Object>> store) {
		switch (ER_TYPE) {
		case DYSNI:
			Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> configs = dataset.getConfigs();
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
		DatasetUtils du = dataset.getDataset();
		SymmetricTable<String, Boolean> duplicatesToCheck = new SymmetricTable<>();
		try (Reader in = new InputStreamReader(dataset.getFile());
				CSVParser parser = FORMAT.parse(in);
				RecordStore<String, Map<String, Object>> store = new MemoryStore<>()) {
			EntityResolver<Map<String, Object>, String> er = getEntityResolver(dataset, new IDDSimilarityClassifier(du),
					store);
			for (CSVRecord record : parser) {
				Map<String, Object> rec = du.parseRecord(record.toMap());
				String id = (String) rec.get(dataset.getIdField());
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
			LOGGER.warning("Error executing App: " + e.getMessage());
			e.printStackTrace();
		}
		long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		Evaluator evaluator = new Evaluator(dataset.getGroundThruth());
		evaluator.evaluate(duplicatesToCheck);
	}
}
