package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.avl.Node;

class DysniIndex<T, K extends Comparable<K>, V> {

	private final KeyHandler<T, K> keyHandler;
	private final AVLTree<K, V> tree = new AVLTree<>();
	private final Table<K, K, Double> similarities = HashBasedTable.<K, K, Double> create();

	public DysniIndex(final KeyHandler<T, K> keyHandler) {
		this.keyHandler = keyHandler;
	}

	private void addSimilarity(final K key, final K key2, final Double sim) {
		if (key.compareTo(key2) < 0) {
			similarities.put(key, key2, sim);
		} else {
			similarities.put(key2, key, sim);
		}
	}

	public Collection<V> findCandidates(final T rec, final V value) {
		final Node<K, V> node = tree.find(keyHandler.computeKey(rec));
		if (node == null) {
			return Collections.emptyList();
		}
		final List<V> candidates = new ArrayList<>();
		candidates.addAll(node.getAll());
		for (Node<K, V> prevNode = node.getPrevious(); prevNode != null; prevNode = prevNode.getPrevious()) {
			if (getSimilarity(prevNode, node) >= keyHandler.getComparator().getThreshold()) {
				candidates.addAll(prevNode.getAll());
			} else {
				break;
			}
		}
		for (Node<K, V> nextNode = node.getNext(); nextNode != null; nextNode = nextNode.getNext()) {
			if (getSimilarity(nextNode, node) >= keyHandler.getComparator().getThreshold()) {
				candidates.addAll(nextNode.getAll());
			} else {
				break;
			}
		}
		candidates.remove(value);
		return candidates;
	}

	private Double getSimilarity(final K key, final K key2) {
		if (key.compareTo(key2) < 0) {
			return similarities.get(key, key2);
		} else {
			return similarities.get(key2, key);
		}
	}

	private double getSimilarity(final Node<K, V> node2, final Node<K, V> node) {
		Double sim = getSimilarity(node2.getKey(), node.getKey());
		if (sim == null) {
			sim = keyHandler.getComparator().compare(node.getKey(), node2.getKey());
			addSimilarity(node2.getKey(), node.getKey(), sim);
		}
		return sim;
	}

	public void insert(final T element, final V value) {
		tree.insert(keyHandler.computeKey(element), value);
	}

}
