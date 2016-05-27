package de.hpi.idd.dysni.simavl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hpi.idd.dysni.avl.Container;

class SimilarityAwareContainer<K extends Comparable<K>, V> extends Container<V> {

	private final Map<K, Double> similarities = new HashMap<>();
	private final KeyComparator<K> comp;
	private SimilarityAwareNode<K, V> node;

	public SimilarityAwareContainer(final KeyComparator<K> comp) {
		super();
		this.comp = comp;
	}

	void addSimilarity(final K skv, final Double sim) {
		similarities.put(skv, sim);
	}

	public List<V> getSimilarCandidates() {
		final List<V> candidates = new ArrayList<>();
		candidates.addAll(getAll());
		for (SimilarityAwareNode<K, V> node = this.node.getPrevious(); node != null; node = node.getPrevious()) {
			if (getSimilarity(node) >= comp.getThreshold()) {
				candidates.addAll(node.getContainer().getAll());
			} else {
				break;
			}
		}
		for (SimilarityAwareNode<K, V> node = this.node.getNext(); node != null; node = node.getNext()) {
			if (getSimilarity(node) >= comp.getThreshold()) {
				candidates.addAll(node.getContainer().getAll());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(final SimilarityAwareNode<K, V> node2) {
		Double sim = similarities.get(node2.getKey());
		if (sim == null) {
			sim = comp.compare(node.getKey(), node2.getKey());
			addSimilarity(node2.getKey(), sim);
			node2.getContainer().addSimilarity(node.getKey(), sim);
		}
		return sim;
	}

	public void setNode(final SimilarityAwareNode<K, V> node) {
		this.node = node;
	}

}
