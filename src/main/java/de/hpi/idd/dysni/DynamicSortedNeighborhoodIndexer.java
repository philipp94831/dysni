package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.hpi.idd.RecordComparator;
import de.hpi.idd.dysni.avl.AVLTree;

public class DynamicSortedNeighborhoodIndexer<T> {

	private UnionFind<T> uf = new UnionFind<>();
	private final RecordComparator<T> comp;
	private final List<Tuple<WrapperFactory<T>, AVLTree<String, ElementWrapper<T>>>> trees = new ArrayList<>();

	public DynamicSortedNeighborhoodIndexer(RecordComparator<T> comp, List<WrapperFactory<T>> factories) {
		this.comp = comp;
		for (WrapperFactory<T> factory : factories) {
			this.trees.add(new Tuple<>(factory, new AVLTree<>()));
		}
	}

	public void add(T rec) {
		Set<T> candidates = new HashSet<>();
		for (Tuple<WrapperFactory<T>, AVLTree<String, ElementWrapper<T>>> entry : trees) {
			candidates.addAll(entry.getRight().insert(entry.getLeft().wrap(rec)).stream().map(ElementWrapper::getObject)
					.collect(Collectors.toList()));
		}
		for (T candidate : candidates) {
			if (comp.areSimilar(candidate, rec)) {
				uf.union(rec, candidate);
			}
		}
	}

	private static class Tuple<LEFT, RIGHT> {

		private final LEFT left;
		private final RIGHT right;

		public Tuple(LEFT left, RIGHT right) {
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
}
