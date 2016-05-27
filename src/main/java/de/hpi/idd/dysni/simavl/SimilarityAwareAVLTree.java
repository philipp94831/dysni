package de.hpi.idd.dysni.simavl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.avl.HasKey;

public class SimilarityAwareAVLTree<K extends Comparable<K>, V extends HasKey<K>>
		extends AVLTree<K, V, SimilarityAwareContainer<K, V>, SimilarityAwareNode<K, V>> {

	private final KeyComparator<K> comp;

	public SimilarityAwareAVLTree(final KeyComparator<K> comp) {
		this.comp = comp;
	}

	@Override
	protected SimilarityAwareNode<K, V> createNode(final V element) {
		return new SimilarityAwareNode<>(element, comp);
	}

	public Collection<V> findCandidates(final V elem) {
		final SimilarityAwareNode<K, V> node = find(elem);
		if (node == null) {
			return Collections.emptyList();
		}
		final List<V> candidates = node.getContainer().getSimilarCandidates();
		candidates.remove(elem);
		return candidates;
	}

}
