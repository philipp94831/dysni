package de.hpi.idd.dysni.sim;

public interface SimilarityAssessor<T> extends SimilarityMeasure<T> {

	default boolean areSimilar(final T record, final T record2) {
		return isSimilarity(calculateSimilarity(record, record2));
	}

	double getThreshold();

	default boolean isSimilarity(final double sim) {
		return sim >= getThreshold();
	}
}
