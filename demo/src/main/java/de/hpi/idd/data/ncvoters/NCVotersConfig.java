package de.hpi.idd.data.ncvoters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.KeyHandler;
import de.hpi.idd.dysni.window.AdaptiveKeySimilarityWindowBuilder;
import de.hpi.idd.sim.LevenshteinSimilarity;
import de.hpi.idd.sim.SimilarityMeasure;

public class NCVotersConfig {
	
	private static final SimilarityMeasure<String> LEVENSHTEIN = new LevenshteinSimilarity();

	public static Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> config() {
		return Arrays.asList(new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

			@Override
			public String computeKey(Map<String, Object> obj) {
				String writer = ((String) obj.get("last_name")).toLowerCase();
				String artist = ((String) obj.get("first_name")).toLowerCase();
				return StringUtils.substring(artist, 0, 7) + StringUtils.substring(writer, 0, 7);
			}
		}, new AdaptiveKeySimilarityWindowBuilder<>(LEVENSHTEIN.asClassifier(0.8))),
				new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

					@Override
					public String computeKey(Map<String, Object> obj) {
						String title = ((String) obj.get("first_name")).toLowerCase();
						String writer = ((String) obj.get("last_name")).toLowerCase();
						return StringUtils.substring(title, 0, 7) + StringUtils.substring(writer, 0, 7);
					}
				}, new AdaptiveKeySimilarityWindowBuilder<>(LEVENSHTEIN.asClassifier(0.8))));
	}

}
