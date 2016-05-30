package de.hpi.idd.dysni.sim;

public class DefaultAssessor<T> implements SimilarityAssessor<T> {

	private final SimilarityMeasure<T> sim;
	private final double threshold;

	public DefaultAssessor(final SimilarityMeasure<T> sim, final double threshold) {
		this.sim = sim;
		this.threshold = threshold;
	}

	@Override
	public double calculateSimilarity(final T e1, final T e2) {
		return sim.calculateSimilarity(e1, e2);
	}

	@Override
	public double getThreshold() {
		return threshold;
	}

}
