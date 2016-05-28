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

public class DynamicSortedNeighborhoodIndexer<ID, ELEMENT, KEY extends Comparable<KEY>> {

	private final GenericSimilarityMeasure<ELEMENT> comp;
	private final List<DysniIndex<ELEMENT, KEY, ID>> indexes = new ArrayList<>();
	private final RecordStore<ID, ELEMENT> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<ID, ELEMENT> store, final GenericSimilarityMeasure<ELEMENT> comp, final List<KeyHandler<ELEMENT, KEY>> keyHandlers) {
		this.store = store;
		this.comp = comp;
		for (final KeyHandler<ELEMENT, KEY> keyHandler : keyHandlers) {
			indexes.add(new DysniIndex<>(keyHandler));
		}
	}

	public void add(final IdWrapper<ID, ELEMENT> rec) throws StoreException {
		final ID recId = rec.getId();
		store.storeRecord(recId, rec.getObject());
		for (final DysniIndex<ELEMENT, KEY, ID> index : indexes) {
			index.insert(rec.getObject(), rec.getId());
		}
	}

	public Collection<ID> findDuplicates(final IdWrapper<ID, ELEMENT> rec) throws StoreException {
		final Set<ID> candidates = new HashSet<>();
		for (final DysniIndex<ELEMENT, KEY, ID> index : indexes) {
			final Collection<ID> newCandidates = index.findCandidates(rec.getObject());
			candidates.addAll(newCandidates);
		}
		candidates.remove(rec.getId());
		for (final ID candidate : candidates) {
			if (comp.areSimilar(rec.getObject(), store.getRecord(candidate))) {
				uf.union(rec.getId(), candidate);
			}
		}
		return uf.getComponent(rec.getId());
	}
}
