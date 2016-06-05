package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.sim.SimilarityClassifier;
import de.hpi.idd.dysni.util.SymmetricTable;

/**
 * {@link WindowBuilder Window builder} that expands the window as long as the
 * similarity of the keys to initial key exceeds a certain threshold.
 *
 * @param <RECORD>
 *            type of elements associated with the index
 * @param <KEY>
 *            type of keys which the index is sorted by
 * @param <ID>
 *            type of ids representing the elements
 */
public class AdaptiveKeySimilarityWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID>
		implements WindowBuilder<RECORD, KEY, ID> {

	/** similarity classifier to determine similar keys */
	private final SimilarityClassifier<KEY> classifier;
	/** symmetric store to cache similarity of keys as they won't change */
	private final SymmetricTable<KEY, Double> similarities = new SymmetricTable<>();

	/**
	 * Construct a new window builder
	 *
	 * @param classifier
	 *            classifier to measure similarity of keys
	 */
	public AdaptiveKeySimilarityWindowBuilder(SimilarityClassifier<KEY> classifier) {
		this.classifier = classifier;
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
	 * Expand window as long as the similarity of the initial node's key to the
	 * current node's key exceeds the specified threshold
	 *
	 * @param initial
	 *            initial node where the expansion starts
	 * @param f
	 *            function to retrieve the next node
	 * @return ids contained in the built window
	 */
	private Collection<ID> expand(Node<KEY, ID> initial, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		for (Node<KEY, ID> node = f.apply(initial); node != null; node = f.apply(node)) {
			if (classifier.isSimilarity(getSimilarity(node.getKey(), initial.getKey()))) {
				candidates.addAll(node.getElements());
			} else {
				break;
			}
		}
		return candidates;
	}

	/**
	 * Lazy computation of similarities. Similarities are looked up in cache and
	 * only computed if not computed before.
	 *
	 * @param key1
	 *            first key
	 * @param key2
	 *            second key
	 * @return similarity of the keys
	 */
	private double getSimilarity(KEY key1, KEY key2) {
		Double sim = similarities.get(key1, key2);
		if (sim == null) {
			sim = classifier.calculateCheckedSimilarity(key1, key2);
			similarities.put(key1, key2, sim);
		}
		return sim;
	}

}
