package de.hpi.idd;

import de.hpi.idd.store.StoreException;

import java.io.Closeable;
import java.util.Collection;

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
	 * @param record
	 *            the record to be added
	 * @param recordId
	 *            the unique identifier of the record
	 * @throws StoreException
	 */
	Collection<ID> insert(RECORD record, ID recordId) throws StoreException;

	/**
	 * Find duplicates for the specified record
	 *
	 * @param record
	 *            the record to be resolved
	 * @param recordId
	 *            the unique identifier of the record
	 * @return ids of duplicate records
	 * @throws StoreException
	 */
	Collection<ID> resolve(RECORD record, ID recordId) throws StoreException;

}
