package de.hpi.idd.dysni;

import de.hpi.idd.dysni.avl.KeyComparator;

interface KeyWrapperFactory<K, T> {

	KeyWrapper<T> wrap(K rec);

	KeyComparator<T> getComparator();

}
