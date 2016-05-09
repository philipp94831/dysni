package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container<K extends Comparable<K>, V extends Element<K>> {

	private Collection<V> elements = new ArrayList<>();
	private Node<K, V> node;
	private Map<K, Double> similarities = new HashMap<>();
	private final KeyComparator<K> comp;
	
	
	public Container(KeyComparator<K> comp) {
		this.comp = comp;
	}
	
	public void remove(V element) {
		elements.remove(element);
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public void add(V element) {
		elements.add(element);
	}

	public boolean contains(V element) {
		return elements.contains(element);
	}

	public void setNode(Node<K, V> node) {
		this.node = node;
	}
	
	public List<V> getSimilarCandidates() {
		List<V> candidates = new ArrayList<>();
		candidates.addAll(getAll());
		Node<K, V> node = this.node;
		while(true) {
			node = node.getPrevious();
			if(node == null) {
				break;
			}
			if(getSimilarity(node) >= comp.getThreshold()) {
				candidates.addAll(node.getContainer().getAll());
			} else {
				break;
			}
		}
		node = this.node;
		while(true) {
			node = node.getNext();
			if(node == null) {
				break;
			}
			if(getSimilarity(node) >= comp.getThreshold()) {
				candidates.addAll(node.getContainer().getAll());
			} else {
				break;
			}
		}
		return candidates;
	}

	private double getSimilarity(Node<K, V> node2) {
		Double sim = similarities.get(node2.getKey());
		if(sim == null) {
			sim = comp.compare(node.getKey(), node2.getKey());
			addSimilarity(node2.getKey(), sim);
			node2.getContainer().addSimilarity(node.getKey(), sim);
		}
		return sim;
	}

	public void addSimilarity(K skv, Double sim) {
		similarities.put(skv, sim);
	}

	public Collection<V> getAll() {
		return elements;
	}
}
