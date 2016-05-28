package de.hpi.idd.dysni;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.cd.CDRecordComparator;
import de.hpi.idd.dysni.key.KeyComparator;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.key.LevenshteinComparator;
import de.hpi.idd.dysni.sim.IDDSimilarityMeasure;
import de.hpi.idd.dysni.store.MemoryStore;

public class App {

	private static class CDKeyHandler implements KeyHandler<Map<String, String>, String> {

		@Override
		public String computeKey(final Map<String, String> obj) {
			final String title = obj.get("dtitle");
			final String artist = obj.get("artist");
			return title.substring(0, Math.min(3, title.length())) + artist.substring(0, Math.min(3, artist.length()));
		}

		@Override
		public KeyComparator<String> getComparator() {
			return App.COMPARATOR;
		}
	}

	private static class CDKeyHandler2 implements KeyHandler<Map<String, String>, String> {

		@Override
		public String computeKey(final Map<String, String> obj) {
			final String title = obj.get("dtitle");
			final String artist = obj.get("artist");
			return artist.substring(0, Math.min(3, artist.length())) + title.substring(0, Math.min(3, title.length()));
		}

		@Override
		public KeyComparator<String> getComparator() {
			return App.COMPARATOR;
		}
	}

	private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.5);

	public static void main(final String[] args) {
		final long start = System.nanoTime();
		int i = 0;
		int count = 0;
		final DynamicSortedNeighborhoodIndexer<String, Map<String, String>, String> dysni = new DynamicSortedNeighborhoodIndexer<>(
				new MemoryStore<>(), new IDDSimilarityMeasure(new CDRecordComparator()),
				Arrays.asList(new CDKeyHandler(), new CDKeyHandler2()));
		try {
			final CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
					.parse(new FileReader("data/cd_dataset.csv"));
			for (final CSVRecord record : parser) {
				final Map<String, String> map = record.toMap();
				final IdWrapper<String, Map<String, String>> rec = new IdWrapper<>(map, map.get("did"));
				dysni.add(rec);
				final Collection<String> duplicates = dysni.findDuplicates(rec);
				count += duplicates.isEmpty() ? 0 : 1;
				System.out.println("Duplicates for " + rec.getId() + ": " + duplicates);
				i++;
			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		System.out.println("Found " + count + " duplicates");
	}
}
