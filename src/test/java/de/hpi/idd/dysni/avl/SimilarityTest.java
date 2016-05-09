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
		tree.insert(Foo.F);
		tree.insert(Foo.E);
		Node<String, Foo> node = tree.insert(Foo.CD);
		List<Foo> candidates = node.getContainer().getSimilarCandidates();
		assertTrue(candidates.contains(Foo.CD));
		assertTrue(candidates.contains(Foo.C));
		assertTrue(candidates.contains(Foo.D));
		assertEquals(3, candidates.size());
	}
}
