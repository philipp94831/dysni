package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.HashMap;

public interface Core {

	/**
	 * 
	 * In this method, you should instantiate all the necessary index parts in order to be able to handle
	 * insertRecord, removeRecord and getDuplicates queries.
	 * 
	 * @param parameters: You could pass your parameters in a key, value form.
	 */
	public void buildIndex(HashMap<String, String> parameters);
		
	/**
	 * 
	 * Insert the record in the index.
	 * 
	 * @param record: A generic, CSV-like type to provide the values of the record.
	 * @return: true if successful, false otherwise
	 */
	public boolean insertRecord(ArrayList<String> record, Integer recordID);
	
//	/**
//	 * 
//	 * Remove the record from the index.
//	 * 
//	 * @param recordID: remove the record with this recordID
//	 * @return: true if successful, false otherwise
//	 */
//	public boolean removeRecord(Integer recordID);
	
	/**
	 * 
	 * Given a duplicate, return all the matching records you could find in your index.
	 * 
	 * @param record: A generic, CSV-like type to provide the values of the record.
	 * @return: The results in a CSV-like type (for each record), in a ArrayList container.
	 */
	public ArrayList<Integer> getDuplicates(ArrayList<String> record);
	
	/**
	 * 
	 * You should deallocate all the data structures.
	 * 
	 * @param parameters: You could pass your parameters in a key, value form.
	 * @return: true if successful, false otherwise
	 */
	public boolean destroyIndex(HashMap<String, String> parameters);
	
	/**
	 * Get the attributes from the record with this record ID.
	 * @param recordID
	 * @return
	 */
	public ArrayList<String> getRecordAttributes(Integer recordID);
}
