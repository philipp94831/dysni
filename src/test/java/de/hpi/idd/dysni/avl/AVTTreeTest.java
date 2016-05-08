package de.hpi.idd.dysni.avl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class AVTTreeTest {

	private static enum Foo implements Element<String> {
		A("a"), B("b"), C("c"), D("d"), E("e"), F("f"), G("g"), H("h"), I("i");

		private final String skv;

		private Foo(final String skv) {
			this.skv = skv;
		}

		@Override
		public String getSKV() {
			return skv;
		}
	}

	private final AVLTree<String, Foo> tree = new AVLTree<>();

	@Test
	public void testIterator() {
		tree.insert(Foo.D);
		tree.insert(Foo.F);
		tree.insert(Foo.C);
		tree.insert(Foo.E);
		tree.insert(Foo.A);
		tree.insert(Foo.B);
		final Iterator<AVLTree<String, Foo>.Node> it = tree.iterator();
		assertEquals(it.next().getSKV(), "a");
		assertEquals(it.next().getSKV(), "b");
		assertEquals(it.next().getSKV(), "c");
		assertEquals(it.next().getSKV(), "d");
		assertEquals(it.next().getSKV(), "e");
		assertEquals(it.next().getSKV(), "f");
		assertFalse(it.hasNext());
	}

	@Test
	public void testLeftRotation() {
		tree.insert(Foo.G);
		tree.insert(Foo.I);
		tree.insert(Foo.E);
		tree.insert(Foo.H);
		tree.insert(Foo.F);
		tree.insert(Foo.C);
		tree.insert(Foo.D);
		tree.insert(Foo.B);
		tree.insert(Foo.A);
		final AVLTree<String, Foo>.Node g = tree.getRoot();
		assertEquals(g.getSKV(), "g");
		final AVLTree<String, Foo>.Node f = g.getPrevious();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getNext().getSKV(), "g");
		final AVLTree<String, Foo>.Node e = f.getPrevious();
		assertEquals(e.getSKV(), "e");
		assertEquals(e.getNext().getSKV(), "f");
		final AVLTree<String, Foo>.Node d = e.getPrevious();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getNext().getSKV(), "e");
		final AVLTree<String, Foo>.Node c = d.getPrevious();
		assertEquals(c.getSKV(), "c");
		assertEquals(c.getNext().getSKV(), "d");
		final AVLTree<String, Foo>.Node b = c.getPrevious();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getNext().getSKV(), "c");
		final AVLTree<String, Foo>.Node a = b.getPrevious();
		assertEquals(a.getSKV(), "a");
		assertEquals(a.getNext().getSKV(), "b");
		assertNull(a.getPrevious());
		final AVLTree<String, Foo>.Node h = g.getNext();
		assertEquals(h.getSKV(), "h");
		assertEquals(h.getPrevious().getSKV(), "g");
		final AVLTree<String, Foo>.Node i = h.getNext();
		assertEquals(i.getSKV(), "i");
		assertEquals(i.getPrevious().getSKV(), "h");
		assertNull(i.getNext());
	}

	@Test
	public void testRightRotation() {
		tree.insert(Foo.C);
		tree.insert(Foo.A);
		tree.insert(Foo.E);
		tree.insert(Foo.B);
		tree.insert(Foo.D);
		tree.insert(Foo.G);
		tree.insert(Foo.F);
		tree.insert(Foo.H);
		tree.insert(Foo.I);
		final AVLTree<String, Foo>.Node c = tree.getRoot();
		assertEquals(c.getSKV(), "c");
		final AVLTree<String, Foo>.Node d = c.getNext();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		final AVLTree<String, Foo>.Node e = d.getNext();
		assertEquals(e.getSKV(), "e");
		assertEquals(e.getPrevious().getSKV(), "d");
		final AVLTree<String, Foo>.Node f = e.getNext();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "e");
		final AVLTree<String, Foo>.Node g = f.getNext();
		assertEquals(g.getSKV(), "g");
		assertEquals(g.getPrevious().getSKV(), "f");
		final AVLTree<String, Foo>.Node h = g.getNext();
		assertEquals(h.getSKV(), "h");
		assertEquals(h.getPrevious().getSKV(), "g");
		final AVLTree<String, Foo>.Node i = h.getNext();
		assertEquals(i.getSKV(), "i");
		assertEquals(i.getPrevious().getSKV(), "h");
		assertNull(i.getNext());
		final AVLTree<String, Foo>.Node b = c.getPrevious();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getNext().getSKV(), "c");
		final AVLTree<String, Foo>.Node a = b.getPrevious();
		assertEquals(a.getSKV(), "a");
		assertEquals(a.getNext().getSKV(), "b");
		assertNull(a.getPrevious());
	}

	@Test
	public void testDeletion() {
		tree.insert(Foo.C);
		tree.insert(Foo.A);
		tree.insert(Foo.D);
		tree.insert(Foo.B);
		tree.insert(Foo.F);
		tree.insert(Foo.E);
		tree.delete(Foo.A);
		tree.delete(Foo.E);
		final AVLTree<String, Foo>.Node c = tree.getRoot();
		assertEquals(c.getSKV(), "c");
		final AVLTree<String, Foo>.Node d = c.getNext();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		final AVLTree<String, Foo>.Node f = d.getNext();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "d");
		assertNull(f.getNext());
		final AVLTree<String, Foo>.Node b = c.getPrevious();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getNext().getSKV(), "c");
		assertNull(b.getPrevious());
	}

	@Test
	public void testDeletionWihtMultipleElements() {
		tree.insert(Foo.A);
		tree.insert(Foo.A);
		tree.delete(Foo.A);
		assertFalse(tree.isEmpty());
		final AVLTree<String, Foo>.Node a = tree.getRoot();
		assertEquals(a.getSKV(), "a");
		assertNull(a.getNext());
		assertNull(a.getPrevious());
		tree.delete(Foo.A);
		assertTrue(tree.isEmpty());
	}
}
