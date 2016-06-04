package de.hpi.idd.dysni.store;

import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;

public interface RecordStore<K, V> extends AutoCloseable {

	Iterator<Pair<K, V>> all();

	V getRecord(K id) throws StoreException;

	void storeRecord(K id, V record) throws StoreException;
}
