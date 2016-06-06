package de.hpi.idd;

import java.util.HashMap;
import java.util.Map;

import de.hpi.idd.sim.SimilarityClassifier;

/**
 * Wrapper class to standardize the provided {@link DatasetUtils} and make it
 * generic
 *
 */
public class IDDSimilarityClassifier implements SimilarityClassifier<Map<String, Object>> {

	private final DatasetUtils sim;

	/**
	 *
	 * @param sim
	 *            {@link DatasetUtils} to be wrapped
	 */
	public IDDSimilarityClassifier(DatasetUtils sim) {
		this.sim = sim;
	}

	@Override
	public double calculateSimilarity(Map<String, Object> record, Map<String, Object> record2) {
		return sim.calculateSimilarity(record, record2, new HashMap<>());
	}

	@Override
	public double getThreshold() {
		return sim.getDatasetThreshold();
	}
}
