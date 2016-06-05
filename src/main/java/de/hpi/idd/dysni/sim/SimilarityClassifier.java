package de.hpi.idd.dysni.sim;

/**
 * Extension to {@link SimilarityMeasure}. Classify the similarity of two
 * objects on a binary scale which indicates equality or not
 *
 * @param <T>
 *            type of the objects to be compared
 */
public interface SimilarityClassifier<T> extends SimilarityMeasure<T> {

	/**
	 * classify the equality of two objects based on their similarity
	 *
	 * @param record
	 *            first object
	 * @param record2
	 *            second object
	 * @return true if the objects are regarded equal considering the specified
	 *         threshold
	 * @throws ArithmeticException
	 *             if the similarity value violates certain constraints
	 */
	default boolean areSimilar(T record, T record2) throws ArithmeticException {
		return isSimilarity(calculateCheckedSimilarity(record, record2));
	}

	/**
	 * Threshold for which equality is indicated if exceeded
	 *
	 * @return threshold
	 */
	double getThreshold();

	/**
	 * classify the equality of two unknown objects based on their similarity
	 *
	 * @param sim
	 *            similarity of two unknown objects
	 * @return true if the objects are regarded equal considering the specified
	 *         threshold
	 */
	default boolean isSimilarity(double sim) {
		return sim >= getThreshold();
	}
}
