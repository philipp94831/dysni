package de.hpi.idd.dysni;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

import de.hpi.idd.dysni.window.AdaptiveKeySimilarityWindowBuilder;
import de.hpi.idd.sim.LevenshteinSimilarity;
import de.hpi.idd.sim.SimilarityMeasure;

public class DySNIndexTest {

	private static class StringKeyHandler implements KeyHandler<String, String> {

		@Override
		public String computeKey(String s) {
			return s;
		}
	}

	private static final SimilarityMeasure<String> LEVENSHTEIN = new LevenshteinSimilarity();
	private final DySNIndex<String, String, String> index = new DySNIndex<>(
			new DySNIndexConfiguration<>(new StringKeyHandler(),
					new AdaptiveKeySimilarityWindowBuilder<>(DySNIndexTest.LEVENSHTEIN.asClassifier(0.5))));

	private void insert(String s) {
		index.insert(s, s.toLowerCase());
	}

	@Test
	public void test() {
		insert("C");
		insert("A");
		insert("D");
		insert("B");
		insert("E");
		insert("DE");
		Collection<String> candidates = index.findCandidates("DE");
		assertTrue(candidates.contains("d"));
		assertTrue(candidates.contains("e"));
		assertTrue(candidates.contains("de"));
		assertEquals(3, candidates.size());
	}
}
