package de.hpi.idd.dysni;

import java.util.Collections;
import java.util.List;

import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.avl.Element;
import de.hpi.idd.dysni.avl.KeyComparator;
import de.hpi.idd.dysni.comp.LevenshteinComparator;
import de.hpi.idd.dysni.records.CDRecord;
import de.hpi.idd.dysni.records.CDRecordComparator;

public class DynamicSortedNeighborhoodIndexer {
	
	private AVLTree<String, CDRecordElement> tree = new AVLTree<>();
	private UnionFind<String> uf = new UnionFind<>();
	
	public static void main(String[] args) {
		DynamicSortedNeighborhoodIndexer dysni = new DynamicSortedNeighborhoodIndexer();
		CDRecord rec = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None", (short) 2016, Collections.emptyList());
		CDRecord rec2 = new CDRecord("1", "The Rolling Tones", "Overpriced Best CD", "trash", "Pop", "None", (short) 2017, Collections.emptyList());
		dysni.add(rec);
		dysni.add(rec2);
	}
	

	private void add(CDRecord rec) {
		List<CDRecordElement> candidates = tree.insert(new CDRecordElement(rec));
		for(CDRecordElement candidate : candidates) {
			if(CDRecordComparator.similar(candidate.record, rec)) {
				uf.union(rec.getdId(), candidate.record.getdId());
			}
		}
	}


	private static class CDRecordElement implements Element<String> {
		
		private final CDRecord record;
		private static final LevenshteinComparator COMPARATOR = new LevenshteinComparator(0.5);
		
		
		public CDRecordElement(CDRecord record) {
			this.record = record;
		}

		@Override
		public KeyComparator<String> getComparator() {
			return COMPARATOR;
		}

		@Override
		public String getKey() {
			return record.getdTitle().substring(0, 3) + record.getArtist().substring(0, 3);
		}
		
	}
}
