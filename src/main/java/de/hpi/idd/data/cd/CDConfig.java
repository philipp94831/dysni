package de.hpi.idd.data.cd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.KeyHandler;
import de.hpi.idd.dysni.sim.LevenshteinSimilarity;
import de.hpi.idd.dysni.sim.SimilarityMeasure;
import de.hpi.idd.dysni.window.AdaptiveKeySimilarityWindowBuilder;

public class CDConfig {

	private static final SimilarityMeasure<String> LEVENSHTEIN = new LevenshteinSimilarity();

	public static Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> config() {
		return Arrays.asList(new DySNIndexConfiguration<>(new KeyHandler<Map<String, String>, String>() {

			@Override
			public String computeKey(Map<String, String> obj) {
				String title = obj.get("dtitle").toLowerCase();
				String artist = obj.get("artist").toLowerCase();
				return artist.substring(0, Math.min(3, artist.length()))
						+ title.substring(0, Math.min(3, title.length()));
			}
		}, new AdaptiveKeySimilarityWindowBuilder<>(CDConfig.LEVENSHTEIN.asAssessor(0.8))),
				new DySNIndexConfiguration<>(new KeyHandler<Map<String, String>, String>() {

					@Override
					public String computeKey(Map<String, String> obj) {
						String title = obj.get("dtitle").toLowerCase();
						String artist = obj.get("artist").toLowerCase();
						return title.substring(0, Math.min(3, title.length()))
								+ artist.substring(0, Math.min(3, artist.length()));
					}
				}, new AdaptiveKeySimilarityWindowBuilder<>(CDConfig.LEVENSHTEIN.asAssessor(0.6))));
	}
}