package de.hpi.idd.dysni;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.dysni.comp.LevenshteinComparator;
import de.hpi.idd.dysni.records.CDRecordComparator;
import de.hpi.idd.dysni.simavl.KeyComparator;
import de.hpi.idd.dysni.store.MemoryStore;
import de.hpi.idd.dysni.store.RecordStore;

public class App {

	private static class CDRecordFactory implements KeyComputer<IdWrapper, String> {

		@Override
		public String computeKey(final IdWrapper rec) {
			final Map<String, String> obj = rec.getObject();
			final String title = obj.get("dtitle");
			final String artist = obj.get("artist");
			return title.substring(0, Math.min(3, title.length())) + artist.substring(0, Math.min(3, artist.length()));
		}

		@Override
		public KeyComparator<String> getComparator() {
			return App.COMPARATOR;
		}
	}

	private static class CDRecordFactory2 implements KeyComputer<IdWrapper, String> {

		@Override
		public String computeKey(final IdWrapper rec) {
			final Map<String, String> obj = rec.getObject();
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
		final RecordStore<IdWrapper> store = new MemoryStore<>();
		final DynamicSortedNeighborhoodIndexer<IdWrapper> dysni = new DynamicSortedNeighborhoodIndexer<>(store,
				new CDRecordComparator(store), Arrays.asList(new CDRecordFactory(), new CDRecordFactory2()));
		try {
			final CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
					.parse(new FileReader("data/cd_dataset.csv"));
			for (final CSVRecord record : parser) {
				final IdWrapper rec = new IdWrapper(record.toMap());
				dysni.add(rec);
				final Collection<String> duplicates = dysni.findDuplicates(rec);
				count += duplicates.isEmpty() ? 0 : 1;
				System.out.println("Duplicates for " + rec.getId() + ": " + duplicates);
				i++;
			}
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		System.out.println("Found " + count + " duplicates");
	}
}
