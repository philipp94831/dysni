package de.hpi.idd.data.movies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import de.hpi.idd.DatasetUtils;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class MovieSimilarityMeasure extends DatasetUtils {

	private NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();

	public MovieSimilarityMeasure() {
		this.datasetThreshold = 0.93;
	}

	public static double subsetSim(Collection<? extends Object> c1, Collection<? extends Object> c2) {
		HashSet<Object> intersection = new HashSet<Object>(c1);
		intersection.retainAll(c2);
		int minsize = c1.size() < c2.size() ? c1.size() : c2.size();
		if (minsize == 0)
			return 0.0;
		return (double) intersection.size() / (double) minsize;
	}

	@Override
	public Double calculateSimilarity(Map<String, Object> r1, Map<String, Object> r2, Map<String, String> parameters) {

		double titlesim = getTitleSimilarity((String) r1.get("title"), (String) r2.get("title"));
		double actorssim = getActorsSimilarity((String[]) r1.get("actors"), (String[]) r2.get("actors"));

		return actorssim == 0.0 ? 0.0 : titlesim;

	}

	private double getTitleSimilarity(String s1, String s2) {
		if (!checkSequels(s1, s2))
			return 0;

		s1 = removeSpecialCharacters(s1);
		s2 = removeSpecialCharacters(s2);
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		return getTitlePartSimilarity(s1, s2);
	}

	private double getTitlePartSimilarity(String s1, String s2) {
		String[] s2parts = s2.split("\\|");
		String[] s1parts = s1.split("\\|");
		if (s1parts.length > 1 && s2parts.length > 1)
			return 0.0;
		double result = 0;
		double sim;
		for (String s1part : s1parts) {
			if (s1part.contains("award") || s1part.contains("serial") || s1part.contains("prix")
					|| s1part.contains("films"))
				continue;
			for (String s2part : s2parts) {
				if (s2part.contains("award") || s2part.contains("serial") || s2part.contains("prix")
						|| s2part.contains("films"))
					continue;
				sim = levenshtein.similarity(reorderArticle(removePunctuationMarks(s1part)),
						reorderArticle(removePunctuationMarks(s2part)));
				if (sim > result)
					result = sim;
			}
		}
		return result;
	}

	private String reorderArticle(String s) {
		int x = s.lastIndexOf(' ');
		if (x < 2)
			return s;
		if (s.charAt(x - 1) != ',')
			return s;
		String article = s.substring(x + 1);
		if (article.length() > 3)
			return s;
		if (article.endsWith("."))
			return s;
		if (article.equals("het"))
			return s;
		// Bei Apostroph L' kein Leerzeichen!
		if (article.equals("l\'"))
			return article + s.substring(0, x - 1);
		return article + " " + s.substring(0, x - 1);
	}

	private String removeSpecialCharacters(String s1) {
		String result = s1.replace("�", "");
		result = result.replace("0101", "");
		if (!result.contains("00"))
			return result;
		result = result.replace("00ED", "");
		result = result.replace("00EE", "");
		result = result.replace("00E8", "");
		result = result.replace("00E9", "");
		result = result.replace("00F6", "");
		result = result.replace("00F5", "");
		result = result.replace("00F3", "");

		return result;
	}

	private boolean checkSequels(String s1, String s2) {
		return findSequel(s1) == findSequel(s2);
	}

	private int findSequel(String s) {
		int lastblanc1 = s.lastIndexOf(' ');
		if (lastblanc1 > 2)
			return parseNumber(s.substring(s.lastIndexOf(' ') + 1));
		return -1;
	}

	private int parseNumber(String s) {
		int result;
		// decimal numbers
		try {
			result = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			result = -1;
		}
		if (result > 0)
			return result;
		// roman numbers

		if (s.equals("I"))
			return 1;
		if (s.equals("II"))
			return 2;
		if (s.equals("III"))
			return 3;
		if (s.equals("IV"))
			return 4;
		if (s.equals("V"))
			return 5;
		if (s.equals("VI"))
			return 6;
		if (s.equals("VII"))
			return 7;
		if (s.equals("VIII"))
			return 8;
		if (s.equals("IX"))
			return 9;
		if (s.equals("X"))
			return 10;
		if (s.equals("XI"))
			return 11;
		if (s.equals("XII"))
			return 12;
		if (s.equals("XIII"))
			return 13;
		if (s.equals("XIV"))
			return 14;
		if (s.equals("XV"))
			return 15;
		if (s.equals("XVI"))
			return 16;
		if (s.equals("XVII"))
			return 17;
		if (s.equals("XVIII"))
			return 18;
		if (s.equals("XIX"))
			return 19;
		if (s.equals("XX"))
			return 20;

		return -1;

	}

	private String removePunctuationMarks(String s) {
		while (s.endsWith(".") || s.endsWith("!") || s.endsWith("?"))
			s = s.substring(0, s.length() - 1);
		return s;
	}

	private double getActorsSimilarity(String[] actors1, String[] actors2) {

		if (actors1.length == 0 || actors2.length == 0)
			return -1;
		return subsetSim(Arrays.asList(actors1), Arrays.asList(actors2));

	}

	public static Map<String, Object> parseRecord(String value) {
		String[] rec = value.split("\"");
		String id = "";
		if (rec.length > 1)
			id = rec[1];

		Map<String, Object> record = new HashMap<>();
		record.put("record_id", Integer.parseInt(id));
		record.put("title", rec[3]);
		if (rec.length < 6) {
			String[] actors = {};
			record.put("actors", actors);
		} else {
			record.put("actors", rec[5].split("\\|"));
		}
		return record;
	}

	@Override
	public Map<String, Object> parseRecord(Map<String, String> values) {
		Map<String, Object> record = new HashMap<>();
		for (Entry<String, String> entry : values.entrySet()) {
			switch (entry.getKey()) {
			case "id":
				record.put("id", entry.getValue());
				break;
			case "title":
				record.put(entry.getKey(), entry.getValue());
				break;
			case "actors":
				record.put(entry.getKey(), entry.getValue().split("\\|"));
				break;

			}
		}
		return record;
	}

	@Override
	public Double compareAttributeValue(String attribute, Object value1, Object value2) {
		switch (attribute) {
		case "title":
			return getTitleSimilarity((String) value1, (String) value2);
		case "actors":
			return getActorsSimilarity((String[]) value1, (String[]) value2);
		}
		return 0.0;
	}

	@Override
	public Boolean isMatch(Map<String, Double> similarities) {
		// TODO Auto-generated method stub
		return null;
	}
}
