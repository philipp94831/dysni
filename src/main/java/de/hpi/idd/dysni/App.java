package de.hpi.idd.dysni;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.Dataset;
import de.hpi.idd.DatasetManager;
import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.cd.CDRecordMatchingQualityChecker;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.sim.IDDSimilarityMeasure;
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
		final List<KeyHandler<Map<String, String>, String>> keyHandlers = DatasetManager.getKeyHandlers(dataset);
		final SimilarityMeasure similarityMeasure = DatasetManager.getSimilarityMeasure(dataset);
		final DynamicSortedNeighborhoodIndexer<String, Map<String, String>, String> dysni = new DynamicSortedNeighborhoodIndexer<>(
				new MemoryStore<>(), new IDDSimilarityMeasure(similarityMeasure), keyHandlers);
		final SymmetricTable<String, Boolean> duplicatesToCheck = new SymmetricTable<>();
		try (final CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
				.parse(new FileReader(App.DATA_DIR + DatasetManager.getFileName(dataset)))) {
			for (final CSVRecord record : parser) {
				final Map<String, String> map = record.toMap();
				final IdWrapper<String, Map<String, String>> rec = new IdWrapper<>(map,
						map.get(DatasetManager.getIdColumn(dataset)));
				dysni.add(rec);
				final Collection<String> duplicates = dysni.findDuplicates(rec);
				if (!duplicates.isEmpty()) {
					for (final String duplicate : duplicates) {
						duplicatesToCheck.put(rec.getId(), duplicate, true);
					}
				}
				System.out.println("Duplicates for " + rec.getId() + ": " + duplicates);
				i++;
			}
		} catch (final IOException e) {
			throw new RuntimeException("Error parsing CSV", e);
		} catch (final StoreException e) {
			throw new RuntimeException("Error accessing storage", e);
		}
		final long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		final CDRecordMatchingQualityChecker checker = new CDRecordMatchingQualityChecker();
		checker.checkDuplicates(duplicatesToCheck);
	}
}
