package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container<K extends Comparable<K>, V extends HasKey<K>> {

	private final KeyComparator<K> comp;
	private final Collection<V> elements = new ArrayList<>();
	private Node<K, V> node;
	private final Map<K, Double> similarities = new HashMap<>();

	public Container(KeyComparator<K> comp) {
		this.comp = comp;
	}

	public void add(V element) {
		elements.add(element);
	}

	void addSimilarity(K skv, Double sim) {
		similarities.put(skv, sim);
	}

	public boolean contains(V element) {
		return elements.contains(element);
	}

	Collection<V> getAll() {
		return elements;
	}

	public List<V> getSimilarCandidates() {
		List<V> candidates = new ArrayList<>();
		candidates.addAll(getAll());
		for (Node<K, V> node = this.node.getPrevious(); node != null; node = node.getPrevious()) {
			if (getSimilarity(node) >= comp.getThreshold()) {
				candidates.addAll(node.getContainer().getAll());
			} else {
				break;
			}
		}
		for (Node<K, V> node = this.node.getNext(); node != null; node = node.getNext()) {
			if (getSimilarity(node) >= comp.getThreshold()) {
				candidates.addAll(node.getContainer().getAll());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(Node<K, V> node2) {
		Double sim = similarities.get(node2.getKey());
		if (sim == null) {
			sim = comp.compare(node.getKey(), node2.getKey());
			addSimilarity(node2.getKey(), sim);
			node2.getContainer().addSimilarity(node.getKey(), sim);
		}
		return sim;
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public void remove(V element) {
		elements.remove(element);
	}

	public void setNode(Node<K, V> node) {
		this.node = node;
	}
}
