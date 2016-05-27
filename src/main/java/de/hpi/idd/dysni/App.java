package de.hpi.idd.dysni;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.dysni.comp.LevenshteinComparator;
import de.hpi.idd.dysni.records.CDRecordComparator;

public class App {

	private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.5);

	public static void main(String[] args) {
		long start = System.nanoTime();
		int i = 0;
		int count = 0;
		DynamicSortedNeighborhoodIndexer<Map<String, String>> dysni = new DynamicSortedNeighborhoodIndexer<>(new CDRecordComparator(),
				Arrays.asList(new CDRecordFactory(), new CDRecordFactory2()));
		try {
			CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader("data/cd_dataset.csv"));
			for (CSVRecord record : parser) {
				Map<String, String> rec = record.toMap();
				Collection<Map<String, String>> duplicates = dysni.add(rec);
				count += duplicates.isEmpty() ? 0 : 1;
				System.out.println("Duplicates for " + rec.get("did") + ": "
						+ duplicates.stream().map(d -> d.get("did")).collect(Collectors.toList()));
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

	private static class CDRecordFactory implements WrapperFactory<Map<String, String>> {

		@Override
		public ElementWrapper<Map<String, String>> wrap(Map<String, String> rec) {
			String title = rec.get("dtitle");
			String artist = rec.get("artist");
			String key = title.substring(0, Math.min(3, title.length()))
					+ artist.substring(0, Math.min(3, artist.length()));
			return new ElementWrapper<>(rec, key, COMPARATOR);
		}
	}

	private static class CDRecordFactory2 implements WrapperFactory<Map<String, String>> {

		@Override
		public ElementWrapper<Map<String, String>> wrap(Map<String, String> rec) {
			String title = rec.get("dtitle");
			String artist = rec.get("artist");
			String key = artist.substring(0, Math.min(3, artist.length()))
					+ title.substring(0, Math.min(3, title.length()));
			return new ElementWrapper<>(rec, key, COMPARATOR);
		}
	}
}
