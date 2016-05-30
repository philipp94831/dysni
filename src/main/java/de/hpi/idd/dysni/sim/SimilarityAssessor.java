package de.hpi.idd.dysni.sim;

public interface SimilarityAssessor<T> extends SimilarityMeasure<T> {

	default boolean areSimilar(final T record, final T record2) {
		return calculateSimilarity(record, record2) >= getThreshold();
	}

	double getThreshold();
}
