package de.hpi.idd.dysni.avl;

public class SimpleAVLTree<K extends Comparable<K>, V> extends AVLTree<K, V, SimpleContainer<V>, SimpleNode<K, V>> {

	@Override
	protected SimpleNode<K, V> createNode(final K key, final V element) {
		return new SimpleNode<>(key, element);
	}

}
