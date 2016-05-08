package de.hpi.idd.dysni.avl;

import java.util.Iterator;

public class AVLTreeIterator<U extends Comparable<U>, T extends Element<U>> implements Iterator<Node<U, T>> {

	private Node<U, T> next;

	public AVLTreeIterator(final Node<U, T> smallest) {
		next = smallest;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public Node<U, T> next() {
		if(next== null) {
			throw new IllegalStateException("There is no next element");
		}
		final Node<U, T> ret = next;
		next = next.getNext();
		return ret;
	}
}
