package de.hpi.idd.dysni.avl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SimilarityTest {

	private final AVLTree<String, Foo> tree = new AVLTree<>();

	@Test
	public void test() {
		tree.insert(Foo.C);
		tree.insert(Foo.A);
		tree.insert(Foo.D);
		tree.insert(Foo.B);
		tree.insert(Foo.E);
		List<Foo> candidates = tree.insert(Foo.DE);
		assertTrue(candidates.contains(Foo.D));
		assertTrue(candidates.contains(Foo.E));
		assertEquals(2, candidates.size());
	}
}
