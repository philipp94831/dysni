package de.hpi.idd.dysni.store;

import java.util.HashMap;
import java.util.Map;

public class MemoryStore<T> implements RecordStore<T> {

	private final Map<String, T> store = new HashMap<>();

	@Override
	public T getRecord(String id) {
		return store.get(id);
	}

	@Override
	public void storeRecord(String id, T record) {
		store.put(id, record);
	}

}
