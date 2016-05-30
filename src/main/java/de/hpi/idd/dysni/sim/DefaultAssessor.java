package de.hpi.idd.dysni.sim;

public class DefaultAssessor<T> implements SimilarityAssessor<T> {

	private final SimilarityMeasure<T> sim;
	private final double threshold;

	public DefaultAssessor(SimilarityMeasure<T> sim, double threshold) {
		this.sim = sim;
		this.threshold = threshold;
	}

	@Override
	public double calculateSimilarity(T e1, T e2) {
		return sim.calculateSimilarity(e1, e2);
	}

	@Override
	public double getThreshold() {
		return threshold;
	}

}
