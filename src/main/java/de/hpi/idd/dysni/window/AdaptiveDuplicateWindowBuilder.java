package de.hpi.idd.dysni.window;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import de.hpi.idd.dysni.avl.Node;
import de.hpi.idd.dysni.sim.SimilarityAssessor;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;

public class AdaptiveDuplicateWindowBuilder<RECORD, KEY extends Comparable<KEY>, ID>
		implements WindowBuilder<RECORD, KEY, ID> {

	private final SimilarityAssessor<RECORD> simAssessor;
	private final RecordStore<ID, RECORD> store;
	private final double threshold;

	public AdaptiveDuplicateWindowBuilder(double threshold, SimilarityAssessor<RECORD> simAssessor,
			RecordStore<ID, RECORD> store) {
		this.threshold = threshold;
		this.simAssessor = simAssessor;
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

	private Collection<ID> expand(RECORD rec, Node<KEY, ID> node, Function<Node<KEY, ID>, Node<KEY, ID>> f) {
		Collection<ID> candidates = new ArrayList<>();
		int added = 0;
		int matches = 0;
		for (node = f.apply(node); node != null && isAboveThreshold(added, matches); node = f.apply(node)) {
			for (ID id : node.getElements()) {
				try {
					if (simAssessor.areSimilar(rec, store.getRecord(id))) {
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
