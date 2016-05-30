package de.hpi.idd.dysni.sim;

public interface SimilarityAssessor<T> extends SimilarityMeasure<T> {

	default boolean areSimilar(T record, T record2) {
		return isSimilarity(calculateSimilarity(record, record2));
	}

	double getThreshold();

	default boolean isSimilarity(double sim) {
		return sim >= getThreshold();
	}
}
