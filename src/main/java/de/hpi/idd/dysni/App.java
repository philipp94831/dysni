package de.hpi.idd.dysni;

import java.util.Arrays;
import java.util.Collections;

import de.hpi.idd.dysni.avl.KeyComparator;
import de.hpi.idd.dysni.comp.LevenshteinComparator;
import de.hpi.idd.dysni.records.CDRecord;
import de.hpi.idd.dysni.records.CDRecordComparator;

public class App {

	public static void main(String[] args) {
		DynamicSortedNeighborhoodIndexer<CDRecord> dysni = new DynamicSortedNeighborhoodIndexer<>(
				new CDRecordComparator(), Arrays.asList(new CDRecordFactory()));
		CDRecord rec = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None",
				(short) 2016, Collections.emptyList());
		CDRecord rec2 = new CDRecord("1", "The Rolling Tones", "Overpriced Best CD", "trash", "Pop", "None",
				(short) 2017, Collections.emptyList());
		dysni.add(rec);
		dysni.add(rec2);
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
			public String getKey() {
				return object.getdTitle().substring(0, 3) + object.getArtist().substring(0, 3);
			}

		}
	}

}
