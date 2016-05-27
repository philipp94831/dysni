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

	private UnionFind<String> uf = new UnionFind<>();
	private final SimilarityMeasure comp;
	private final RecordStore<T> store;
	private final List<Tuple<KeyWrapperFactory<T, String>, AVLTree<String, KeyWrapper<String>>>> trees = new ArrayList<>();

	public DynamicSortedNeighborhoodIndexer(RecordStore<T> store, SimilarityMeasure comp, List<KeyWrapperFactory<T, String>> factories) {
		this.store = store;
		this.comp = comp;
		for (KeyWrapperFactory<T, String> factory : factories) {
			trees.add(new Tuple<>(factory, new AVLTree<>()));
		}
	}

	public Collection<String> add(T rec) {
		String recId = rec.getId();
		store.storeRecord(recId, rec);
		Set<String> candidates = new HashSet<>();
		for (Tuple<KeyWrapperFactory<T, String>, AVLTree<String, KeyWrapper<String>>> tuple : trees) {
			KeyWrapperFactory<T, String> factory = tuple.getLeft();
			AVLTree<String, KeyWrapper<String>> tree = tuple.getRight();
			List<String> newCandidates = tree.insert(factory.wrap(rec)).stream().map(KeyWrapper::getObject)
					.collect(Collectors.toList());
			candidates.addAll(newCandidates);
		}
		for (String candidate : candidates) {
			if (comp.getThreshold() <= comp.calculateSimilarity(recId, candidate, new HashMap<>())) {
				uf.union(recId, candidate);
			}
		}
		return uf.getComponent(recId);
	}
	
	public Collection<String> findDuplicates(T rec) {
		String recId = rec.getId();
		Set<String> candidates = new HashSet<>();
		for (Tuple<KeyWrapperFactory<T, String>, AVLTree<String, KeyWrapper<String>>> tuple : trees) {
			KeyWrapperFactory<T, String> factory = tuple.getLeft();
			AVLTree<String, KeyWrapper<String>> tree = tuple.getRight();
			List<String> newCandidates = tree.findCandidates(factory.wrap(rec)).stream().map(KeyWrapper::getObject)
					.collect(Collectors.toList());
			candidates.addAll(newCandidates);
		}
		for (String candidate : candidates) {
			if (comp.getThreshold() <= comp.calculateSimilarity(recId, candidate, new HashMap<>())) {
				uf.union(recId, candidate);
			}
		}
		return uf.getComponent(recId);
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
