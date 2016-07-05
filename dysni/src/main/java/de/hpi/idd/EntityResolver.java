package de.hpi.idd;

import java.io.Closeable;
import java.util.Collection;

import de.hpi.idd.store.StoreException;

/**
 * Interface for entity resolvers to determine similar or equal entities.
 * Entities are represented by their id.
 *
 * @param <RECORD>
 *            the type of records to be resolved
 * @param <ID>
 *            the type
 */
public interface EntityResolver<RECORD, ID> extends Closeable {

	/**
	 * Add a record to the entity resolver
	 *
	 * @param rec
	 *            the record to be added
	 * @param recId
	 *            the unique identifier of the record
	 * @throws Exception
	 */
	void insert(RECORD rec, ID recId) throws StoreException;

	/**
	 * Find duplicates for the specified record
	 *
	 * @param rec
	 *            the record to be resolved
	 * @param recId
	 *            the unique identifier of the record
	 * @return collection of ids of duplicate records
	 * @throws Exception
	 */
	Collection<ID> resolve(RECORD rec, ID recId) throws StoreException;

}
