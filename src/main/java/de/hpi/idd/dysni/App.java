package de.hpi.idd.dysni;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.Dataset;
import de.hpi.idd.KeyHandlerManager;
import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.SimilarityMeasureManager;
import de.hpi.idd.cd.CDRecordMatchingQualityChecker;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.sim.IDDSimilarityMeasure;
import de.hpi.idd.dysni.store.MemoryStore;

public class App {

	public static void main(final String[] args) {
		final long start = System.nanoTime();
		int i = 0;
		int count = 0;
		final Dataset dataset = Dataset.getForName("cd");
		final List<KeyHandler<Map<String, String>, String>> keyHandlers = KeyHandlerManager.getKeyHandlers(dataset);
		final SimilarityMeasure similarityMeasure = SimilarityMeasureManager.getSimilarityMeasure(dataset);
		final DynamicSortedNeighborhoodIndexer<String, Map<String, String>, String> dysni = new DynamicSortedNeighborhoodIndexer<>(
				new MemoryStore<>(), new IDDSimilarityMeasure(similarityMeasure), keyHandlers);
		final Map<String, List<String>> duplicatesToCheck = new HashMap<>();
		final Set<String> keys = new HashSet<>();
		try {
			final CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
					.parse(new FileReader("data/cd_dataset.csv"));
			for (final CSVRecord record : parser) {
				final Map<String, String> map = record.toMap();
				final IdWrapper<String, Map<String, String>> rec = new IdWrapper<>(map, map.get("did"));
				dysni.add(rec);
				final Collection<String> duplicates = dysni.findDuplicates(rec);
				if (keys.contains(rec.getId())) {
					System.out.println("hamma schon");
				} else {
					keys.add(rec.getId());
				}
				if (!duplicates.isEmpty()) {
					duplicatesToCheck.put(rec.getId(), new LinkedList<>());
					duplicatesToCheck.get(rec.getId()).addAll(duplicates);
				}
				count += duplicates.isEmpty() ? 0 : 1;
				System.out.println("Duplicates for " + rec.getId() + ": " + duplicates.size());
				i++;
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		System.out.println("Found " + count + " duplicates");
		final CDRecordMatchingQualityChecker checker = new CDRecordMatchingQualityChecker();
		checker.checkDuplicates(duplicatesToCheck);
	}
}
