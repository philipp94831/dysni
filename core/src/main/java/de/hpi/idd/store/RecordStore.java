package de.hpi.idd.store;

import java.io.Closeable;
import java.util.Map.Entry;

/**
 * Generic record store to store and retrieve objects based on their id
 *
 * @param <K>
 *            type of the ids
 * @param <V>
 *            type of the objects to be stored
 */
public interface RecordStore<K, V> extends Closeable, Iterable<Entry<K, V>> {

	@Override
	void close() throws StoreException;

	/**
	 * Retrieve record based on its id
	 *
	 * @param id
	 *            id of the record to be retrieved
	 * @return record associated with the id
	 * @throws StoreException
	 */
	V getRecord(K id) throws StoreException;

	/**
	 * Store a record based on its id
	 *
	 * @param id
	 *            id of the record to be stored
	 * @param record
	 *            record to be stored
	 * @throws StoreException
	 */
	void storeRecord(K id, V record) throws StoreException;
}
