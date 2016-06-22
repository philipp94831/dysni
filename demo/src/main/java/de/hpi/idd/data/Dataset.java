package de.hpi.idd.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import de.hpi.idd.DatasetUtils;
import de.hpi.idd.data.cd.CDConfig;
import de.hpi.idd.data.cd.CDDataset;
import de.hpi.idd.data.cora.CoraConfig;
import de.hpi.idd.data.cora.Utility;
import de.hpi.idd.data.movies.MovieSimilarityMeasure;
import de.hpi.idd.data.movies.MoviesConfig;
import de.hpi.idd.data.ncvoters.NCVotersConfig;
import de.hpi.idd.data.ncvoters.NCVotersSimilarity;
import de.hpi.idd.data.people.PeopleConfig;
import de.hpi.idd.data.people.SimilarityFunctionForPeopleDataset;
import de.hpi.idd.dysni.DySNIndexConfiguration;

/**
 * Utility class to access the properties of each data set used in this course
 *
 */
public enum Dataset {
	CD(CDConfig.config(), "cd_dataset.csv", "cd_dataset_duplicates.csv", new CDDataset()),
	CORA(CoraConfig.config(), "cora_v2.csv", "cora_ground_truth.csv", new Utility()),
	MOVIES(MoviesConfig.config(), "movies_dataset.csv", "movies_ground_truth.csv", new MovieSimilarityMeasure()),
	NCVOTERS(NCVotersConfig.config(), "ncvoters1M.csv", "ncvoters1M_ground_truth.csv", new NCVotersSimilarity()),
	PEOPLE(PeopleConfig.config(), "parsed1M.csv", "gold_standard_1M.csv", new SimilarityFunctionForPeopleDataset());

	public static final String ID = "id";

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
		case "people":
			return PEOPLE;
		default:
			throw new RuntimeException("Dataset \"" + name + "\" not available");
		}
	}

	private final Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> configs;
	private final String fileName;
	private final String groundTruth;
	private final DatasetUtils similarityMeasure;

	Dataset(Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> configs, String fileName,
			String groundTruth, DatasetUtils similarityMeasure) {
		this.configs = configs;
		this.fileName = fileName;
		this.groundTruth = groundTruth;
		this.similarityMeasure = similarityMeasure;
	}

	public Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> getConfigs() {
		return configs;
	}

	public DatasetUtils getDataset() {
		return similarityMeasure;
	}

	public InputStream getFile() {
		try {
			return Dataset.class.getResource(fileName).openStream();
		} catch (IOException e) {
			throw new RuntimeException("Resource not available: " + fileName, e);
		}
	}

	public InputStream getGroundThruth() {
		try {
			return Dataset.class.getResource(groundTruth).openStream();
		} catch (IOException e) {
			throw new RuntimeException("Resource not available: " + groundTruth, e);
		}
	}
}
