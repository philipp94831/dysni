package de.hpi.idd.dysni.sim;

import org.apache.commons.lang3.StringUtils;

public class LevenshteinMetric implements SimilarityMeasure<String> {

	@Override
	public double calculateSimilarity(String e1, String e2) {
		return 1.0 - (double) StringUtils.getLevenshteinDistance(e1, e2) / Math.max(e1.length(), e2.length());
	}
}