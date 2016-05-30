package de.hpi.idd;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import de.hpi.idd.dysni.sim.SimilarityAssessor;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.util.UnionFind;

public class BruteForce<RECORD, ID> implements EntityResolver<RECORD, ID> {

	private final SimilarityAssessor<RECORD> sim;
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public BruteForce(RecordStore<ID, RECORD> store, SimilarityAssessor<RECORD> sim) {
		this.store = store;
		this.sim = sim;
	}

	@Override
	public void add(RECORD rec, ID recId) throws Exception {
		store.storeRecord(recId, rec);
	}

	@Override
	public Collection<ID> findDuplicates(RECORD rec, ID recId) throws Exception {
		Iterator<Entry<ID, RECORD>> it = store.all();
		while (it.hasNext()) {
			Entry<ID, RECORD> entry = it.next();
			if (sim.areSimilar(rec, entry.getValue()) && !recId.equals(entry.getKey())) {
				uf.union(recId, entry.getKey());
			}
		}
		return uf.getComponent(recId);
	}

}
