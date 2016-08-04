package de.hpi.idd.sim;

public class EqualsSimilarityClassifier<T> implements SimilarityClassifier<T> {

	@Override
	public double calculateSimilarity(T e1, T e2) {
		return e1.equals(e2) ? 1.0 : 0.0;
	}

	@Override
	public double getThreshold() {
		return 1.0;
	}

}