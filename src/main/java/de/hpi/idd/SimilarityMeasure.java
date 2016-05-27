package de.hpi.idd;

import java.util.ArrayList;
import java.util.HashMap;

public interface SimilarityMeasure {

	public double getThreshold();

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
	public Double calculateSimilarity(String recordID1, String recordID2, HashMap<String, String> parameters);

}
