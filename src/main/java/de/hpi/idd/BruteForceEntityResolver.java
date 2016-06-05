package de.hpi.idd;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;

import de.hpi.idd.dysni.sim.SimilarityClassifier;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.util.UnionFind;

/**
 * Brute force entity resolver which compares every record to every other
 *
 * @param <RECORD>
 *            the type of records to be resolved
 * @param <ID>
 *            the type of ids representing each record
 */
public class BruteForceEntityResolver<RECORD, ID> implements EntityResolver<RECORD, ID> {

	private final SimilarityClassifier<RECORD> sim;
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public BruteForceEntityResolver(RecordStore<ID, RECORD> store, SimilarityClassifier<RECORD> sim) {
		this.store = store;
		this.sim = sim;
	}

	@Override
	public void add(RECORD rec, ID recId) throws Exception {
		store.storeRecord(recId, rec);
	}

	@Override
	public Collection<ID> findDuplicates(RECORD rec, ID recId) throws Exception {
		Iterator<Pair<ID, RECORD>> it = store.all();
		while (it.hasNext()) {
			Pair<ID, RECORD> entry = it.next();
			if (sim.areSimilar(rec, entry.getRight()) && !recId.equals(entry.getLeft())) {
				uf.union(recId, entry.getLeft());
			}
		}
		return uf.getComponent(recId);
	}

}
