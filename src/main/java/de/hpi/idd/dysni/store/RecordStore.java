package de.hpi.idd.dysni.store;

import java.util.Iterator;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Generic record store to store and retrieve objects based on their id
 *
 * @param <K>
 *            type of the ids
 * @param <V>
 *            type of the objects to be stored
 */
public interface RecordStore<K, V> extends AutoCloseable {

	/**
	 * Get access to all objects stored
	 * 
	 * @return iterator to iterate over all objects stored
	 */
	Iterator<Pair<K, V>> all();

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
