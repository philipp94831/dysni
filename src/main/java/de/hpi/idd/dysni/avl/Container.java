package de.hpi.idd.dysni.avl;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Container<V> {

	private final Collection<V> elements = new ArrayList<>();

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
}
