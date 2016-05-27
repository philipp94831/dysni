package de.hpi.idd.dysni.avl;

import java.util.Iterator;

class AVLTreeIterator<K extends Comparable<K>, V extends HasKey<K>, C extends Container<K, V, C, N>, N extends Node<K, V, C, N>>
		implements Iterator<N> {

	private N next;

	public AVLTreeIterator(final N smallest) {
		next = smallest;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public N next() {
		if (next == null) {
			throw new IllegalStateException("There is no next element");
		}
		final N ret = next;
		next = next.getNext();
		return ret;
	}
}
