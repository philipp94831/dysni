package de.hpi.idd.dysni.comp;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.dysni.avl.KeyComparator;

public class LevenshteinComparator implements KeyComparator<String> {

	private final double threshold;

	public LevenshteinComparator(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public double compare(String e1, String e2) {
		return 1.0 - (double) StringUtils.getLevenshteinDistance(e1, e2) / Math.max(e1.length(), e2.length());
	}

	@Override
	public double getThreshold() {
		return threshold;
	}
}