package de.hpi.idd;

import java.util.Map;

abstract public class DatasetUtils {

	protected double datasetThreshold = Double.MIN_VALUE;

	/**
	 *
	 * Given two record IDs, return their similarity in the range of [0,1].
	 *
	 * @param recordID1
	 * @param recordID2
	 * @param parameters:
	 *            You could pass your parameters in a key, value form.
	 * @return: The similarity in a double value of a range [0,1].
	 */
	abstract public Double calculateSimilarity(Map<String, Object> recordID1, Map<String, Object> recordID2,
			Map<String, String> parameters);

	public double getDatasetThreshold() {
		return datasetThreshold;
	}

	/**
	 *
	 * @param The
	 *            record in a String format. For instance: 'record_id, value1,
	 *            value2,... ,valueN'
	 * @return A dictionary with key-value objects: e.g. <attribute1, value1>
	 *         Each value can be of any type, thus it is Object (and not
	 *         String).
	 */
	abstract public Map<String, Object> parseRecord(Map<String, String> value);

	public void setDatasetThreshold(double datasetThreshold) {
		this.datasetThreshold = datasetThreshold;
	}

}
