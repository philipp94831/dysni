package de.hpi.idd.dysni.sim;

/**
 * Simple class to transform any {@link SimilarityMeasure} into an
 * {@link SimilarityClassifier}
 *
 * @param <T>
 *            type of objects to be compared
 */
class DefaultSimilarityClassifier<T> implements SimilarityClassifier<T> {

	private final SimilarityMeasure<T> sim;
	private final double threshold;

	/**
	 *
	 * @param sim
	 *            similarity measure to be used
	 * @param threshold
	 *            threhold which indicates equality or not
	 */
	public DefaultSimilarityClassifier(SimilarityMeasure<T> sim, double threshold) {
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
