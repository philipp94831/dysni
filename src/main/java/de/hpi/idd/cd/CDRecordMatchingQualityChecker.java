package de.hpi.idd.cd;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by dennis on 26.05.16.
 */
public class CDRecordMatchingQualityChecker {

    private Map<String, List<String>> duplicates;

    public CDRecordMatchingQualityChecker() {
        duplicates = new HashMap<>();
        try {
            CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader("data/cd_dataset_duplicates.csv"));
            for (CSVRecord record : parser) {
                Map<String, String> rec = record.toMap();
                if (!duplicates.containsKey(rec.get("firstId"))) {
                     duplicates.put(rec.get("firstId"), new LinkedList<>());
                }
                duplicates.get(rec.get("firstId")).add(rec.get("secondId"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkDuplicates(Map<String, List<String>> duplicatesToCheck) {
        int wrong = 0;
        int right = 0;
        double recall = 0.0;
        for (String key : duplicatesToCheck.keySet()) {
            List<String> trueDuplicates = this.duplicates.get(key);

            if (!(trueDuplicates == null)) {
                for (String recordId : duplicatesToCheck.get(key)) {
                    if (trueDuplicates.contains(recordId)) {
                        right++;
                    } else {
                        wrong++;
                    }
                }
            } else {
                wrong += duplicatesToCheck.get(key).size();
            }
        }
        System.out.println("debug");
    }
}
