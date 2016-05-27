package de.hpi.idd.dysni.avl;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.idd.dysni.comp.LevenshteinComparator;
import de.hpi.idd.dysni.simavl.SimilarityAwareAVLTree;

public class SimilarityTest {

	private final SimilarityAwareAVLTree<String, Foo> tree = new SimilarityAwareAVLTree<>(
			new LevenshteinComparator(0.5));

	@Test
	public void test() {
		tree.insert(Foo.C.getKey(), Foo.C);
		tree.insert(Foo.A.getKey(), Foo.A);
		tree.insert(Foo.D.getKey(), Foo.D);
		tree.insert(Foo.B.getKey(), Foo.B);
		tree.insert(Foo.E.getKey(), Foo.E);
		tree.insert(Foo.DE.getKey(), Foo.DE);
		final Collection<Foo> candidates = tree.findCandidates(Foo.DE.getKey(), Foo.DE);
		Assert.assertTrue(candidates.contains(Foo.D));
		Assert.assertTrue(candidates.contains(Foo.E));
		Assert.assertEquals(2, candidates.size());
	}
}
