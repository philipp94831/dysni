package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
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
			trees.add(new Tuple<>(factory, new AVLTree<>()));
		}
	}

	public Collection<T> add(T rec) {
		Set<T> candidates = new HashSet<>();
		for (Tuple<WrapperFactory<T>, AVLTree<String, ElementWrapper<T>>> tuple : trees) {
			WrapperFactory<T> factory = tuple.getLeft();
			AVLTree<String, ElementWrapper<T>> tree = tuple.getRight();
			List<T> newCandidates = tree.insert(factory.wrap(rec)).stream().map(ElementWrapper::getObject)
					.collect(Collectors.toList());
			candidates.addAll(newCandidates);
		}
		for (T candidate : candidates) {
			if (comp.areSimilar(candidate, rec)) {
				uf.union(rec, candidate);
			}
		}
		return uf.getComponent(rec);
	}
	
	public Collection<T> findDuplicates(T rec) {
		Set<T> candidates = new HashSet<>();
		for (Tuple<WrapperFactory<T>, AVLTree<String, ElementWrapper<T>>> tuple : trees) {
			WrapperFactory<T> factory = tuple.getLeft();
			AVLTree<String, ElementWrapper<T>> tree = tuple.getRight();
			List<T> newCandidates = tree.findCandidates(factory.wrap(rec)).stream().map(ElementWrapper::getObject)
					.collect(Collectors.toList());
			candidates.addAll(newCandidates);
		}
		for (T candidate : candidates) {
			if (comp.areSimilar(candidate, rec)) {
				uf.union(rec, candidate);
			}
		}
		return uf.getComponent(rec);
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
