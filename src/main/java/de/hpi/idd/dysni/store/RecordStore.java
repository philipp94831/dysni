package de.hpi.idd.dysni.store;

public interface RecordStore<T> {

	T getRecord(String id);

	void storeRecord(String id, T record);

}
