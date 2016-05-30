package de.hpi.idd.dysni.store;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryStore<K, V> implements RecordStore<K, V> {

	private final Map<K, V> store = new HashMap<>();

	@Override
	public Iterator<Entry<K, V>> all() {
		return store.entrySet().iterator();
	}

	@Override
	public void close() {
	}

	@Override
	public V getRecord(K id) {
		return store.get(id);
	}

	@Override
	public void storeRecord(K id, V record) {
		store.put(id, record);
	}
}
