package de.hpi.idd.dysni;

interface KeyHandler<K, T> {

	T computeKey(K rec);

	KeyComparator<T> getComparator();

}
