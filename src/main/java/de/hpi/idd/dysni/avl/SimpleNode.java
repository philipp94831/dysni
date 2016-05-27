package de.hpi.idd.dysni.avl;

public class SimpleNode<K extends Comparable<K>, V> extends Node<K, V, SimpleContainer<V>, SimpleNode<K, V>> {

	SimpleNode(final K key, final V element) {
		super(key, element, new SimpleContainer<>());
	}

	@Override
	protected SimpleNode<K, V> createNode(final K key, final V element) {
		return new SimpleNode<>(key, element);
	}

}
