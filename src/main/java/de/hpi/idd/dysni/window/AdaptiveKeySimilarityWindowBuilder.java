package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.sim.SimilarityAssessor;
import de.hpi.idd.dysni.util.SymmetricTable;

public class AdaptiveKeySimilarityWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID>
		implements WindowBuilder<RECORD, KEY, ID> {

	private final SimilarityAssessor<KEY> sim;
	private final SymmetricTable<KEY, Double> similarities = new SymmetricTable<>();

	public AdaptiveKeySimilarityWindowBuilder(SimilarityAssessor<KEY> sim) {
		this.sim = sim;
	}

	@Override
	public Collection<ID> buildWindow(RECORD rec, Node<KEY, ID> node) {
		if (node == null) {
			return Collections.emptyList();
		}
		Collection<ID> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		for (Node<KEY, ID> prevNode = node.getPrevious(); prevNode != null; prevNode = prevNode.getPrevious()) {
			if (sim.isSimilarity(getSimilarity(prevNode, node))) {
				candidates.addAll(prevNode.getElements());
			} else {
				break;
			}
		}
		for (Node<KEY, ID> nextNode = node.getNext(); nextNode != null; nextNode = nextNode.getNext()) {
			if (sim.isSimilarity(getSimilarity(nextNode, node))) {
				candidates.addAll(nextNode.getElements());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(Node<KEY, ID> node2, Node<KEY, ID> node) {
		Double simAssessor = similarities.get(node2.getKey(), node.getKey());
		if (simAssessor == null) {
			simAssessor = sim.calculateSimilarity(node.getKey(), node2.getKey());
			similarities.put(node2.getKey(), node.getKey(), simAssessor);
		}
		return simAssessor;
	}

}
