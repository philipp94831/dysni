package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.util.SymmetricTable;

class DysniIndex<T, K extends Comparable<K>, V> {

	private final KeyHandler<T, K> keyHandler;
	private final SymmetricTable<K, Double> similarities = new SymmetricTable<>();
	private final AVLTree<K, V> tree = new AVLTree<>();

	public DysniIndex(final KeyHandler<T, K> keyHandler) {
		this.keyHandler = keyHandler;
	}

	public Collection<V> findCandidates(final T rec) {
		final Node<K, V> node = tree.find(keyHandler.computeKey(rec));
		if (node == null) {
			return Collections.emptyList();
		}
		final List<V> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		for (Node<K, V> prevNode = node.getPrevious(); prevNode != null; prevNode = prevNode.getPrevious()) {
			if (getSimilarity(prevNode, node) >= keyHandler.getComparator().getThreshold()) {
				candidates.addAll(prevNode.getElements());
			} else {
				break;
			}
		}
		for (Node<K, V> nextNode = node.getNext(); nextNode != null; nextNode = nextNode.getNext()) {
			if (getSimilarity(nextNode, node) >= keyHandler.getComparator().getThreshold()) {
				candidates.addAll(nextNode.getElements());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(final Node<K, V> node2, final Node<K, V> node) {
		Double sim = similarities.get(node2.getKey(), node.getKey());
		if (sim == null) {
			sim = keyHandler.getComparator().compare(node.getKey(), node2.getKey());
			similarities.put(node2.getKey(), node.getKey(), sim);
		}
		return sim;
	}

	public void insert(final T element, final V value) {
		tree.insert(keyHandler.computeKey(element), value);
	}
}
