package de.hpi.idd.dysni.simavl;

public interface KeyComparator<T> {

	double compare(T e1, T e2);

	double getThreshold();
}
