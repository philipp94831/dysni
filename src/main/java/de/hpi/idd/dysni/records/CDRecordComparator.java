package de.hpi.idd.dysni.records;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.SimilarityMeasure;
import de.hpi.idd.dysni.IdWrapper;
import de.hpi.idd.dysni.store.RecordStore;

/**
 * Created by dennis on 08.05.16.
 */
public class CDRecordComparator implements SimilarityMeasure {

	public enum CDSimilarity {
		ARTIST, DTITLE, GENRE, YEAR, CDEXTRA, TRACK, CATEGORY;

		private static final double TOTAL_WEIGHT = Arrays.stream(CDSimilarity.values())
				.mapToDouble(CDSimilarity::weight).sum();
		private final double weight;

		CDSimilarity() {
			this(1);
		}

		CDSimilarity(final double weight) {
			this.weight = weight;
		}

		public double weight() {
			return weight;
		}
	}

	private static final double THRESHOLD = 0.5;

	public static Map<CDSimilarity, Double> getSimilarityOfRecords(final CDRecord firstRecord,
			final CDRecord secondRecord) {
		final Map<CDSimilarity, Double> similarityMap = new HashMap<>();
		similarityMap.put(CDSimilarity.ARTIST,
				CDRecordComparator.levenshteinDistance(firstRecord.getArtist(), secondRecord.getArtist()));
		similarityMap.put(CDSimilarity.DTITLE,
				CDRecordComparator.levenshteinDistance(firstRecord.getdTitle(), secondRecord.getdTitle()));
		similarityMap.put(CDSimilarity.CATEGORY,
				CDRecordComparator.levenshteinDistance(firstRecord.getCategory(), secondRecord.getCategory()));
		similarityMap.put(CDSimilarity.GENRE,
				CDRecordComparator.levenshteinDistance(firstRecord.getGenre(), secondRecord.getGenre()));
		similarityMap.put(CDSimilarity.YEAR,
				CDRecordComparator.levenshteinDistance(firstRecord.getYear() + "", secondRecord.getYear() + ""));
		similarityMap.put(CDSimilarity.CDEXTRA,
				CDRecordComparator.levenshteinDistance(firstRecord.getCdExtra(), secondRecord.getCdExtra()));
		similarityMap.put(CDSimilarity.TRACK,
				CDRecordComparator.getSimilarityOfTracks(firstRecord.getTracks(), secondRecord.getTracks()));
		return similarityMap;
	}

	private static double getSimilarityOfTracks(final List<String> firstTracklist, final List<String> secondTracklist) {
		final HashSet<String> set = new HashSet<>(firstTracklist);
		set.retainAll(secondTracklist);
		final int shared = set.size();
		final Set<String> mergedTrackset = new HashSet<>();
		mergedTrackset.addAll(firstTracklist);
		mergedTrackset.addAll(secondTracklist);
		if (mergedTrackset.size() == 0) {
			return 1.0;
		}
		return (double) shared / mergedTrackset.size();
	}

	private static double levenshteinDistance(final String a, final String b) {
		return 1.0 - (double) StringUtils.getLevenshteinDistance(a.toLowerCase(), b.toLowerCase())
				/ Math.max(a.length(), b.length());
	}

	public static double similarity(final CDRecord firstRecord, final CDRecord secondRecord) {
		final Map<CDSimilarity, Double> similarityMap = CDRecordComparator.getSimilarityOfRecords(firstRecord,
				secondRecord);
		double result = 0.0;
		for (final Entry<CDSimilarity, Double> entry : similarityMap.entrySet()) {
			result += entry.getKey().weight() * entry.getValue();
		}
		return result / CDSimilarity.TOTAL_WEIGHT;
	}

	private final RecordStore<IdWrapper> store;

	public CDRecordComparator(final RecordStore<IdWrapper> store) {
		this.store = store;
	}

	private double calculateSimilarity(final Map<String, String> firstRecord, final Map<String, String> secondRecord) {
		return CDRecordComparator.similarity(CDRecordParser.parse(firstRecord), CDRecordParser.parse(secondRecord));
	}

	@Override
	public Double calculateSimilarity(final String recordID1, final String recordID2,
			final HashMap<String, String> parameters) {
		final Map<String, String> firstRecord = store.getRecord(recordID1).getObject();
		final Map<String, String> secondRecord = store.getRecord(recordID2).getObject();
		return calculateSimilarity(firstRecord, secondRecord);
	}

	@Override
	public double getThreshold() {
		return CDRecordComparator.THRESHOLD;
	}
}
