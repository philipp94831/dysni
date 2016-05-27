package de.hpi.idd.dysni.simavl;

import de.hpi.idd.dysni.avl.HasKey;
import de.hpi.idd.dysni.avl.Node;

public class SimilarityAwareNode<K extends Comparable<K>, V extends HasKey<K>>
		extends Node<K, V, SimilarityAwareContainer<K, V>, SimilarityAwareNode<K, V>> {

	private final KeyComparator<K> comp;

	SimilarityAwareNode(final V element, final KeyComparator<K> comp) {
		super(element, new SimilarityAwareContainer<>(comp));
		this.comp = comp;
	}

	@Override
	protected SimilarityAwareNode<K, V> createNode(final V element) {
		return new SimilarityAwareNode<>(element, comp);
	}

}
