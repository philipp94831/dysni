package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.sim.SimilarityClassifier;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;

/**
 * {@link WindowBuilder Window builder} that expands the window as long as the
 * ratio of duplicates found exceeds a certain threshold.
 *
 * @param <RECORD>
 *            type of elements associated with the index
 * @param <KEY>
 *            type of keys which the index is sorted by
 * @param <ID>
 *            type of ids representing the elements
 */
public class AdaptiveDuplicateWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID>
		implements WindowBuilder<RECORD, KEY, ID> {

	/** similarity classifier to determine duplicates */
	private final SimilarityClassifier<RECORD> classifier;
	/** store to retrieve records by their id */
	private final RecordStore<ID, RECORD> store;
	/** threshold to decide whether expansion should be continued or not */
	private final double threshold;

	/**
	 * Construct a new window builder
	 *
	 * @param threshold
	 *            threshold to decide whether expansion should be continued or
	 *            not
	 * @param classifier
	 *            similarity classifier to determine duplicates
	 * @param store
	 *            store to retrieve records by their id
	 */
	public AdaptiveDuplicateWindowBuilder(double threshold, SimilarityClassifier<RECORD> classifier,
			RecordStore<ID, RECORD> store) {
		this.threshold = threshold;
		this.classifier = classifier;
		this.store = store;
	}

	@Override
	public Collection<ID> buildWindow(RECORD rec, Node<KEY, ID> node) {
		if (node == null) {
			return Collections.emptyList();
		}
		Collection<ID> candidates = new ArrayList<>();
		candidates.addAll(node.getElements());
		candidates.addAll(expand(rec, node, Node::getPrevious));
		candidates.addAll(expand(rec, node, Node::getNext));
		return candidates;
	}

	/**
	 * Expand window as long as ratio of duplicates to all candidates exceeds
	 * the specified threshold.
	 *
	 * @param record
	 *            the record for which duplicates should be found
	 * @param node
	 *            the node where the expansion should be started
	 * @param f
	 *            function to expand the window to the next node
	 * @return ids contained in the built window
	 */
	private Collection<ID> expand(RECORD record, Node<KEY, ID> node, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		int added = 0;
		int matches = 0;
		for (node = f.apply(node); node != null && isAboveThreshold(added, matches); node = f.apply(node)) {
			for (ID id : node.getElements()) {
				try {
					if (classifier.areSimilar(record, store.getRecord(id))) {
						matches++;
					}
					candidates.add(id);
					added++;
				} catch (StoreException e) {
					throw new RuntimeException("Error accessing storage", e);
				}
			}
		}
		return candidates;
	}

	private boolean isAboveThreshold(int added, int matches) {
		return added == 0 || (double) matches / added >= threshold;
	}

}
