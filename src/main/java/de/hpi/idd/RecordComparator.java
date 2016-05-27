package de.hpi.idd;

import java.util.Map;

public interface RecordComparator {

	public default boolean areSimilar(Map<String, String> firstRecord, Map<String, String> secondRecord) {
		return calculateSimilarity(firstRecord, secondRecord) > getThreshold();
	}

	public double calculateSimilarity(Map<String, String> t1, Map<String, String> t2);

	public double getThreshold();
}
