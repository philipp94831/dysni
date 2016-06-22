package de.hpi.idd;

import java.util.Map;

abstract public class DatasetUtils {
	
	protected double datasetThreshold = Double.MIN_VALUE;

	public double getDatasetThreshold() {
		return datasetThreshold;
	}

	public void setDatasetThreshold(double datasetThreshold) {
		this.datasetThreshold = datasetThreshold;
	}

	/**
	 * 
	 * Given two records, return their similarity in the range of [0,1].
	 * 
	 * @param record1
	 * @param record2
	 * @param parameters: You could pass your parameters in a key, value form.
	 * @return: The similarity in a double value of a range [0,1].
	 */
	abstract public Double calculateSimilarity(Map<String, Object> record1, Map<String, Object> record2, Map<String, String> parameters);
	
	/**
	 * 
	 * @param The record in a Key-Value <String, String> format. 
	 * 			For instance: <'id', '1'>, <'attribute1', 'value1'>, <'attribute2', 'value2'>, ..., <'attributeN', valueN'>
	 * @return A dictionary with key-value objects: e.g. <attribute1, value1>
	 * 			Each value can be of any type, thus it is Object (and not String).
	 */
	abstract public Map<String, Object> parseRecord(Map<String, String> values);
	
	
	/**
	 * Given the attribute name, as it appears on the dataset itself, and two values, return the similarity of them
	 *   in the range [0, 1].
	 * @param attribute: e.g. FirstName
	 * @param value1: e.g. Bob
	 * @param value2: e.g. Bobb
	 * @return e.g.: 0.75
	 */
	abstract public Double compareAttributeValue(String attribute, Object value1, Object value2);
	
	/**
	 * 
	 * Provided with all the attributes' similarities, it decides whether it is a match or not.
	 * 
	 * @param similarities: {<"FirstName", 0.75>, <"LastName", 0.85>, <"Year", 0.8>}
	 * @return If it is a match or not: e.g. True
	 */
	abstract public Boolean isMatch(Map<String, Double> similarities);
	
}
