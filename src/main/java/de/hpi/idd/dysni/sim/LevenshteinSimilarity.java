package de.hpi.idd.dysni.sim;

import org.apache.commons.lang3.StringUtils;

public class LevenshteinSimilarity implements SimilarityMeasure<String> {

	@Override
	public double calculateSimilarity(String e1, String e2) {
		if (e1.isEmpty() && e2.isEmpty()) {
			return 1.0;
		}
		return 1.0 - (double) StringUtils.getLevenshteinDistance(e1.toLowerCase(), e2.toLowerCase())
				/ Math.max(e1.length(), e2.length());
	}
}