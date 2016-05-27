package de.hpi.idd.dysni.avl;

import java.util.Iterator;

class AVLTreeIterator<K extends Comparable<K>, V> implements Iterator<Node<K, V>> {

	private Node<K, V> next;

	public AVLTreeIterator(final Node<K, V> smallest) {
		next = smallest;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public Node<K, V> next() {
		if (next == null) {
			throw new IllegalStateException("There is no next element");
		}
		final Node<K, V> ret = next;
		next = next.getNext();
		return ret;
	}
}
