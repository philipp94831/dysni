package de.hpi.idd;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import de.hpi.idd.data.Dataset;

public class DySNIExecuteInstructions implements ExecuteInstructions {

	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withFirstRecordAsHeader();
	private final Core core;

	public DySNIExecuteInstructions() {
		core = new DySNICore();
	}

	@Override
	public void executeFile(File fileIn, File fileOut, Map<String, String> parameters) {
		core.buildIndex(parameters);
		DatasetUtils du = Dataset.getForName(parameters.get("dataset")).getDataset();
		try (CSVParser parser = FORMAT.parse(new FileReader(fileIn))) {
			for (CSVRecord record : parser) {
				Map<String, Object> rec = du.parseRecord(record.toMap());
				String instruction = record.get("instruction");
				executeInstruction(instruction, rec);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		core.destroyIndex(Collections.emptyMap());
	}

	@Override
	public void executeInstruction(String instruction, Map<String, Object> record) {
		switch (instruction) {
		case "insert":
			core.insertRecord(record, Collections.emptyMap());
			break;
		case "getDuplicates":
			core.getDuplicates(record, Collections.emptyMap());
			break;
		default:
			break;
		}
	}

}
