package de.hpi.idd.dysni.store;

public interface RecordStore<K, V> extends AutoCloseable {

	V getRecord(K id) throws StoreException;

	void storeRecord(K id, V record) throws StoreException;
}
