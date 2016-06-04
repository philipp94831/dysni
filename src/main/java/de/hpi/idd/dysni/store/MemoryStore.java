package de.hpi.idd.dysni.store;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

public class MemoryStore<K, V> implements RecordStore<K, V> {

	private final Map<K, V> store = new HashMap<>();

	@Override
	public Iterator<Pair<K, V>> all() {
		return store.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())).collect(Collectors.toList())
				.iterator();
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
