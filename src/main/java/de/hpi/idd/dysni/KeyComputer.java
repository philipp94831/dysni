package de.hpi.idd.dysni;

import de.hpi.idd.dysni.simavl.KeyComparator;

interface KeyComputer<K, T> {

	T computeKey(K rec);

	KeyComparator<T> getComparator();

}
