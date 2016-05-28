package de.hpi.idd.dysni.key;

import org.apache.commons.lang3.StringUtils;

public class LevenshteinComparator implements KeyComparator<String> {

	private final double threshold;

	public LevenshteinComparator(final double threshold) {
		this.threshold = threshold;
	}

	@Override
	public double compare(final String e1, final String e2) {
		return 1.0 - (double) StringUtils.getLevenshteinDistance(e1, e2) / Math.max(e1.length(), e2.length());
	}

	@Override
	public double getThreshold() {
		return threshold;
	}
}