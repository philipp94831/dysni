package de.hpi.idd.data.cd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.SimilarityMeasure;

/**
 * Created by dennis on 08.05.16.
 */
public class CDSimilarityMeasure implements SimilarityMeasure {

	static class CDRecord {

		private String artist;
		private String category;
		private String cdExtra;
		private String dId;
		private String dTitle;
		private String genre;
		private List<String> tracks;
		private Short year;

		public CDRecord() {
		}

		public CDRecord(final String dId, final String artist, final String dTitle, final String category,
				final String genre, final String cdExtra, final short year, final List<String> tracks) {
			this.dId = dId;
			this.artist = artist;
			this.dTitle = dTitle;
			this.category = category;
			this.genre = genre;
			this.cdExtra = cdExtra;
			this.year = year;
			this.tracks = tracks;
		}

		public String getArtist() {
			return artist;
		}

		public String getCategory() {
			return category;
		}

		public String getCdExtra() {
			return cdExtra;
		}

		public String getdId() {
			return dId;
		}

		public String getdTitle() {
			return dTitle;
		}

		public String getGenre() {
			return genre;
		}

		public List<String> getTracks() {
			return tracks;
		}

		public Short getYear() {
			return year;
		}

		public void setArtist(final String artist) {
			this.artist = artist;
		}

		public void setCategory(final String category) {
			this.category = category;
		}

		public void setCdExtra(final String cdExtra) {
			this.cdExtra = cdExtra;
		}

		public void setdId(final String dId) {
			this.dId = dId;
		}

		public void setdTitle(final String dTitle) {
			this.dTitle = dTitle;
		}

		public void setGenre(final String genre) {
			this.genre = genre;
		}

		public void setTracks(final List<String> tracks) {
			this.tracks = tracks;
		}

		public void setYear(final short year) {
			this.year = year;
		}

		@Override
		public String toString() {
			return dId;
		}
	}

	private static class CDRecordParser {

		public static CDRecord parse(final Map<String, String> record) {
			final CDRecord cd = new CDRecord();
			cd.setdId(record.get("did"));
			cd.setArtist(record.get("artist"));
			cd.setdTitle(record.get("dtitle"));
			cd.setCategory(record.get("category"));
			final String year = record.get("year");
			if (!year.isEmpty()) {
				cd.setYear(Short.parseShort(year));
			}
			cd.setGenre(record.get("genre"));
			cd.setCdExtra(record.get("cdextra"));
			cd.setTracks(Arrays.asList(record.get("tracks").split("\\|")));
			return cd;
		}
	}

	public enum CDSimilarity {
		ARTIST(5), CATEGORY, CDEXTRA, DTITLE(3), GENRE, TRACK(2), YEAR;

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

	private static final double THRESHOLD = 0.7;

	private static int getNthDigit(final int number, final int n) {
		return (int) (Math.abs(number) / Math.pow(10, n) % 10);
	}

	private static int getNumberOfDigits(final int number) {
		return (int) (Math.log10(Math.abs(number)) + 1);
	}

	public static Map<CDSimilarity, Double> getSimilarityOfRecords(final CDRecord firstRecord,
			final CDRecord secondRecord) {
		final Map<CDSimilarity, Double> similarityMap = new HashMap<>();
		similarityMap.put(CDSimilarity.ARTIST,
				CDSimilarityMeasure.levenshteinDistance(firstRecord.getArtist(), secondRecord.getArtist()));
		similarityMap.put(CDSimilarity.DTITLE,
				CDSimilarityMeasure.levenshteinDistance(firstRecord.getdTitle(), secondRecord.getdTitle()));
		similarityMap.put(CDSimilarity.CATEGORY,
				CDSimilarityMeasure.levenshteinDistance(firstRecord.getCategory(), secondRecord.getCategory()));
		similarityMap.put(CDSimilarity.GENRE,
				CDSimilarityMeasure.levenshteinDistance(firstRecord.getGenre(), secondRecord.getGenre()));
		similarityMap.put(CDSimilarity.YEAR,
				CDSimilarityMeasure.yearDistance(firstRecord.getYear(), secondRecord.getYear()));
		similarityMap.put(CDSimilarity.CDEXTRA,
				CDSimilarityMeasure.levenshteinDistance(firstRecord.getCdExtra(), secondRecord.getCdExtra()));
		similarityMap.put(CDSimilarity.TRACK,
				CDSimilarityMeasure.getSimilarityOfTracks(firstRecord.getTracks(), secondRecord.getTracks()));
		return similarityMap;
	}

	private static double getSimilarityOfTracks(final List<String> firstTracklist, final List<String> secondTracklist) {
		final HashSet<String> set = new HashSet<>(firstTracklist);
		set.retainAll(secondTracklist);
		final int shared = set.size();
		final Set<String> mergedTrackset = new HashSet<>();
		mergedTrackset.addAll(firstTracklist);
		mergedTrackset.addAll(secondTracklist);
		if (mergedTrackset.isEmpty()) {
			return 1.0;
		}
		return (double) shared / mergedTrackset.size();
	}

	private static double levenshteinDistance(final String a, final String b) {
		return 1.0 - (double) StringUtils.getLevenshteinDistance(a.toLowerCase(), b.toLowerCase())
				/ Math.max(a.length(), b.length());
	}

	public static double similarity(final CDRecord firstRecord, final CDRecord secondRecord) {
		final Map<CDSimilarity, Double> similarityMap = CDSimilarityMeasure.getSimilarityOfRecords(firstRecord,
				secondRecord);
		double result = 0.0;
		for (final Entry<CDSimilarity, Double> entry : similarityMap.entrySet()) {
			result += entry.getKey().weight() * entry.getValue();
		}
		return result / CDSimilarity.TOTAL_WEIGHT;
	}

	private static Double yearDistance(final Short year, final Short year2) {
		// TODO default?
		if (year == null && year2 == null) {
			return 1.0;
		}
		if (year == null || year2 == null) {
			return 0.0;
		}
		int diff = 0;
		int max = 0;
		final int n = Math.max(CDSimilarityMeasure.getNumberOfDigits(year),
				CDSimilarityMeasure.getNumberOfDigits(year2));
		for (int i = 0; i < n; i++) {
			final int weight = i + 1;
			max += weight * 9;
			diff += weight
					* Math.abs(CDSimilarityMeasure.getNthDigit(year, i) - CDSimilarityMeasure.getNthDigit(year2, i));
		}
		return 1 - (double) diff / max;
	}

	@Override
	public Double calculateSimilarity(final Map<String, String> firstRecord, final Map<String, String> secondRecord,
			final HashMap<String, String> parameters) {
		return CDSimilarityMeasure.similarity(CDRecordParser.parse(firstRecord), CDRecordParser.parse(secondRecord));
	}

	@Override
	public double getThreshold() {
		return CDSimilarityMeasure.THRESHOLD;
	}
}