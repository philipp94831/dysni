package de.hpi.idd;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import de.hpi.idd.data.cd.CDKeyHandler;
import de.hpi.idd.data.cd.CDSimilarityMeasure;
import de.hpi.idd.dysni.KeyHandler;

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

	public static Collection<KeyHandler<Map<String, String>, ?>> getKeyHandlers(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return CDKeyHandler.keyHandler();
		default:
			return Collections.emptyList();
		}
	}

	public static SimilarityMeasure getSimilarityMeasure(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return new CDSimilarityMeasure();
		default:
			return null;
		}
	}
}
