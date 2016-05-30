package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.idd.dysni.sim.SimilarityAssessor;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.UnionFind;

public class DynamicSortedNeighborhoodIndexer<ID, RECORD> {

	private final SimilarityAssessor<RECORD> comp;
	private final List<DysniIndex<RECORD, ?, ID>> indexes = new ArrayList<>();
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<ID, RECORD> store, final SimilarityAssessor<RECORD> comp,
			final Collection<KeyHandler<RECORD, ?>> keyHandlers) {
		this.store = store;
		this.comp = comp;
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
			if (comp.areSimilar(rec, store.getRecord(candidate))) {
				uf.union(recId, candidate);
			}
		}
		return uf.getComponent(recId);
	}
}
