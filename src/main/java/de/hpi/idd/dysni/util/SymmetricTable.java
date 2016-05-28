package de.hpi.idd.dysni.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class SymmetricTable<K, V> {

	private final Table<K, K, V> table = HashBasedTable.<K, K, V> create();

	public V get(final K key1, final K key2) {
		if (table.contains(key1, key2)) {
			return table.get(key1, key2);
		} else {
			return table.get(key2, key1);
		}
	}

	public void put(final K key1, final K key2, final V value) {
		if (table.contains(key1, key2)) {
			table.put(key1, key2, value);
		} else {
			table.put(key2, key1, value);
		}
	}
}
