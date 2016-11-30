package de.hpi.idd;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.DynamicSortedNeighborhoodIndexer;
import de.hpi.idd.sim.PluggableSimilarityMeasure;
import de.hpi.idd.sim.SimilarityClassifier;
import de.hpi.idd.store.MemoryStore;
import de.hpi.idd.store.RecordStore;
import de.hpi.idd.store.StoreException;

public class DySNIExampleApp {

	private enum ERType {
		BRUTE_FORCE, DYSNI
	}

	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withFirstRecordAsHeader();

	public static void main(String[] args) {
		DySNIExampleApp app = new DySNIExampleApp();
		JCommander jc = new JCommander(app, args);
		if (app.help) {
			jc.usage();
			System.exit(0);
		}
		app.run();
	}

	@Parameter(names = "--help", help = true)
	private boolean help = false;

	@Parameter(names = { "--threshold" })
	private double threshold = 0.5;
	@Parameter(names = { "--input", "-i" }, required = true)
	private String fileName;
	@Parameter(names = { "--out", "-o" }, required = true)
	private String outFileName;
	@Parameter(names = { "--id" })
	private String idField = "id";
	@Parameter(names = { "--type", "-t" })
	private ERType type = ERType.DYSNI;

	private EntityResolver<Map<String, String>, String> getEntityResolver(
			RecordStore<String, Map<String, String>> store,
			SimilarityClassifier<Map<String, String>> similarityClassifier) {
		switch (type) {
		case DYSNI:
			Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> configs = new ArrayList<>();
			return new DynamicSortedNeighborhoodIndexer<>(store, similarityClassifier).addIndexes(configs)
					.setParallelizable(true);
		case BRUTE_FORCE:
			return new BruteForceEntityResolver<>(store, similarityClassifier).setParallelizable(true);
		default:
			throw new IllegalArgumentException("Unknown EntityResolver: " + type);
		}
	}

	public void run() {
		PluggableSimilarityMeasure<String> similarityMeasure = new PluggableSimilarityMeasure<>();
		SimilarityClassifier<Map<String, String>> similarityClassifier = similarityMeasure.asClassifier(threshold);
		try (CSVParser parser = FORMAT.parse(new InputStreamReader(new FileInputStream(fileName)));
				EntityResolver<Map<String, String>, String> er = getEntityResolver(new MemoryStore<>(),
						similarityClassifier);
				BufferedWriter out = new BufferedWriter(new FileWriter(outFileName))) {
			int i = 0;
			long start = System.nanoTime();
			for (CSVRecord record : parser) {
				Map<String, String> rec = record.toMap();
				String id = rec.get(idField);
				Collection<String> duplicates = er.insert(rec, id);
				for (String duplicate : duplicates) {
					out.write(id + "\t" + duplicate);
					out.newLine();
				}
				i++;
			}
			long time = System.nanoTime() - start;
			System.out.println("Resolved " + i + " records in " + time / 1_000_000 + "ms");
		} catch (StoreException e) {
			throw new RuntimeException("Error accessing storage", e);
		} catch (IOException e) {
			throw new RuntimeException("Error parsing CSV", e);
		}
	}
}
