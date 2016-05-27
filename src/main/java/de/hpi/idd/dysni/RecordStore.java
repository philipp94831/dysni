package de.hpi.idd.dysni;

import java.util.HashMap;
import java.util.Map;

public class RecordStore<T> {
	
	private final Map<String, T> store = new HashMap<>();
	
	public T getRecord(String id) {
		return store.get(id);
	}
	
	public void storeRecord(String id, T record) {
		store.put(id, record);
	}

}
