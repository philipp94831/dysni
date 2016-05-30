package de.hpi.idd;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import de.hpi.idd.data.cd.CDKeyHandler;
import de.hpi.idd.data.cd.CDSimilarityMeasure;
import de.hpi.idd.data.cora.CoraKeyHandler;
import de.hpi.idd.data.movies.MoviesKeyHandler;
import de.hpi.idd.data.ncvoters.NCVotersKeyHandler;
import de.hpi.idd.dysni.DySNIndexConfiguration;

public class DatasetManager {

	public static String getFileName(Dataset dataset) {
		switch (dataset) {
		case CD:
			return "cd_dataset.csv";
		case CORA:
			return "cora.csv";
		case MOVIES:
			return "movies_dataset.csv";
		case NCVOTERS:
			return "ncvoters.csv";
		default:
			return null;
		}
	}

	public static String getGroundThruth(Dataset dataset) {
		switch (dataset) {
		case CD:
			return "cd_dataset_duplicates.csv";
		case CORA:
			return "cora_ground_truth.csv";
		case MOVIES:
			return "movies_ground_truth.csv";
		case NCVOTERS:
			return "ncvoters_ground_truth.csv";
		default:
			return null;
		}
	}

	public static String getIdField(Dataset dataset) {
		switch (dataset) {
		case CD:
			return "did";
		case CORA:
			return "id";
		case MOVIES:
			return " id";
		default:
			return "id";
		}
	}

	public static Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> getKeyHandlers(Dataset dataset) {
		switch (dataset) {
		case CD:
			return CDKeyHandler.keyHandler();
		case CORA:
			return CoraKeyHandler.keyHandler();
		case MOVIES:
			return MoviesKeyHandler.keyHandler();
		case NCVOTERS:
			return NCVotersKeyHandler.keyHandler();
		default:
			return Collections.emptyList();
		}
	}

	public static SimilarityMeasure getSimilarityMeasure(Dataset dataset) {
		switch (dataset) {
		case CD:
			return new CDSimilarityMeasure();
		default:
			return null;
		}
	}
}
