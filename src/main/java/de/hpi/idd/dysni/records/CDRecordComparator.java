package de.hpi.idd.dysni.records;

import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.RecordComparator;

/**
 * Created by dennis on 08.05.16.
 */
public class CDRecordComparator implements RecordComparator<Map<String, String>> {

	private static final double THRESHOLD = 0.5;

	public static Map<CDSimilarity, Double> getSimilarityOfRecords(CDRecord firstRecord, CDRecord secondRecord) {
		Map<CDSimilarity, Double> similarityMap = new HashMap<>();
		similarityMap.put(CDSimilarity.ARTIST, levenshteinDistance(firstRecord.getArtist(), secondRecord.getArtist()));
		similarityMap.put(CDSimilarity.DTITLE, levenshteinDistance(firstRecord.getdTitle(), secondRecord.getdTitle()));
		similarityMap.put(CDSimilarity.CATEGORY,
				levenshteinDistance(firstRecord.getCategory(), secondRecord.getCategory()));
		similarityMap.put(CDSimilarity.GENRE, levenshteinDistance(firstRecord.getGenre(), secondRecord.getGenre()));
		similarityMap.put(CDSimilarity.YEAR,
				levenshteinDistance(firstRecord.getYear() + "", secondRecord.getYear() + ""));
		similarityMap.put(CDSimilarity.CDEXTRA,
				levenshteinDistance(firstRecord.getCdExtra(), secondRecord.getCdExtra()));
		similarityMap.put(CDSimilarity.TRACK, getSimilarityOfTracks(firstRecord.getTracks(), secondRecord.getTracks()));
		return similarityMap;
	}

	@Override
	public boolean areSimilar(Map<String, String> firstRecord, Map<String, String> secondRecord) {
		CDRecordParser p = new CDRecordParser();
		return similarity(p.parse(firstRecord), p.parse(secondRecord)) > THRESHOLD;
	}

	private static double getSimilarityOfTracks(List<String> firstTracklist, List<String> secondTracklist) {
		HashSet<String> set = new HashSet<>(firstTracklist);
		set.retainAll(secondTracklist);
		int shared = set.size();
		Set<String> mergedTrackset = new HashSet<>();
		mergedTrackset.addAll(firstTracklist);
		mergedTrackset.addAll(secondTracklist);
		if (mergedTrackset.size() == 0) {
			return 1.0;
		}
		return (double) shared / mergedTrackset.size();
	}

	public static double levenshteinDistance(String a, String b) {
		return 1.0 - (double) StringUtils.getLevenshteinDistance(a.toLowerCase(), b.toLowerCase())
				/ Math.max(a.length(), b.length());
	}

	public static enum CDSimilarity {
		ARTIST, DTITLE, GENRE, YEAR, CDEXTRA, TRACK, CATEGORY;

		private final double weight;
		private static final double TOTAL_WEIGHT = Arrays.stream(values()).mapToDouble(CDSimilarity::weight).sum();

		private CDSimilarity() {
			this(1);
		}

		private CDSimilarity(double weight) {
			this.weight = weight;
		}

		public double weight() {
			return weight;
		}
	}

	public static double similarity(CDRecord firstRecord, CDRecord secondRecord) {
		Map<CDSimilarity, Double> similarityMap = getSimilarityOfRecords(firstRecord, secondRecord);
		double result = 0.0;
		for (Entry<CDSimilarity, Double> entry : similarityMap.entrySet()) {
			result += entry.getKey().weight() * entry.getValue();
		}
		return result / CDSimilarity.TOTAL_WEIGHT;
	}
}
