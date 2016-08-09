package de.hpi.idd;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.data.Dataset;
import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.DynamicSortedNeighborhoodIndexer;
import de.hpi.idd.store.MemoryStore;
import de.hpi.idd.store.RecordStore;
import de.hpi.idd.store.StoreException;
import de.hpi.idd.util.SymmetricTable;

public class App {

	private enum ERType {
		BRUTE_FORCE, DYSNI
	}

	private static final Dataset DATASET = Dataset.PEOPLE;
	private static final ERType ER_TYPE = ERType.DYSNI;
	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withFirstRecordAsHeader();

	private static EntityResolver<Map<String, Object>, String> getEntityResolver(Dataset dataset,
			RecordStore<String, Map<String, Object>> store) {
		IDDSimilarityClassifier similarityMeasure = new IDDSimilarityClassifier(DATASET.getDataset());
		switch (ER_TYPE) {
		case DYSNI:
			Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> configs = dataset.getConfigs();
			return new DynamicSortedNeighborhoodIndexer<>(store, similarityMeasure).addIndexes(configs)
					.setParallelizable(dataset.isParallelizable());
		case BRUTE_FORCE:
			return new BruteForceEntityResolver<>(store, similarityMeasure)
					.setParallelizable(dataset.isParallelizable());
		default:
			throw new IllegalArgumentException("Unknown EntityResolver: " + ER_TYPE);
		}
	}

	public static void main(String[] args) {
		DatasetUtils du = DATASET.getDataset();
		SymmetricTable<String, Boolean> duplicatesToCheck = new SymmetricTable<>();
		try (CSVParser parser = FORMAT.parse(new InputStreamReader(DATASET.getFile()));
				EntityResolver<Map<String, Object>, String> er = getEntityResolver(DATASET, new MemoryStore<>())) {
			int i = 0;
			long start = System.nanoTime();
			for (CSVRecord record : parser) {
				Map<String, Object> rec = du.parseRecord(record.toMap());
				String id = (String) rec.get(Dataset.ID);
				Collection<String> duplicates = er.insert(rec, id);
				for (String duplicate : duplicates) {
					duplicatesToCheck.put(id, duplicate, true);
				}
				i++;
//				System.out.println(i + " Duplicates for " + id + ": " + duplicates);
			}
			long time = System.nanoTime() - start;
			System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		} catch (StoreException e) {
			throw new RuntimeException("Error accessing storage", e);
		} catch (IOException e) {
			throw new RuntimeException("Error parsing CSV", e);
		}
		Evaluator evaluator = new Evaluator(DATASET.getGroundThruth());
		evaluator.evaluate(duplicatesToCheck);
	}
}
