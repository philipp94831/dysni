package de.hpi.idd;

import java.util.ArrayList;
import java.util.HashMap;

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
	public void buildIndex(HashMap<String, String> parameters);

	/**
	 *
	 * You should deallocate all the data structures.
	 *
	 * @param parameters:
	 *            You could pass your parameters in a key, value form.
	 * @return: true if successful, false otherwise
	 */
	public boolean destroyIndex(HashMap<String, String> parameters);

	// /**
	// *
	// * Remove the record from the index.
	// *
	// * @param recordID: remove the record with this recordID
	// * @return: true if successful, false otherwise
	// */
	// public boolean removeRecord(Integer recordID);
	/**
	 *
	 * Given a duplicate, return all the matching records you could find in your
	 * index.
	 *
	 * // * @param record: A generic, CSV-like type to provide the values of the
	 * record in a Key-Value style.
	 *
	 * @return: The result record IDs in an ArrayList container.
	 */
	public ArrayList<String> getDuplicates(HashMap<String, String> record, HashMap<String, String> parameters);

	/**
	 * Get the record with this record ID.
	 *
	 * @param recordID
	 * @return
	 */
	public ArrayList<String> getRecord(String recordID);

	/**
	 *
	 * Insert the record in the index.
	 *
	 * @param record:
	 *            A generic, CSV-like type to provide the values of the record
	 *            in a Key-Value style.
	 * @return: true if successful, false otherwise
	 */
	public boolean insertRecord(HashMap<String, String> record, HashMap<String, String> parameters);
}
