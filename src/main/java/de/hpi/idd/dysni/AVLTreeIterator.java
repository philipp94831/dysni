package de.hpi.idd.dysni;

import java.util.Iterator;

public class AVLTreeIterator<U extends Comparable<U>, T extends Element<U>> implements Iterator<AVLTree<U, T>.Node> {

	private AVLTree<U, T>.Node next;

	public AVLTreeIterator(final AVLTree<U, T>.Node smallest) {
		next = smallest;
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public AVLTree<U, T>.Node next() {
		final AVLTree<U, T>.Node ret = next;
		next = next.getNext();
		return ret;
	}
}
