package de.hpi.idd.data.cd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import de.hpi.idd.dysni.KeyHandler;
import de.hpi.idd.dysni.sim.LevenshteinMetric;
import de.hpi.idd.dysni.sim.SimilarityMeasure;

public class CDKeyHandler {

	private static final LevenshteinMetric LEVENSHTEIN = new LevenshteinMetric();

	public static Collection<KeyHandler<Map<String, String>, ?>> keyHandler() {
		return Arrays.asList(new KeyHandler<Map<String, String>, String>() {

			@Override
			public String computeKey(final Map<String, String> obj) {
				final String title = obj.get("dtitle");
				final String artist = obj.get("artist");
				return artist.substring(0, Math.min(3, artist.length()))
						+ title.substring(0, Math.min(3, title.length()));
			}

			@Override
			public SimilarityMeasure<String> getSimilarityMeasure() {
				return CDKeyHandler.LEVENSHTEIN;
			}

			@Override
			public double getThreshold() {
				return 0.8;
			}
		}, new KeyHandler<Map<String, String>, String>() {

			@Override
			public String computeKey(final Map<String, String> obj) {
				final String title = obj.get("dtitle");
				final String artist = obj.get("artist");
				return title.substring(0, Math.min(3, title.length()))
						+ artist.substring(0, Math.min(3, artist.length()));
			}

			@Override
			public SimilarityMeasure<String> getSimilarityMeasure() {
				return CDKeyHandler.LEVENSHTEIN;
			}

			@Override
			public double getThreshold() {
				return 0.6;
			}
		});
	}
}