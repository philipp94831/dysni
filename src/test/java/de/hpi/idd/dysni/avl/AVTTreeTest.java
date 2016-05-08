package de.hpi.idd.dysni.avl;

import static org.junit.Assert.*;

import org.junit.Test;

import de.hpi.idd.dysni.avl.AVLTree.Node;

public class AVTTreeTest {
	
	private AVLTree<String, Foo> tree = new AVLTree<>();

	@Test
	public void testRightRotation() {
		tree.insert(Foo.C);
		tree.insert(Foo.A);
		tree.insert(Foo.D);
		tree.insert(Foo.B);
		tree.insert(Foo.F);
		tree.insert(Foo.E);
		Node c = tree.getTop();
		assertEquals(c.getSKV(), "c");
		Node d = c.getNext();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		Node e = d.getNext();
		assertEquals(e.getSKV(), "e");
		assertEquals(e.getPrevious().getSKV(), "d");
		Node f = e.getNext();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "e");
		assertNull(f.getNext());
		Node b = c.getPrevious();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getNext().getSKV(), "c");
		Node a = b.getPrevious();
		assertEquals(a.getSKV(), "a");
		assertEquals(a.getNext().getSKV(), "b");
		assertNull(a.getPrevious());
	}

	private static enum Foo implements Element<String> {
		
		A("a"), B("b"), C("c"), D("d"), E("e"), F("f");

		private final String skv;

		private Foo(String skv) {
			this.skv = skv;
		}

		@Override
		public String getSKV() {
			return skv;
		}
	}
}
