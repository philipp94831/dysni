package de.hpi.idd.sim;

import java.util.HashMap;
import java.util.Map;

public class PluggableSimilarityMeasure<T> implements SimilarityMeasure<Map<String, T>> {

	private static class PluggedSimilarityMeasureConfiguration<T> {

		private final double weight;
		private final SimilarityMeasure<T> similarityMeasure;

		public PluggedSimilarityMeasureConfiguration(double weight, SimilarityMeasure<T> similarityMeasure) {
			this.weight = weight;
			this.similarityMeasure = similarityMeasure;
		}

		public double getWeight() {
			return weight;
		}

		public SimilarityMeasure<T> getSimilarityMeasure() {
			return similarityMeasure;
		}

	}

	private final Map<String, PluggedSimilarityMeasureConfiguration<T>> configurations = new HashMap<>();
	private double totalWeight = 0.0;

	@Override
	public double calculateSimilarity(Map<String, T> e1, Map<String, T> e2) {
		double sum = 0.0;
		for (String key : e1.keySet()) {
			PluggedSimilarityMeasureConfiguration<T> configuration = configurations.get(key);
			SimilarityMeasure<T> similarityMeasure = configuration.getSimilarityMeasure();
			double weight = configuration.getWeight();
			sum += weight * similarityMeasure.calculateCheckedSimilarity(e1.get(key), e2.get(key));
		}
		return totalWeight == 0.0 ? 0.0 : sum / totalWeight;
	}

	public PluggableSimilarityMeasure<T> addSimilarityMeasure(String attributeName,
			SimilarityMeasure<T> similarityMeasure, double weight) {
		PluggedSimilarityMeasureConfiguration<T> configuration = new PluggedSimilarityMeasureConfiguration<>(weight,
				similarityMeasure);
		configurations.put(attributeName, configuration);
		totalWeight += weight;
		return this;
	}

}
