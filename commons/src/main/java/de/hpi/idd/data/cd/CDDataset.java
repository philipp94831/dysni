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

import de.hpi.idd.DatasetUtils;

public class CDDataset extends DatasetUtils {

	static class CDRecord {

		private String artist;
		private String category;
		private String cdExtra;
		private String genre;
		private String id;
		private String title;
		private List<String> tracks;
		private Short year;

		public CDRecord() {
		}

		public CDRecord(String id, String artist, String title, String category, String genre, String cdExtra,
				short year, List<String> tracks) {
			this.id = id;
			this.artist = artist;
			this.title = title;
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

		public String getGenre() {
			return genre;
		}

		public String getId() {
			return id;
		}

		public String getTitle() {
			return title;
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

		public void setGenre(String genre) {
			this.genre = genre;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setTracks(List<String> tracks) {
			this.tracks = tracks;
		}

		public void setYear(short year) {
			this.year = year;
		}

		@Override
		public String toString() {
			return id;
		}
	}

	private static class CDRecordParser {

		@SuppressWarnings("unchecked")
		public static CDRecord parse(Map<String, Object> record) {
			CDRecord cd = new CDRecord();
			cd.setId((String) record.get(ID));
			cd.setArtist((String) record.get(ARTIST));
			cd.setTitle((String) record.get(TITLE));
			cd.setCategory((String) record.get(CATEGORY));
			Object year = record.get(YEAR);
			if (year != null) {
				cd.setYear((Short) year);
			}
			cd.setGenre((String) record.get(GENRE));
			cd.setCdExtra((String) record.get(CDEXTRA));
			cd.setTracks((List<String>) record.get(TRACKS));
			return cd;
		}
	}

	public enum CDSimilarity {
		ARTIST(5), CATEGORY, CDEXTRA(0), GENRE, TITLE(4), TRACK(3), YEAR;

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

	private static final String ARTIST = "artist";
	private static final String CATEGORY = "category";
	private static final String CDEXTRA = "cdextra";
	private static final String GENRE = "genre";
	private static final String ID = "id";
	private static final String SEPERATOR = "\\|";
	private static final double THRESHOLD = 0.7;
	private static final String TITLE = "dtitle";
	private static final String TRACKS = "tracks";
	private static final String YEAR = "year";

	private static int getNthDigit(int number, int n) {
		return (int) (Math.abs(number) / Math.pow(10, n) % 10);
	}

	private static int getNumberOfDigits(int number) {
		return (int) (Math.log10(Math.abs(number)) + 1);
	}

	public static Map<CDSimilarity, Double> getSimilarityOfRecords(CDRecord firstRecord, CDRecord secondRecord) {
		Map<CDSimilarity, Double> similarityMap = new HashMap<>();
		similarityMap.put(CDSimilarity.ARTIST,
				CDDataset.levenshteinDistance(firstRecord.getArtist(), secondRecord.getArtist()));
		similarityMap.put(CDSimilarity.TITLE,
				CDDataset.levenshteinDistance(firstRecord.getTitle(), secondRecord.getTitle()));
		similarityMap.put(CDSimilarity.CATEGORY,
				CDDataset.levenshteinDistance(firstRecord.getCategory(), secondRecord.getCategory()));
		similarityMap.put(CDSimilarity.GENRE,
				CDDataset.levenshteinDistance(firstRecord.getGenre(), secondRecord.getGenre()));
		similarityMap.put(CDSimilarity.YEAR, CDDataset.yearDistance(firstRecord.getYear(), secondRecord.getYear()));
		similarityMap.put(CDSimilarity.CDEXTRA,
				CDDataset.levenshteinDistance(firstRecord.getCdExtra(), secondRecord.getCdExtra()));
		similarityMap.put(CDSimilarity.TRACK,
				CDDataset.getSimilarityOfTracks(firstRecord.getTracks(), secondRecord.getTracks()));
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
		Map<CDSimilarity, Double> similarityMap = CDDataset.getSimilarityOfRecords(firstRecord, secondRecord);
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
		int n = Math.max(CDDataset.getNumberOfDigits(year), CDDataset.getNumberOfDigits(year2));
		for (int i = 0; i < n; i++) {
			int weight = i + 1;
			max += weight * 9;
			diff += weight * Math.abs(CDDataset.getNthDigit(year, i) - CDDataset.getNthDigit(year2, i));
		}
		return 1 - (double) diff / max;
	}

	public CDDataset() {
		datasetThreshold = THRESHOLD;
	}

	/**
	 * Brute-force results:
	 * 
	 * <ul>
	 * <li>Recall: 0.8304</li>
	 * <li>Precision: 0.8451</li>
	 * <li>F-Measure: 0.8377</li>
	 * </ul>
	 */
	@Override
	public Double calculateSimilarity(Map<String, Object> firstRecord, Map<String, Object> secondRecord,
			Map<String, String> parameters) {
		return CDDataset.similarity(CDRecordParser.parse(firstRecord), CDRecordParser.parse(secondRecord));
	}

	@Override
	public Map<String, Object> parseRecord(Map<String, String> value) {
		Map<String, Object> record = new HashMap<>();
		record.put(ID, value.get(ID));
		record.put(ARTIST, value.get(ARTIST));
		record.put(TITLE, value.get(TITLE));
		record.put(CATEGORY, value.get(CATEGORY));
		String year = value.get(YEAR);
		if (!year.isEmpty()) {
			record.put(YEAR, Short.parseShort(year));
		}
		record.put(GENRE, value.get(GENRE));
		record.put(CDEXTRA, value.get(CDEXTRA));
		record.put(TRACKS, Arrays.asList(value.get(TRACKS).split(SEPERATOR)).stream().map(CDDataset::trimNumbers)
				.map(CDDataset::normalize).collect(Collectors.toList()));
		return record;
	}
}
