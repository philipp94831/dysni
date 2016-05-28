package de.hpi.idd.cd;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Created by dennis on 26.05.16.
 */
public class CDRecordMatchingQualityChecker {

	private final Map<String, List<String>> duplicates;

	public CDRecordMatchingQualityChecker() {
		duplicates = new HashMap<>();
		try {
			final CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader()
					.parse(new FileReader("data/cd_dataset_duplicates.csv"));
			for (final CSVRecord record : parser) {
				final Map<String, String> rec = record.toMap();
				if (!duplicates.containsKey(rec.get("firstId"))) {
					duplicates.put(rec.get("firstId"), new LinkedList<>());
				}
				duplicates.get(rec.get("firstId")).add(rec.get("secondId"));
				if (!duplicates.containsKey(rec.get("secondId"))) {
					duplicates.put(rec.get("secondId"), new LinkedList<>());
				}
				duplicates.get(rec.get("secondId")).add(rec.get("firstId"));
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void checkDuplicates(Map<String, List<String>> duplicatesToCheck) {
		int wrong = 0;
		int right = 0;
		for (String key : duplicatesToCheck.keySet()) {
			List<String> trueDuplicates = this.duplicates.getOrDefault(key, Collections.emptyList());
			for (String recordId : duplicatesToCheck.get(key)) {
				if (trueDuplicates.contains(recordId)) {
					right++;
					// System.out.println("CORRECT resolution: " + key + " " + recordId);
				} else {
					wrong++;
					System.out.println("INCORRECT resolution: " + key + " " + recordId);
				}
			}
		}
		double precision = (double) right / 298;
		System.out.println("Of the found duplicates " + right + " are correct and " + wrong + " are incorrect.");
		System.out.println("Precision is " + precision);
	}
}
