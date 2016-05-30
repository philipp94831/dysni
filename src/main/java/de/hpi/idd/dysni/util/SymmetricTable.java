package de.hpi.idd.dysni.util;

import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

public class SymmetricTable<K, V> {

	private final Table<K, K, V> table = HashBasedTable.create();

	public Set<Cell<K, K, V>> cellSet() {
		return table.cellSet();
	}

	public boolean contains(K key1, K key2) {
		return table.contains(key1, key2) || table.contains(key2, key1);
	}

	public V get(K key1, K key2) {
		return table.contains(key1, key2) ? table.get(key1, key2) : table.get(key2, key1);
	}

	public void put(K key1, K key2, V value) {
		if (table.contains(key1, key2)) {
			table.put(key1, key2, value);
		} else {
			table.put(key2, key1, value);
		}
	}
}
