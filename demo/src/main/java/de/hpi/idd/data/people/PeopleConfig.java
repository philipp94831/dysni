package de.hpi.idd.data.people;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.KeyHandler;
import de.hpi.idd.dysni.window.AdaptiveKeySimilarityWindowBuilder;
import de.hpi.idd.sim.LevenshteinSimilarity;
import de.hpi.idd.sim.SimilarityMeasure;

public class PeopleConfig {
	
	private static final SimilarityMeasure<String> LEVENSHTEIN = new LevenshteinSimilarity();

	public static Collection<DySNIndexConfiguration<Map<String, Object>, ?, String>> config() {
		return Arrays.asList(new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

			@Override
			public String computeKey(Map<String, Object> obj) {
				String givenName = ((String) obj.get("given_name")).toLowerCase();
				String surname = ((String) obj.get("surname")).toLowerCase();
				return StringUtils.substring(surname, 0, 4) + StringUtils.substring(givenName, 0, 4);
			}
		}, new AdaptiveKeySimilarityWindowBuilder<>(LEVENSHTEIN.asClassifier(0.6))),
				new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

					@Override
					public String computeKey(Map<String, Object> obj) {
						String surname = ((String) obj.get("surname")).toLowerCase();
						String givenName = ((String) obj.get("given_name")).toLowerCase();
						return StringUtils.substring(surname, 0, 4) + StringUtils.substring(givenName, 0, 4);
					}
				}, new AdaptiveKeySimilarityWindowBuilder<>(LEVENSHTEIN.asClassifier(0.6))),
				new DySNIndexConfiguration<>(new KeyHandler<Map<String, Object>, String>() {

					@Override
					public String computeKey(Map<String, Object> obj) {
						String surname = ((String) obj.get("surname")).toLowerCase();
						String suburb = ((String) obj.get("suburb")).toLowerCase();
						return StringUtils.substring(suburb, 0, 4) + StringUtils.substring(surname, 0, 4);
					}
				}, new AdaptiveKeySimilarityWindowBuilder<>(LEVENSHTEIN.asClassifier(0.6))));
	}
}
