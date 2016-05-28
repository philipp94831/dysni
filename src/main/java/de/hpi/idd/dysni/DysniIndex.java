package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.util.SymmetricTable;

class DysniIndex<ELEMENT, KEY extends Comparable<KEY>, VALUE> {

	private final KeyHandler<ELEMENT, KEY> keyHandler;
	private final SymmetricTable<KEY, Double> similarities = new SymmetricTable<>();
	private final AVLTree<KEY, VALUE> tree = new AVLTree<>();

	public DysniIndex(final KeyHandler<ELEMENT, KEY> keyHandler) {
		this.keyHandler = keyHandler;
	}

	public Collection<VALUE> findCandidates(final ELEMENT rec) {
		final Node<KEY, VALUE> node = tree.find(keyHandler.computeKey(rec));
		if (node == null) {
			return Collections.emptyList();
		}
		final List<VALUE> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		for (Node<KEY, VALUE> prevNode = node.getPrevious(); prevNode != null; prevNode = prevNode.getPrevious()) {
			if (getSimilarity(prevNode, node) >= keyHandler.getComparator().getThreshold()) {
				candidates.addAll(prevNode.getElements());
			} else {
				break;
			}
		}
		for (Node<KEY, VALUE> nextNode = node.getNext(); nextNode != null; nextNode = nextNode.getNext()) {
			if (getSimilarity(nextNode, node) >= keyHandler.getComparator().getThreshold()) {
				candidates.addAll(nextNode.getElements());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(final Node<KEY, VALUE> node2, final Node<KEY, VALUE> node) {
		Double sim = similarities.get(node2.getKey(), node.getKey());
		if (sim == null) {
			sim = keyHandler.getComparator().compare(node.getKey(), node2.getKey());
			similarities.put(node2.getKey(), node.getKey(), sim);
		}
		return sim;
	}

	public void insert(final ELEMENT element, final VALUE value) {
		tree.insert(keyHandler.computeKey(element), value);
	}
}
