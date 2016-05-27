package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.store.RecordStore;

public class DynamicSortedNeighborhoodIndexer<T extends HasId<String>> {

	private static class Tuple<LEFT, RIGHT> {

		private final LEFT left;
		private final RIGHT right;

		public Tuple(final LEFT left, final RIGHT right) {
			this.left = left;
			this.right = right;
		}

		public LEFT getLeft() {
			return left;
		}

		public RIGHT getRight() {
			return right;
		}
	}

	private final UnionFind<String> uf = new UnionFind<>();
	private final SimilarityMeasure comp;
	private final RecordStore<T> store;

	private final List<Tuple<KeyWrapperFactory<T, String>, AVLTree<String, KeyWrapper<String>>>> trees = new ArrayList<>();

	public DynamicSortedNeighborhoodIndexer(final RecordStore<T> store, final SimilarityMeasure comp,
			final List<KeyWrapperFactory<T, String>> factories) {
		this.store = store;
		this.comp = comp;
		for (final KeyWrapperFactory<T, String> factory : factories) {
			trees.add(new Tuple<>(factory, new AVLTree<>(factory.getComparator())));
		}
	}

	public void add(final T rec) {
		final String recId = rec.getId();
		store.storeRecord(recId, rec);
		for (final Tuple<KeyWrapperFactory<T, String>, AVLTree<String, KeyWrapper<String>>> tuple : trees) {
			final KeyWrapperFactory<T, String> factory = tuple.getLeft();
			final AVLTree<String, KeyWrapper<String>> tree = tuple.getRight();
			tree.insert(factory.wrap(rec));
		}
	}

	public Collection<String> findDuplicates(final T rec) {
		final String recId = rec.getId();
		final Set<String> candidates = new HashSet<>();
		for (final Tuple<KeyWrapperFactory<T, String>, AVLTree<String, KeyWrapper<String>>> tuple : trees) {
			final KeyWrapperFactory<T, String> factory = tuple.getLeft();
			final AVLTree<String, KeyWrapper<String>> tree = tuple.getRight();
			final List<String> newCandidates = tree.findCandidates(factory.wrap(rec)).stream()
					.map(KeyWrapper::getObject).collect(Collectors.toList());
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
