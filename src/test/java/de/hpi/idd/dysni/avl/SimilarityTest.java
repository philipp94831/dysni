package de.hpi.idd.dysni.avl;

import static org.junit.Assert.*;

import java.util.Collection;
import org.junit.Test;

import de.hpi.idd.dysni.comp.LevenshteinComparator;

public class SimilarityTest {

	private final AVLTree<String, Foo> tree = new AVLTree<>(new LevenshteinComparator(0.5));

	@Test
	public void test() {
		tree.insert(Foo.C);
		tree.insert(Foo.A);
		tree.insert(Foo.D);
		tree.insert(Foo.B);
		tree.insert(Foo.E);
		tree.insert(Foo.DE);
		Collection<Foo> candidates = tree.findCandidates(Foo.DE);
		assertTrue(candidates.contains(Foo.D));
		assertTrue(candidates.contains(Foo.E));
		assertEquals(2, candidates.size());
	}
}
