package de.hpi.idd;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.hpi.idd.cd.CDKeyHandler;
import de.hpi.idd.cd.CDKeyHandler2;
import de.hpi.idd.cd.CDRecordComparator;
import de.hpi.idd.dysni.key.KeyHandler;

public class DatasetManager {

	public static String getFileName(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return "cd_dataset.csv";
		default:
			return null;
		}
	}

	public static String getGroundThruth(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return "cd_dataset_duplicates.csv";
		default:
			return null;
		}
	}

	public static String getIdField(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return "did";
		default:
			return null;
		}
	}

	public static List<KeyHandler<Map<String, String>, String>> getKeyHandlers(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return Arrays.asList(new CDKeyHandler(), new CDKeyHandler2());
		default:
			return Collections.emptyList();
		}
	}

	public static SimilarityMeasure getSimilarityMeasure(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return new CDRecordComparator();
		default:
			return null;
		}
	}
}
