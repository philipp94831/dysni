package de.hpi.idd.dysni.avl;

public class SimpleNode<K extends Comparable<K>, V extends HasKey<K>>
		extends Node<K, V, SimpleContainer<K, V>, SimpleNode<K, V>> {

	SimpleNode(final V element) {
		super(element, new SimpleContainer<>());
	}

	@Override
	protected SimpleNode<K, V> createNode(final V element) {
		return new SimpleNode<>(element);
	}

}
