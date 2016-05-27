package de.hpi.idd.dysni.store;

public interface RecordStore<T> {

	public T getRecord(String id);

	public void storeRecord(String id, T record);

}
