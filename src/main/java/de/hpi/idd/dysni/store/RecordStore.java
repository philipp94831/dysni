package de.hpi.idd.dysni.store;

import java.util.Iterator;
import java.util.Map.Entry;

public interface RecordStore<K, V> extends AutoCloseable {

	Iterator<Entry<K, V>> all();

	V getRecord(K id) throws StoreException;

	void storeRecord(K id, V record) throws StoreException;
}
