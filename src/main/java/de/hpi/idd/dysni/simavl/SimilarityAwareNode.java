package de.hpi.idd.dysni.simavl;

import de.hpi.idd.dysni.avl.Node;

public class SimilarityAwareNode<K extends Comparable<K>, V>
		extends Node<K, V, SimilarityAwareContainer<K, V>, SimilarityAwareNode<K, V>> {

	private final KeyComparator<K> comp;

	SimilarityAwareNode(final K key, final V element, final KeyComparator<K> comp) {
		super(key, element, new SimilarityAwareContainer<>(comp));
		this.comp = comp;
	}

	@Override
	protected SimilarityAwareNode<K, V> createNode(final K key, final V element) {
		return new SimilarityAwareNode<>(key, element, comp);
	}

	@Override
	protected void setContainer(final SimilarityAwareContainer<K, V> container) {
		super.setContainer(container);
		container.setNode(this);
	}

}
