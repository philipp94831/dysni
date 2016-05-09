package de.hpi.idd.dysni.avl;

public interface Element<K extends Comparable<K>> {

	public KeyComparator<K> getComparator();

	public K getKey();
}
