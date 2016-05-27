package de.hpi.idd.dysni;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.idd.dysni.comp.LevenshteinComparator;

public class DisnyIndexTest {

	private static class FooKeyHandler implements KeyHandler<Foo, String> {

		@Override
		public String computeKey(final Foo foo) {
			return foo.name();
		}

		@Override
		public KeyComparator<String> getComparator() {
			return DisnyIndexTest.COMPARATOR;
		}

	}

	private static final KeyComparator<String> COMPARATOR = new LevenshteinComparator(0.5);

	private final DysniIndex<Foo, String, Foo> index = new DysniIndex<>(new FooKeyHandler());

	private void insert(final Foo foo) {
		index.insert(foo, foo);
	}

	@Test
	public void test() {
		insert(Foo.C);
		insert(Foo.A);
		insert(Foo.D);
		insert(Foo.B);
		insert(Foo.E);
		insert(Foo.DE);
		final Collection<Foo> candidates = index.findCandidates(Foo.DE, Foo.DE);
		Assert.assertTrue(candidates.contains(Foo.D));
		Assert.assertTrue(candidates.contains(Foo.E));
		Assert.assertEquals(2, candidates.size());
	}
}
