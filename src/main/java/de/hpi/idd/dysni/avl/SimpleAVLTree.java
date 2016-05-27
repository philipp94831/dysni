package de.hpi.idd.dysni.avl;

public class SimpleAVLTree<K extends Comparable<K>, V extends HasKey<K>>
		extends AVLTree<K, V, SimpleContainer<K, V>, SimpleNode<K, V>> {

	@Override
	protected SimpleNode<K, V> createNode(final V element) {
		return new SimpleNode<>(element);
	}

}
