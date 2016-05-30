package de.hpi.idd;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.collect.Table.Cell;

import de.hpi.idd.dysni.util.SymmetricTable;
import de.hpi.idd.dysni.util.UnionFind;

/**
 * Created by dennis on 26.05.16.
 */
public class Evaluator {

	private static final boolean VERBOSE = false;
	private final UnionFind<String> duplicates;

	public Evaluator(String groundThruthFile) {
		duplicates = new UnionFind<>();
		try {
			CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader(groundThruthFile));
			for (CSVRecord rec : parser) {
				duplicates.union(rec.get(0), rec.get(1));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void evaluate(SymmetricTable<String, Boolean> duplicatesToCheck) {
		int fp = 0;
		int tp = 0;
		int fn = 0;
		for (Cell<String, String, Boolean> cell : duplicatesToCheck.cellSet()) {
			if (duplicates.connected(cell.getRowKey(), cell.getColumnKey())) {
				tp++;
			} else {
				fp++;
				if (Evaluator.VERBOSE) {
					System.out.println("INCORRECT resolution: " + cell.getRowKey() + " " + cell.getColumnKey());
				}
			}
		}
		int t = 0;
		for (String root : duplicates.getRoots()) {
			List<String> elems = new ArrayList<>(duplicates.getComponent(root));
			elems.add(root);
			for (int i = 0; i < elems.size(); i++) {
				for (int j = i + 1; j < elems.size(); j++) {
					t++;
					if (!duplicatesToCheck.contains(elems.get(i), elems.get(j))) {
						fn++;
						if (Evaluator.VERBOSE) {
							System.out.println("MISSED resolution: " + elems.get(i) + " " + elems.get(j));
						}
					}
				}
			}
		}
		int p = tp + fp;
		double recall = (double) tp / t;
		double precision = (double) tp / p;
		double fmeasure = 2 * (precision * recall) / (precision + recall);
		System.out.println("Found " + p + " duplicates. Of the " + t + " true duplicates " + tp + " were found and "
				+ fn + " were missed. " + fp + " found duplicates are wrong.");
		System.out.println("Precision is " + precision);
		System.out.println("Recall is " + recall);
		System.out.println("F-Measure is " + fmeasure);
	}
}
