package de.hpi.idd.dysni.util;

import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

/**
 * Two-dimensional symmetric value store. Values are stored using two keys but
 * are retrieved using the keys in no matter which order
 *
 * @param <K>
 *            type of the keys
 * @param <V>
 *            type of the values
 */
public class SymmetricTable<K, V> {

	private final Table<K, K, V> table = HashBasedTable.create();

	/**
	 * Get all stored key-key-value triplets
	 *
	 * @return set of key-key-value triplets
	 */
	public Set<Cell<K, K, V>> cellSet() {
		return table.cellSet();
	}

	/**
	 * Check whether a value is associated with the two keys
	 *
	 * @param key1
	 *            first key
	 * @param key2
	 *            second key
	 * @return true if the table contains a value associated with the two keys
	 */
	public boolean contains(K key1, K key2) {
		return table.contains(key1, key2) || table.contains(key2, key1);
	}

	/**
	 * Retrieve the value associated with the two keys
	 *
	 * @param key1
	 *            first key
	 * @param key2
	 *            second key
	 * @return value associated with two keys, null if no value is stored with
	 *         those keys
	 */
	public V get(K key1, K key2) {
		return table.contains(key1, key2) ? table.get(key1, key2) : table.get(key2, key1);
	}

	/**
	 * Store a value with the specified keys
	 *
	 * @param key1
	 *            first key
	 * @param key2
	 *            second key
	 * @param value
	 *            value to be stored
	 */
	public void put(K key1, K key2, V value) {
		if (table.contains(key1, key2)) {
			table.put(key1, key2, value);
		} else {
			table.put(key2, key1, value);
		}
	}
}
