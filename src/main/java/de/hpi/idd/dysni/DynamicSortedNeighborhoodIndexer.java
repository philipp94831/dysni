package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.dysni.store.RecordStore;

public class DynamicSortedNeighborhoodIndexer<T extends HasId<String>> {

	private final UnionFind<String> uf = new UnionFind<>();
	private final SimilarityMeasure comp;
	private final RecordStore<T> store;

	private final List<DysniIndex<T, String, String>> indexes = new ArrayList<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<T> store, final SimilarityMeasure comp,
			final List<KeyHandler<T, String>> keyHandlers) {
		this.store = store;
		this.comp = comp;
		for (final KeyHandler<T, String> keyHandler : keyHandlers) {
			indexes.add(new DysniIndex<>(keyHandler));
		}
	}

	public void add(final T rec) {
		final String recId = rec.getId();
		store.storeRecord(recId, rec);
		for (final DysniIndex<T, String, String> index : indexes) {
			index.insert(rec, rec.getId());
		}
	}

	public Collection<String> findDuplicates(final T rec) {
		final String recId = rec.getId();
		final Set<String> candidates = new HashSet<>();
		for (final DysniIndex<T, String, String> index : indexes) {
			final Collection<String> newCandidates = index.findCandidates(rec, rec.getId());
			candidates.addAll(newCandidates);
		}
		for (final String candidate : candidates) {
			if (comp.getThreshold() <= comp.calculateSimilarity(recId, candidate, new HashMap<>())) {
				uf.union(recId, candidate);
			}
		}
		return uf.getComponent(recId);
	}
}
