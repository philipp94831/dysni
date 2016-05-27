package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.dysni.simavl.SimilarityAwareAVLTree;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.util.Tuple;

public class DynamicSortedNeighborhoodIndexer<T extends HasId<String>> {

	private final UnionFind<String> uf = new UnionFind<>();
	private final SimilarityMeasure comp;
	private final RecordStore<T> store;

	private final List<Tuple<KeyComputer<T, String>, SimilarityAwareAVLTree<String, String>>> trees = new ArrayList<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<T> store, final SimilarityMeasure comp,
			final List<KeyComputer<T, String>> factories) {
		this.store = store;
		this.comp = comp;
		for (final KeyComputer<T, String> factory : factories) {
			trees.add(new Tuple<>(factory, new SimilarityAwareAVLTree<>(factory.getComparator())));
		}
	}

	public void add(final T rec) {
		final String recId = rec.getId();
		store.storeRecord(recId, rec);
		for (final Tuple<KeyComputer<T, String>, SimilarityAwareAVLTree<String, String>> tuple : trees) {
			final KeyComputer<T, String> factory = tuple.getLeft();
			final SimilarityAwareAVLTree<String, String> tree = tuple.getRight();
			tree.insert(factory.computeKey(rec), rec.getId());
		}
	}

	public Collection<String> findDuplicates(final T rec) {
		final String recId = rec.getId();
		final Set<String> candidates = new HashSet<>();
		for (final Tuple<KeyComputer<T, String>, SimilarityAwareAVLTree<String, String>> tuple : trees) {
			final KeyComputer<T, String> factory = tuple.getLeft();
			final SimilarityAwareAVLTree<String, String> tree = tuple.getRight();
			final Collection<String> newCandidates = tree.findCandidates(factory.computeKey(rec), rec.getId());
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
