package de.hpi.idd;

import java.util.HashMap;
import java.util.Map;

import de.hpi.idd.sim.SimilarityClassifier;

/**
 * Wrapper class to standardize the provided {@link SimilarityMeasure} and make
 * it generic
 *
 */
public class IDDSimilarityClassifier implements SimilarityClassifier<Map<String, String>> {

	private final SimilarityMeasure sim;

	/**
	 *
	 * @param sim
	 *            {@link SimilarityMeasure} to be wrapped
	 */
	public IDDSimilarityClassifier(SimilarityMeasure sim) {
		this.sim = sim;
	}

	@Override
	public double calculateSimilarity(Map<String, String> record, Map<String, String> record2) {
		return sim.calculateSimilarity(record, record2, new HashMap<>());
	}

	@Override
	public double getThreshold() {
		return sim.getThreshold();
	}
}
