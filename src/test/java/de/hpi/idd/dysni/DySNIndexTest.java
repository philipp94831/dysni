package de.hpi.idd.dysni;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.idd.dysni.sim.LevenshteinSimilarity;
import de.hpi.idd.dysni.sim.SimilarityMeasure;
import de.hpi.idd.dysni.window.AdaptiveKeySimilarityWindowBuilder;

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
					new AdaptiveKeySimilarityWindowBuilder<>(DySNIndexTest.LEVENSHTEIN.asAssessor(0.5))));

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
		Assert.assertTrue(candidates.contains("d"));
		Assert.assertTrue(candidates.contains("e"));
		Assert.assertTrue(candidates.contains("de"));
		Assert.assertEquals(3, candidates.size());
	}
}
