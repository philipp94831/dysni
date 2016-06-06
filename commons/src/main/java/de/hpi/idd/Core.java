package de.hpi.idd;

import java.util.List;
import java.util.Map;

public interface Core {

	/**
	 *
	 * In this method, you should instantiate all the necessary index parts in
	 * order to be able to handle insertRecord, removeRecord and getDuplicates
	 * queries.
	 *
	 * @param parameters:
	 *            You could pass your parameters in a key, value form.
	 */
	public void buildIndex(Map<String, String> parameters);

	/**
	 *
	 * You should deallocate all the data structures.
	 *
	 * @param parameters:
	 *            You could pass your parameters in a key, value form.
	 * @return: true if successful, false otherwise
	 */
	public boolean destroyIndex(Map<String, String> parameters);

	/**
	 *
	 * Given a duplicate, return all the matching records you could find in your
	 * index.
	 *
	 * @param record:
	 *            A generic, CSV-like type to provide the values of the record
	 *            in a Key-Value style.
	 * @param parameters:
	 *            Same as record.
	 * @return: The result record IDs in an List container.
	 */
	public List<String> getDuplicates(Map<String, Object> record, Map<String, String> parameters);

	/**
	 * Get the record with this record ID.
	 *
	 * @param recordID
	 * @return the record in a key-value format.
	 */
	public Map<String, Object> getRecord(String recordID);

	/**
	 *
	 * Insert the record in the index.
	 *
	 * @param record:
	 *            A generic, CSV-like type to provide the values of the record
	 *            in a Key-Value style.
	 * @return: true if successful, false otherwise
	 */
	public boolean insertRecord(Map<String, Object> record, Map<String, String> parameters);
}
