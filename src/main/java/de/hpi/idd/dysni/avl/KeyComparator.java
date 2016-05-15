package de.hpi.idd.dysni.avl;

public interface KeyComparator<T> {

	double compare(T e1, T e2);

	double getThreshold();
}
