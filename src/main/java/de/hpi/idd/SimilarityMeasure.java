package de.hpi.idd;

import java.util.HashMap;
import java.util.Map;

public interface SimilarityMeasure {

	/**
	 *
	 * Given two record IDs, return their similarity in the range of [0,1].
	 *
	 * @param recordID1
	 * @param recordID2
	 * @param parameters:
	 *            You could pass your parameters in a key, value form.
	 * @return: The result record IDs in an ArrayList container.
	 */
	public Double calculateSimilarity(Map<String, String> record, Map<String, String> record2,
			HashMap<String, String> parameters);

	public double getThreshold();
}
