package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.util.SymmetricTable;

class DysniIndex<RECORD, KEY extends Comparable<KEY>, ID> {

	private final KeyHandler<RECORD, KEY> keyHandler;
	private final SymmetricTable<KEY, Double> similarities = new SymmetricTable<>();
	private final AVLTree<KEY, ID> tree = new AVLTree<>();

	public DysniIndex(final KeyHandler<RECORD, KEY> keyHandler) {
		this.keyHandler = keyHandler;
	}

	public Collection<ID> findCandidates(final RECORD rec) {
		final Node<KEY, ID> node = tree.find(keyHandler.computeKey(rec));
		if (node == null) {
			return Collections.emptyList();
		}
		final Collection<ID> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		for (Node<KEY, ID> prevNode = node.getPrevious(); prevNode != null; prevNode = prevNode.getPrevious()) {
			if (keyHandler.getSimilarityMeasure().isSimilarity(getSimilarity(prevNode, node))) {
				candidates.addAll(prevNode.getElements());
			} else {
				break;
			}
		}
		for (Node<KEY, ID> nextNode = node.getNext(); nextNode != null; nextNode = nextNode.getNext()) {
			if (keyHandler.getSimilarityMeasure().isSimilarity(getSimilarity(nextNode, node))) {
				candidates.addAll(nextNode.getElements());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(final Node<KEY, ID> node2, final Node<KEY, ID> node) {
		Double sim = similarities.get(node2.getKey(), node.getKey());
		if (sim == null) {
			sim = keyHandler.getSimilarityMeasure().calculateSimilarity(node.getKey(), node2.getKey());
			similarities.put(node2.getKey(), node.getKey(), sim);
		}
		return sim;
	}

	public void insert(final RECORD element, final ID value) {
		tree.insert(keyHandler.computeKey(element), value);
	}
}
