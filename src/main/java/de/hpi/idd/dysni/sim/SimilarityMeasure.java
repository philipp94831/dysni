package de.hpi.idd.dysni.sim;

public interface SimilarityMeasure<T> {

	default SimilarityAssessor<T> asAssessor(double threshold) {
		return new DefaultSimilarityAssessor<>(this, threshold);
	}

	double calculateSimilarity(T e1, T e2);
}
