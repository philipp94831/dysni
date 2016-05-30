package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.hpi.idd.dysni.avl.Node;

public class FixedWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID> implements WindowBuilder<RECORD, KEY, ID> {

	private final int size;

	public FixedWindowBuilder(int size) {
		this.size = size;
	}

	@Override
	public Collection<ID> buildWindow(RECORD rec, Node<KEY, ID> node) {
		if (node == null) {
			return Collections.emptyList();
		}
		Collection<ID> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		Node<KEY, ID> prevNode = node.getPrevious();
		for (int i = 0; i < size; i++) {
			if (prevNode == null) {
				break;
			}
			candidates.addAll(prevNode.getElements());
			prevNode = prevNode.getPrevious();
		}
		Node<KEY, ID> nextNode = node.getNext();
		for (int i = 0; i < size; i++) {
			if (nextNode == null) {
				break;
			}
			candidates.addAll(nextNode.getElements());
			nextNode = nextNode.getPrevious();
		}
		return candidates;
	}

}
