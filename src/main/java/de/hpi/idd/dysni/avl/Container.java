package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Container<K extends Comparable<K>, V extends HasKey<K>, C extends Container<K, V, C, N>, N extends Node<K, V, C, N>> {

	private final Collection<V> elements = new ArrayList<>();
	protected N node;

	public void add(final V element) {
		elements.add(element);
	}

	public boolean contains(final V element) {
		return elements.contains(element);
	}

	protected Collection<V> getAll() {
		return elements;
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public void remove(final V element) {
		elements.remove(element);
	}

	public void setNode(final N node) {
		this.node = node;
	}
}
