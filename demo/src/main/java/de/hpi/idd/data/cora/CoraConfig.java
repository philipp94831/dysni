package de.hpi.idd.data.cora;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.KeyHandler;
import de.hpi.idd.dysni.window.AdaptiveKeySimilarityWindowBuilder;
import de.hpi.idd.sim.LevenshteinSimilarity;
import de.hpi.idd.sim.SimilarityMeasure;

public class CoraConfig {
	
	private static final SimilarityMeasure<String> LEVENSHTEIN = new LevenshteinSimilarity();

	public static Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> config() {
		return Arrays.asList(new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

			@Override
			public String computeKey(Map<String, Object> obj) {
				String title = ((String) obj.get("Authors")).toLowerCase();
				String artist = ((String) obj.get("title")).toLowerCase();
				return StringUtils.substring(artist, 0, 3) + StringUtils.substring(title, 0, 3);
			}
		}, new AdaptiveKeySimilarityWindowBuilder<>(LEVENSHTEIN.asClassifier(0.8))),
				new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

					@Override
					public String computeKey(Map<String, Object> obj) {
						String title = ((String) obj.get("title")).toLowerCase();
						String artist = ((String) obj.get("Authors")).toLowerCase();
						return StringUtils.substring(title, 0, 3) + StringUtils.substring(artist, 0, 3);
					}
				}, new AdaptiveKeySimilarityWindowBuilder<>(LEVENSHTEIN.asClassifier(0.6))));
	}

}
