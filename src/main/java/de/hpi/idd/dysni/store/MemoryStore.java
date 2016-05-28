package de.hpi.idd.dysni.store;

import java.util.HashMap;
import java.util.Map;

public class MemoryStore<K, V> implements RecordStore<K, V> {

	private final Map<K, V> store = new HashMap<>();

	@Override
	public V getRecord(final K id) {
		return store.get(id);
	}

	@Override
	public void storeRecord(final K id, final V record) {
		store.put(id, record);
	}
}
