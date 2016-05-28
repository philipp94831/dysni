package de.hpi.idd.dysni.key;

public interface KeyHandler<K, T> {

	T computeKey(K rec);

	KeyComparator<T> getComparator();
}
