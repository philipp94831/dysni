package de.hpi.idd.data.cd;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import de.hpi.idd.dysni.DySNIndexConfiguration;
import de.hpi.idd.dysni.sim.LevenshteinMetric;
import de.hpi.idd.dysni.sim.SimilarityMeasure;
import de.hpi.idd.dysni.window.AdaptiveKeySimilarityWindowBuilder;

public class CDKeyHandler {

	private static final SimilarityMeasure<String> LEVENSHTEIN = new LevenshteinMetric();

	public static Collection<DySNIndexConfiguration<Map<String, String>, ?, String>> keyHandler() {
		return Arrays.asList(new DySNIndexConfiguration<>(obj -> {
			String title = obj.get("dtitle");
			String artist = obj.get("artist");
			return artist.substring(0, Math.min(3, artist.length())) + title.substring(0, Math.min(3, title.length()));
		}, new AdaptiveKeySimilarityWindowBuilder<>(CDKeyHandler.LEVENSHTEIN.asAssessor(0.8))),
				new DySNIndexConfiguration<>(obj -> {
					String title = obj.get("dtitle");
					String artist = obj.get("artist");
					return title.substring(0, Math.min(3, title.length()))
							+ artist.substring(0, Math.min(3, artist.length()));
				}, new AdaptiveKeySimilarityWindowBuilder<>(CDKeyHandler.LEVENSHTEIN.asAssessor(0.6))));
	}
}