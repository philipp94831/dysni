package de.hpi.idd.dysni;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.dysni.avl.KeyComparator;
import de.hpi.idd.dysni.comp.LevenshteinComparator;
import de.hpi.idd.dysni.records.CDRecord;
import de.hpi.idd.dysni.records.CDRecordComparator;
import de.hpi.idd.dysni.records.CDRecordParser;

public class App {

	public static void main(String[] args) {
		long start = System.nanoTime();
		int i = 0;
		DynamicSortedNeighborhoodIndexer<CDRecord> dysni = new DynamicSortedNeighborhoodIndexer<>(
				new CDRecordComparator(), Arrays.asList(new CDRecordFactory()));
		try {
			CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader("data/cd_dataset.csv"));
			CDRecordParser cdparser = new CDRecordParser();
			for (CSVRecord record : parser) {
				CDRecord rec = cdparser.parse(record);
				System.out.println("Duplicates for " + rec.getdId() + ": " + dysni.add(rec));
				i++;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long time = System.nanoTime() - start;
		System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
	}

	private static class CDRecordFactory implements WrapperFactory<CDRecord> {

		@Override
		public ElementWrapper<CDRecord> wrap(CDRecord rec) {
			return new CDRecordElement(rec);
		}

		private static class CDRecordElement extends ElementWrapper<CDRecord> {

			private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.5);

			public CDRecordElement(CDRecord record) {
				super(record);
			}

			@Override
			public KeyComparator<String> getComparator() {
				return COMPARATOR;
			}

			@Override
			protected String computeKey() {
				return object.getdTitle().substring(0, Math.min(3, object.getdTitle().length()))
						+ object.getArtist().substring(0, Math.min(3, object.getArtist().length()));
			}
		}
	}
}
