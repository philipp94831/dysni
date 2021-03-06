package de.hpi.idd.store;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryStore<K, V> implements RecordStore<K, V> {

	private final Map<K, V> store = new HashMap<>();

	@Override
	public void close() {
	}

	@Override
	public V getRecord(K id) {
		return store.get(id);
	}

	@Override
	public Iterator<Entry<K, V>> iterator() {
		return store.entrySet().iterator();
	}

	@Override
	public void storeRecord(K id, V record) {
		if (id == null) {
			throw new NullPointerException("Cannot store record with no id");
		}
		store.put(id, record);
	}
}
