package de.hpi.idd;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.data.Dataset;
import de.hpi.idd.util.SymmetricTable;

public class CoreApp {

	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withFirstRecordAsHeader();
	private static final String DATASET = "febrl";

	public static void main(String[] args) {
		Core core = new DySNICore();
		Map<String, String> parameters = new HashMap<>();
		parameters.put("dataset", DATASET);
		core.buildIndex(parameters);
		Dataset dataset = Dataset.getForName(DATASET);
		DatasetUtils du = dataset.getDataset();
		SymmetricTable<String, Boolean> duplicatesToCheck = new SymmetricTable<>();
		try (CSVParser parser = FORMAT.parse(new InputStreamReader(dataset.getFile()))) {
			for (CSVRecord record : parser) {
				Map<String, Object> rec = du.parseRecord(record.toMap());
				List<String> duplicates = core.insertRecord(rec, Collections.emptyMap());
				for (String duplicate : duplicates) {
					duplicatesToCheck.put((String) rec.get(Dataset.ID), duplicate, true);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		core.destroyIndex(Collections.emptyMap());
		Evaluator evaluator = new Evaluator(dataset.getGroundThruth());
		evaluator.evaluate(duplicatesToCheck);
	}

}
