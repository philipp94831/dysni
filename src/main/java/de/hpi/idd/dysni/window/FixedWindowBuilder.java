package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import de.hpi.idd.dysni.avl.Node;

/**
 * {@link WindowBuilder Window builder} that expands the window with a fixed
 * number of nodes in both directions.
 *
 * @param <RECORD>
 *            type of elements associated with the index
 * @param <KEY>
 *            type of keys which the index is sorted by
 * @param <ID>
 *            type of ids representing the elements
 */
public class FixedWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID> implements WindowBuilder<RECORD, KEY, ID> {

	/** size of window in each direction */
	private final int size;

	/**
	 * Construct a new window builder
	 *
	 * @param size
	 *            size of window in each direction
	 */
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

	/**
	 * Expand window with a fixed number of nodes in each direction
	 *
	 * @param node
	 *            node where expansion starts
	 * @param f
	 *            function to retrieve the next node
	 * @return ids contained in the built window
	 */
	private Collection<ID> expand(Node<KEY, ID> node, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		node = f.apply(node);
		for (int i = 0; i < size && node != null; i++, node = f.apply(node)) {
			candidates.addAll(node.getElements());
		}
		return candidates;
	}

}
