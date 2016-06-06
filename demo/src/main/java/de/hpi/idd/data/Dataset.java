package de.hpi.idd.data;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.data.cd.CDConfig;
import de.hpi.idd.data.cd.CDSimilarityMeasure;
import de.hpi.idd.data.cora.CoraConfig;
import de.hpi.idd.data.movies.MoviesConfig;
import de.hpi.idd.data.ncvoters.NCVotersConfig;
import de.hpi.idd.dysni.DySNIndexConfiguration;

/**
 * Utility class to access the properties of each data set used in this course
 *
 */
public enum Dataset {
	CD(CDConfig.config(), "cd_dataset.csv", "cd_dataset_duplicates.csv", "id", new CDSimilarityMeasure()),
	CORA(CoraConfig.config(), "cora.csv", "cora_ground_truth.csv", "id", null),
	MOVIES(MoviesConfig.config(), "movies_dataset.csv", "movies_ground_truth.csv", "id", null),
	NCVOTERS(NCVotersConfig.config(), "ncvoters.csv", "ncvoters_ground_truth.csv", "id", null);

	public static Dataset getForName(String name) {
		switch (name.toLowerCase()) {
		case "cd":
			return CD;
		case "cora":
			return CORA;
		case "movies":
			return MOVIES;
		case "ncvoters":
			return NCVOTERS;
		default:
			return null;
		}
	}

	private final Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> configs;
	private final String fileName;
	private final String groundTruth;
	private final String idField;
	private final SimilarityMeasure similarityMeasure;

	Dataset(Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> configs, String fileName,
			String groundTruth, String idField, SimilarityMeasure similarityMeasure) {
		this.configs = configs;
		this.fileName = fileName;
		this.groundTruth = groundTruth;
		this.idField = idField;
		this.similarityMeasure = similarityMeasure;
	}

	public Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> getConfigs() {
		return configs;
	}

	public InputStream getFile() {
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}

	public InputStream getGroundThruth() {
		return this.getClass().getClassLoader().getResourceAsStream(groundTruth);
	}

	public String getIdField() {
		return idField;
	}

	public SimilarityMeasure getSimilarityMeasure() {
		return similarityMeasure;
	}
}
