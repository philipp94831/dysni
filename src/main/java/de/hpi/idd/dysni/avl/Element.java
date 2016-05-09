package de.hpi.idd.dysni.avl;

public interface Element<K extends Comparable<K>> {

	public K getKey();
	
	public KeyComparator<K> getComparator();
}
