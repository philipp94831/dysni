package de.hpi.idd.dysni.store;

public interface RecordStore<K, V> {

	V getRecord(K id);

	void storeRecord(K id, V record);
}
