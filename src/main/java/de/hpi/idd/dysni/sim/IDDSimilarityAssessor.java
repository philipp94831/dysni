package de.hpi.idd.dysni.sim;

import java.util.HashMap;
import java.util.Map;

import de.hpi.idd.SimilarityMeasure;

public class IDDSimilarityAssessor implements SimilarityAssessor<Map<String, String>> {

	private final SimilarityMeasure sim;

	public IDDSimilarityAssessor(final SimilarityMeasure sim) {
		this.sim = sim;
	}

	@Override
	public double calculateSimilarity(final Map<String, String> record, final Map<String, String> record2) {
		return sim.calculateSimilarity(record, record2, new HashMap<>());
	}

	@Override
	public double getThreshold() {
		return sim.getThreshold();
	}
}
