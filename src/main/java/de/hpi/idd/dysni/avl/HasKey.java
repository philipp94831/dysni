package de.hpi.idd.dysni.avl;

public interface HasKey<K extends Comparable<K>> {

	public KeyComparator<K> getComparator();

	public K getKey();
}
