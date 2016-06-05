package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import de.hpi.idd.dysni.avl.Node;

/**
 * {@link WindowBuilder Window builder} based on the idea that a fixed number of
 * candidates can be processed in a certain time. Thus this window expands in
 * both directions until a certain number of candidates is retrieved.
 *
 * @param <RECORD>
 *            type of elements associated with the index
 * @param <KEY>
 *            type of keys which the index is sorted by
 * @param <ID>
 *            type of ids representing the elements
 */
public class AdaptiveCandidateWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID>
		implements WindowBuilder<RECORD, KEY, ID> {

	/** maximum number of candidates to be retrieved */
	private final int maximum;

	/**
	 * Construct a new window builder
	 *
	 * @param maximum
	 *            maximum number of candidates to be retrieved when building a
	 *            window
	 */
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

	/**
	 * Expand window with a fixed number of candidates.
	 *
	 * @param node
	 *            the node where the expansion should be started
	 * @param remaining
	 *            number of candidates that should maximally be included in the
	 *            window
	 * @param f
	 *            function to expand the window to the next node
	 * @return ids contained in the built window
	 */
	private Collection<ID> expand(Node<KEY, ID> node, int remaining, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		for (node = f.apply(node); node != null && remaining - candidates.size() > 0; node = f.apply(node)) {
			candidates.addAll(node.getElements());
		}
		return candidates;
	}

}
