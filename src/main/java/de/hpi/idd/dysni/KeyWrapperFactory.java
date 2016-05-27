package de.hpi.idd.dysni;

import de.hpi.idd.dysni.simavl.KeyComparator;

interface KeyWrapperFactory<K, T> {

	KeyComparator<T> getComparator();

	KeyWrapper<T> wrap(K rec);

}
