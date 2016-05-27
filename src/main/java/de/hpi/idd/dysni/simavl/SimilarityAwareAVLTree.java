package de.hpi.idd.dysni.simavl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.hpi.idd.dysni.avl.AVLTree;

public class SimilarityAwareAVLTree<K extends Comparable<K>, V>
		extends AVLTree<K, V, SimilarityAwareContainer<K, V>, SimilarityAwareNode<K, V>> {

	private final KeyComparator<K> comp;

	public SimilarityAwareAVLTree(final KeyComparator<K> comp) {
		this.comp = comp;
	}

	@Override
	protected SimilarityAwareNode<K, V> createNode(final K key, final V element) {
		return new SimilarityAwareNode<>(key, element, comp);
	}

	public Collection<V> findCandidates(final K key, final V elem) {
		final SimilarityAwareNode<K, V> node = find(key);
		if (node == null) {
			return Collections.emptyList();
		}
		final List<V> candidates = node.getContainer().getSimilarCandidates();
		candidates.remove(elem);
		return candidates;
	}

}
