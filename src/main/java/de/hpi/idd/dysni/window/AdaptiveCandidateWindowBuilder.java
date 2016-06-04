package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import de.hpi.idd.dysni.avl.Node;

public class AdaptiveCandidateWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID>
		implements WindowBuilder<RECORD, KEY, ID> {

	private final int maximum;

	public AdaptiveCandidateWindowBuilder(int maximum) {
		this.maximum = maximum;
	}

	@Override
	public Collection<ID> buildWindow(RECORD rec, Node<KEY, ID> node) {
		if (node == null) {
			return Collections.emptyList();
		}
		Collection<ID> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		int remaining = (int) Math.ceil(maximum - candidates.size() / 2.0);
		candidates.addAll(expand(node, remaining, Node::getPrevious));
		candidates.addAll(expand(node, remaining, Node::getNext));
		return candidates;
	}

	private Collection<ID> expand(Node<KEY, ID> node, int remaining, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		for (node = f.apply(node); node != null && remaining - candidates.size() > 0; node = f.apply(node)) {
			candidates.addAll(node.getElements());
		}
		return candidates;
	}

}
