package de.hpi.idd.dysni.key;

public interface KeyComparator<T> {

	double compare(T e1, T e2);

	double getThreshold();
}
