package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.sim.SimilarityClassifier;
import de.hpi.idd.dysni.util.SymmetricTable;

public class AdaptiveKeySimilarityWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID>
		implements WindowBuilder<RECORD, KEY, ID> {

	private final SimilarityClassifier<KEY> classifier;
	private final SymmetricTable<KEY, Double> similarities = new SymmetricTable<>();

	public AdaptiveKeySimilarityWindowBuilder(SimilarityClassifier<KEY> classifier) {
		this.classifier = classifier;
	}

	@Override
	public Collection<ID> buildWindow(RECORD rec, Node<KEY, ID> node) {
		if (node == null) {
			return Collections.emptyList();
		}
		Collection<ID> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		candidates.addAll(expand(node, Node::getPrevious));
		candidates.addAll(expand(node, Node::getNext));
		return candidates;
	}

	private Collection<ID> expand(Node<KEY, ID> initial, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		for (Node<KEY, ID> node = f.apply(initial); node != null; node = f.apply(node)) {
			if (classifier.isSimilarity(getSimilarity(node, initial))) {
				candidates.addAll(node.getElements());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(Node<KEY, ID> node1, Node<KEY, ID> node2) {
		Double sim = similarities.get(node1.getKey(), node2.getKey());
		if (sim == null) {
			sim = classifier.calculateCheckedSimilarity(node1.getKey(), node2.getKey());
			similarities.put(node1.getKey(), node2.getKey(), sim);
		}
		return sim;
	}

}
