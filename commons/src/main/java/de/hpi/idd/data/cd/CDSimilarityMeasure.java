package de.hpi.idd.data.cd;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.SimilarityMeasure;

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

		public CDRecord(String dId, String artist, String dTitle, String category, String genre, String cdExtra,
				short year, List<String> tracks) {
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

		public void setArtist(String artist) {
			this.artist = artist;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public void setCdExtra(String cdExtra) {
			this.cdExtra = cdExtra;
		}

		public void setdId(String dId) {
			this.dId = dId;
		}

		public void setdTitle(String dTitle) {
			this.dTitle = dTitle;
		}

		public void setGenre(String genre) {
			this.genre = genre;
		}

		public void setTracks(List<String> tracks) {
			this.tracks = tracks;
		}

		public void setYear(short year) {
			this.year = year;
		}

		@Override
		public String toString() {
			return dId;
		}
	}

	private static class CDRecordParser {

		private static final Map<String, CDRecord> CACHE = new HashMap<>();

		public static CDRecord parse(Map<String, String> record) {
			String id = record.get("id");
			CDRecord cd = CACHE.get(id);
			if (cd == null) {
				cd = new CDRecord();
				cd.setdId(id);
				cd.setArtist(record.get("artist"));
				cd.setdTitle(record.get("dtitle"));
				cd.setCategory(record.get("category"));
				String year = record.get("year");
				if (!year.isEmpty()) {
					cd.setYear(Short.parseShort(year));
				}
				cd.setGenre(record.get("genre"));
				cd.setCdExtra(record.get("cdextra"));
				cd.setTracks(
						Arrays.asList(record.get("tracks").split("\\|")).stream().map(CDSimilarityMeasure::trimNumbers)
								.map(CDSimilarityMeasure::normalize).collect(Collectors.toList()));
				CACHE.put(id, cd);
			}
			return cd;
		}
	}

	public enum CDSimilarity {
		ARTIST(5), CATEGORY, CDEXTRA(0), DTITLE(4), GENRE, TRACK(3), YEAR;

		private static final double TOTAL_WEIGHT = Arrays.stream(CDSimilarity.values())
				.mapToDouble(CDSimilarity::weight).sum();
		private final double weight;

		CDSimilarity() {
			this(1);
		}

		CDSimilarity(double weight) {
			this.weight = weight;
		}

		public double weight() {
			return weight;
		}
	}

	private static final double THRESHOLD = 0.7;

	private static int getNthDigit(int number, int n) {
		return (int) (Math.abs(number) / Math.pow(10, n) % 10);
	}

	private static int getNumberOfDigits(int number) {
		return (int) (Math.log10(Math.abs(number)) + 1);
	}

	public static Map<CDSimilarity, Double> getSimilarityOfRecords(CDRecord firstRecord, CDRecord secondRecord) {
		Map<CDSimilarity, Double> similarityMap = new HashMap<>();
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

	private static double getSimilarityOfTracks(List<String> firstTracklist, List<String> secondTracklist) {
		HashSet<String> set = new HashSet<>(firstTracklist);
		set.retainAll(secondTracklist);
		int shared = set.size();
		Set<String> mergedTrackset = new HashSet<>();
		mergedTrackset.addAll(firstTracklist);
		mergedTrackset.addAll(secondTracklist);
		if (mergedTrackset.isEmpty()) {
			return 1.0;
		}
		return (double) shared / mergedTrackset.size();
	}

	private static double levenshteinDistance(String a, String b) {
		if (a.isEmpty() || b.isEmpty()) {
			return THRESHOLD;
		}
		return 1.0 - (double) StringUtils.getLevenshteinDistance(a.toLowerCase(), b.toLowerCase())
				/ Math.max(a.length(), b.length());
	}

	private static String normalize(String s) {
		s = s.toLowerCase();
		s = Normalizer.normalize(s, Normalizer.Form.NFD);
		s = s.replaceAll("\\p{M}", "");
		s = s.trim();
		return s;
	}

	public static double similarity(CDRecord firstRecord, CDRecord secondRecord) {
		Map<CDSimilarity, Double> similarityMap = CDSimilarityMeasure.getSimilarityOfRecords(firstRecord, secondRecord);
		double result = 0.0;
		for (Entry<CDSimilarity, Double> entry : similarityMap.entrySet()) {
			result += entry.getKey().weight() * entry.getValue();
		}
		return result / CDSimilarity.TOTAL_WEIGHT;
	}

	private static String trimNumbers(String s) {
		s = s.replaceAll("^\\d+\\s+", "");
		return s;
	}

	private static Double yearDistance(Short year, Short year2) {
		if (year == null || year2 == null) {
			return THRESHOLD;
		}
		int diff = 0;
		int max = 0;
		int n = Math.max(CDSimilarityMeasure.getNumberOfDigits(year), CDSimilarityMeasure.getNumberOfDigits(year2));
		for (int i = 0; i < n; i++) {
			int weight = i + 1;
			max += weight * 9;
			diff += weight
					* Math.abs(CDSimilarityMeasure.getNthDigit(year, i) - CDSimilarityMeasure.getNthDigit(year2, i));
		}
		return 1 - (double) diff / max;
	}

	@Override
	public Double calculateSimilarity(Map<String, String> firstRecord, Map<String, String> secondRecord,
			HashMap<String, String> parameters) {
		return CDSimilarityMeasure.similarity(CDRecordParser.parse(firstRecord), CDRecordParser.parse(secondRecord));
	}

	@Override
	public double getThreshold() {
		return THRESHOLD;
	}
}
