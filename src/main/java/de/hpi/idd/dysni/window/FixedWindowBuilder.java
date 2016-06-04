package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

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
		candidates.addAll(expand(node, Node::getPrevious));
		candidates.addAll(expand(node, Node::getNext));
		return candidates;
	}

	private Collection<ID> expand(Node<KEY, ID> node, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		node = f.apply(node);
		for (int i = 0; i < size && node != null; i++, node = f.apply(node)) {
			candidates.addAll(node.getElements());
		}
		return candidates;
	}

}
