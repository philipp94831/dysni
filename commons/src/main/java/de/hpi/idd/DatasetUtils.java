package de.hpi.idd;

import java.util.Map;

abstract public class DatasetUtils {

	protected double datasetThreshold = Double.MIN_VALUE;

	/**
	 *
	 * Given two records, return their similarity in the range of [0,1].
	 *
	 * @param record1
	 * @param record2
	 * @param parameters
	 *            You could pass your parameters in a key, value form.
	 * @return The similarity in a double value of a range [0,1].
	 */
	abstract public Double calculateSimilarity(Map<String, Object> record1, Map<String, Object> record2,
			Map<String, String> parameters);

	public double getDatasetThreshold() {
		return datasetThreshold;
	}

	/**
	 *
	 * @param values
	 *            The record in a Key-Value <String, String> format. For
	 *            instance: <'id', '1'>, <'attribute1', 'value1'>,
	 *            <'attribute2', 'value2'>, ..., <'attributeN', valueN'>
	 * @return A dictionary with key-value objects: e.g. <attribute1, value1>
	 *         Each value can be of any type, thus it is Object (and not
	 *         String).
	 */
	abstract public Map<String, Object> parseRecord(Map<String, String> values);

	public void setDatasetThreshold(double datasetThreshold) {
		this.datasetThreshold = datasetThreshold;
	}

}
