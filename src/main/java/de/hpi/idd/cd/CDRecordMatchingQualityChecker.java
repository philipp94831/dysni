package de.hpi.idd.cd;

import java.io.FileReader;
import java.io.IOException;
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
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void checkDuplicates(final Map<String, List<String>> duplicatesToCheck) {
		for (final String key : duplicatesToCheck.keySet()) {
			final List<String> trueDuplicates = duplicates.get(key);
			if (!(trueDuplicates == null)) {
				for (final String recordId : duplicatesToCheck.get(key)) {
					if (trueDuplicates.contains(recordId)) {
					} else {
					}
				}
			} else {
				duplicatesToCheck.get(key).size();
			}
		}
		System.out.println("debug");
	}
}
