package de.hpi.idd.dysni.sim;

import org.apache.commons.lang3.StringUtils;

/**
 * String similarity measure based on the Levenshtein distance
 *
 */
public class LevenshteinSimilarity implements SimilarityMeasure<String> {

	/**
	 * Normalized Levenshtein distance of two strings.
	 * 
	 * @return Levenshtein distanced normalized to the maximum length of the
	 *         strings
	 */
	@Override
	public double calculateSimilarity(String e1, String e2) {
		if (e1.isEmpty() && e2.isEmpty()) {
			return 1.0;
		}
		return 1.0 - (double) StringUtils.getLevenshteinDistance(e1.toLowerCase(), e2.toLowerCase())
				/ Math.max(e1.length(), e2.length());
	}
}