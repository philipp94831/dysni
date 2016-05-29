package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.sim.GenericSimilarityMeasure;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.UnionFind;

public class DynamicSortedNeighborhoodIndexer<ID, RECORD, KEY extends Comparable<KEY>> {

	private final GenericSimilarityMeasure<RECORD> comp;
	private final List<DysniIndex<RECORD, KEY, ID>> indexes = new ArrayList<>();
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<ID, RECORD> store, final GenericSimilarityMeasure<RECORD> comp, final List<KeyHandler<RECORD, KEY>> keyHandlers) {
		this.store = store;
		this.comp = comp;
		for (final KeyHandler<RECORD, KEY> keyHandler : keyHandlers) {
			indexes.add(new DysniIndex<>(keyHandler));
		}
	}

	public void add(final RECORD rec, final ID recId) throws StoreException {
		store.storeRecord(recId, rec);
		for (final DysniIndex<RECORD, KEY, ID> index : indexes) {
			index.insert(rec, recId);
		}
	}

	public Collection<ID> findDuplicates(final RECORD rec, final ID recId) throws StoreException {
		final Set<ID> candidates = new HashSet<>();
		for (final DysniIndex<RECORD, KEY, ID> index : indexes) {
			final Collection<ID> newCandidates = index.findCandidates(rec);
			candidates.addAll(newCandidates);
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
