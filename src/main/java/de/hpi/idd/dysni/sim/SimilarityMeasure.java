package de.hpi.idd.dysni.sim;

public interface SimilarityMeasure<T> {

	double calculateSimilarity(T e1, T e2);
}
