package de.hpi.idd.dysni;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.dysni.comp.LevenshteinComparator;
import de.hpi.idd.dysni.records.CDRecord;
import de.hpi.idd.dysni.records.CDRecordComparator;
import de.hpi.idd.dysni.records.CDRecordParser;

public class App {

	private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.5);

	public static void main(String[] args) {
		long start = System.nanoTime();
		int i = 0;
		int count = 0;
		DynamicSortedNeighborhoodIndexer<CDRecord> dysni = new DynamicSortedNeighborhoodIndexer<>(
				new CDRecordComparator(), Arrays.asList(new CDRecordFactory(), new CDRecordFactory2()));
		try {
			CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader("data/cd_dataset.csv"));
			CDRecordParser cdparser = new CDRecordParser();
			for (CSVRecord record : parser) {
				CDRecord rec = cdparser.parse(record);
				Collection<CDRecord> duplicates = dysni.add(rec);
				count += duplicates.isEmpty() ? 0 : 1;
				System.out.println("Duplicates for " + rec.getdId() + ": " + duplicates);
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
		System.out.println("Found " + count + " duplicates");
	}

	private static class CDRecordFactory implements WrapperFactory<CDRecord> {

		@Override
		public ElementWrapper<CDRecord> wrap(CDRecord rec) {
			String key = rec.getdTitle().substring(0, Math.min(3, rec.getdTitle().length()))
					+ rec.getArtist().substring(0, Math.min(3, rec.getArtist().length()));
			return new ElementWrapper<>(rec, key, COMPARATOR);
		}
	}

	private static class CDRecordFactory2 implements WrapperFactory<CDRecord> {

		@Override
		public ElementWrapper<CDRecord> wrap(CDRecord rec) {
			String key = rec.getArtist().substring(0, Math.min(3, rec.getArtist().length()))
					+ rec.getdTitle().substring(0, Math.min(3, rec.getdTitle().length()));
			return new ElementWrapper<>(rec, key, COMPARATOR);
		}
	}
}
