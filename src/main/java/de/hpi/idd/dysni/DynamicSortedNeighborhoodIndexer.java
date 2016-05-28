package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.sim.GenericSimilarityMeasure;
import de.hpi.idd.dysni.store.RecordStore;

public class DynamicSortedNeighborhoodIndexer<ID, VALUE, KEY extends Comparable<KEY>> {

	private final GenericSimilarityMeasure<VALUE> comp;
	private final List<DysniIndex<VALUE, KEY, ID>> indexes = new ArrayList<>();
	private final RecordStore<ID, VALUE> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<ID, VALUE> store, final GenericSimilarityMeasure<VALUE> comp, final List<KeyHandler<VALUE, KEY>> keyHandlers) {
		this.store = store;
		this.comp = comp;
		for (final KeyHandler<VALUE, KEY> keyHandler : keyHandlers) {
			indexes.add(new DysniIndex<>(keyHandler));
		}
	}

	public void add(final IdWrapper<ID, VALUE> rec) {
		final ID recId = rec.getId();
		store.storeRecord(recId, rec.getObject());
		for (final DysniIndex<VALUE, KEY, ID> index : indexes) {
			index.insert(rec.getObject(), rec.getId());
		}
	}

	public Collection<ID> findDuplicates(final IdWrapper<ID, VALUE> rec) {
		final Set<ID> candidates = new HashSet<>();
		for (final DysniIndex<VALUE, KEY, ID> index : indexes) {
			final Collection<ID> newCandidates = index.findCandidates(rec.getObject());
			newCandidates.remove(rec.getId());
			candidates.addAll(newCandidates);
		}
		for (final ID candidate : candidates) {
			if (comp.getThreshold() <= comp.calculateSimilarity(rec.getObject(), store.getRecord(candidate))) {
				uf.union(rec.getId(), candidate);
			}
		}
		return uf.getComponent(rec.getId());
	}
}
