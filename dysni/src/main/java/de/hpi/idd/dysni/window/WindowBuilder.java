package de.hpi.idd.dysni.window;

import java.util.Collection;

import de.hpi.idd.dysni.avl.Node;

/**
 * Interface for building windows when retrieving candidates in a DySNIndex
 *
 * @param <RECORD>
 *            type of elements associated with the index
 * @param <KEY>
 *            type of keys which the index is sorted by
 * @param <ID>
 *            type of ids representing the elements
 */
public interface WindowBuilder<RECORD, KEY extends Comparable<KEY>, ID> {

	/**
	 * Build a window starting at the specified node for the specified record.
	 *
	 * @param record
	 *            the record for which possible duplicates should be found
	 * @param node
	 *            the node containing the record. The window is built starting
	 *            from this node.
	 * @return ids contained in the built window
	 */
	Collection<ID> buildWindow(RECORD record, Node<KEY, ID> node);

}
