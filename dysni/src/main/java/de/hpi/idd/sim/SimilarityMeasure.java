package de.hpi.idd.sim;

/**
 * Interface for measuring the similarity of records on a scale from 0 to 1
 *
 * @param <T>
 *            type of the objects to be compared
 */
public interface SimilarityMeasure<T> {

	/**
	 * Get a classifier based on this similarity measure which classifies the
	 * similarity of objects binary
	 *
	 * @param threshold
	 *            threshold to decide similarity. Similarity is assumed when the
	 *            similarity is above the threshold
	 * @return classifier based on this similarity measure
	 */
	default SimilarityClassifier<T> asClassifier(double threshold) {
		return new DefaultSimilarityClassifier<>(this, threshold);
	}

	/**
	 * Verifies that the similarity of two objects fulfills certain constraints
	 *
	 * @param e1
	 *            first object
	 * @param e2
	 *            second object
	 * @return similarity of the two objects on a scale from 0.0 to 1.0
	 * @throws ArithmeticException
	 *             if the similarity value violates the constraints
	 *
	 * @see #calculateSimilarity(Object, Object)
	 */
	default double calculateCheckedSimilarity(T e1, T e2) throws ArithmeticException {
		double sim = calculateSimilarity(e1, e2);
		if (Double.isNaN(sim)) {
//			throw new ArithmeticException("Similarity is NaN");
		}
		if (sim < 0.0 || sim > 1.0) {
//			throw new ArithmeticException("Similarity is not in interval [0.0, 1.0]");
		}
		return sim;
	}

	/**
	 * Compute the similarity of two objects, usually on a scale from 0.0 to 1.0
	 *
	 * @param e1
	 *            first object
	 * @param e2
	 *            second object
	 * @return similarity of the objects
	 */
	abstract double calculateSimilarity(T e1, T e2);
}
