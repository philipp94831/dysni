package de.hpi.idd.dysni;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.idd.dysni.sim.DefaultAssessor;
import de.hpi.idd.dysni.sim.LevenshteinMetric;
import de.hpi.idd.dysni.sim.SimilarityAssessor;
import de.hpi.idd.dysni.sim.SimilarityMeasure;

public class DySNIndexTest {

	private static class StringKeyHandler implements KeyHandler<String, String> {

		@Override
		public String computeKey(String s) {
			return s;
		}

		@Override
		public SimilarityAssessor<String> getSimilarityMeasure() {
			return new DefaultAssessor<>(DySNIndexTest.LEVENSHTEIN, 0.5);
		}
	}

	private static final SimilarityMeasure<String> LEVENSHTEIN = new LevenshteinMetric();
	private final DySNIndex<String, String, String> index = new DySNIndex<>(new StringKeyHandler());

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
