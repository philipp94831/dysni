package de.hpi.idd.dysni.sim;

public interface GenericSimilarityMeasure<T> {

	default boolean areSimilar(final T record, final T record2) {
		return calculateSimilarity(record, record2) >= getThreshold();
	}

	double calculateSimilarity(T record, T record2);

	double getThreshold();
}
