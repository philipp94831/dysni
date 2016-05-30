package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.hpi.idd.dysni.sim.SimilarityAssessor;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.UnionFind;

public class DynamicSortedNeighborhoodIndexer<RECORD, ID> {

	private final SimilarityAssessor<RECORD> sim;
	private final Collection<DysniIndex<RECORD, ?, ID>> indexes = new ArrayList<>();
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<ID, RECORD> store, final SimilarityAssessor<RECORD> sim,
			final Collection<KeyHandler<RECORD, ?>> keyHandlers) {
		this.store = store;
		this.sim = sim;
		for (final KeyHandler<RECORD, ?> keyHandler : keyHandlers) {
			indexes.add(new DysniIndex<>(keyHandler));
		}
	}

	public void add(final RECORD rec, final ID recId) throws StoreException {
		store.storeRecord(recId, rec);
		for (final DysniIndex<RECORD, ?, ID> index : indexes) {
			index.insert(rec, recId);
		}
	}

	public Collection<ID> findDuplicates(final RECORD rec, final ID recId) throws StoreException {
		final Set<ID> candidates = new HashSet<>();
		for (final DysniIndex<RECORD, ?, ID> index : indexes) {
			candidates.addAll(index.findCandidates(rec));
		}
		candidates.remove(recId);
		for (final ID candidate : candidates) {
			if (sim.areSimilar(rec, store.getRecord(candidate))) {
				uf.union(recId, candidate);
			}
		}
		return uf.getComponent(recId);
	}
}
