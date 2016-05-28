package de.hpi.idd.dysni;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.idd.dysni.key.KeyComparator;
import de.hpi.idd.dysni.key.KeyHandler;
import de.hpi.idd.dysni.key.LevenshteinComparator;

public class DisnyIndexTest {

	private static class StringKeyHandler implements KeyHandler<String, String> {

		@Override
		public String computeKey(final String s) {
			return s;
		}

		@Override
		public KeyComparator<String> getComparator() {
			return DisnyIndexTest.COMPARATOR;
		}
	}

	private static final KeyComparator<String> COMPARATOR = new LevenshteinComparator(0.5);
	private final DysniIndex<String, String, String> index = new DysniIndex<>(new StringKeyHandler());

	private void insert(final String s) {
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
		final Collection<String> candidates = index.findCandidates("DE");
		Assert.assertTrue(candidates.contains("d"));
		Assert.assertTrue(candidates.contains("e"));
		Assert.assertTrue(candidates.contains("de"));
		Assert.assertEquals(3, candidates.size());
	}
}
