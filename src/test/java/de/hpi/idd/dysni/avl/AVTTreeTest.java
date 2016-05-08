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
	public void testDeletion() {
		tree.insert(Foo.C);
		tree.insert(Foo.A);
		tree.insert(Foo.D);
		tree.insert(Foo.B);
		tree.insert(Foo.F);
		tree.insert(Foo.E);
		tree.delete(Foo.A);
		tree.delete(Foo.E);
		final Node<String, Foo> c = tree.getRoot();
		assertEquals(c.getSKV(), "c");
		assertEquals(c.getPrevious().getSKV(), "b");
		assertEquals(c.getNext().getSKV(), "d");
		final Node<String, Foo> d = c.getRight();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		assertEquals(d.getNext().getSKV(), "f");
		assertNull(d.getLeft());
		final Node<String, Foo> f = d.getRight();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "d");
		assertNull(f.getNext());
		assertNull(f.getLeft());
		assertNull(f.getRight());
		final Node<String, Foo> b = c.getLeft();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getNext().getSKV(), "c");
		assertNull(b.getPrevious());
		assertNull(b.getLeft());
		assertNull(b.getRight());
	}

	@Test
	public void testDeletionWihtMultipleElements() {
		tree.insert(Foo.A);
		tree.insert(Foo.A);
		tree.delete(Foo.A);
		assertFalse(tree.isEmpty());
		final Node<String, Foo> a = tree.getRoot();
		assertEquals(a.getSKV(), "a");
		assertNull(a.getNext());
		assertNull(a.getPrevious());
		tree.delete(Foo.A);
		assertTrue(tree.isEmpty());
	}

	@Test
	public void testIterator() {
		tree.insert(Foo.D);
		tree.insert(Foo.F);
		tree.insert(Foo.C);
		tree.insert(Foo.E);
		tree.insert(Foo.A);
		tree.insert(Foo.B);
		final Iterator<Node<String, Foo>> it = tree.iterator();
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
		tree.insert(Foo.C);
		tree.insert(Foo.A);
		tree.insert(Foo.E);
		tree.insert(Foo.B);
		tree.insert(Foo.D);
		tree.insert(Foo.G);
		tree.insert(Foo.F);
		tree.insert(Foo.H);
		tree.insert(Foo.I);
		final Node<String, Foo> c = tree.getRoot();
		assertEquals(c.getSKV(), "c");
		assertEquals(c.getPrevious().getSKV(), "b");
		assertEquals(c.getNext().getSKV(), "d");
		final Node<String, Foo> a = c.getLeft();
		assertEquals(a.getSKV(), "a");
		assertEquals(a.getNext().getSKV(), "b");
		assertNull(a.getPrevious());
		assertNull(a.getLeft());
		final Node<String, Foo> b = a.getRight();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getPrevious().getSKV(), "a");
		assertEquals(b.getNext().getSKV(), "c");
		assertNull(b.getLeft());
		assertNull(b.getRight());
		final Node<String, Foo> g = c.getRight();
		assertEquals(g.getSKV(), "g");
		assertEquals(g.getPrevious().getSKV(), "f");
		assertEquals(g.getNext().getSKV(), "h");
		final Node<String, Foo> e = g.getLeft();
		assertEquals(e.getSKV(), "e");
		assertEquals(e.getPrevious().getSKV(), "d");
		assertEquals(e.getNext().getSKV(), "f");
		final Node<String, Foo> d = e.getLeft();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		assertEquals(d.getNext().getSKV(), "e");
		assertNull(d.getLeft());
		assertNull(d.getRight());
		final Node<String, Foo> f = e.getRight();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "e");
		assertEquals(f.getNext().getSKV(), "g");
		assertNull(f.getLeft());
		assertNull(f.getRight());
		final Node<String, Foo> h = g.getRight();
		assertEquals(h.getSKV(), "h");
		assertEquals(h.getPrevious().getSKV(), "g");
		assertEquals(h.getNext().getSKV(), "i");
		assertNull(h.getLeft());
		final Node<String, Foo> i = h.getRight();
		assertEquals(i.getSKV(), "i");
		assertEquals(i.getPrevious().getSKV(), "h");
		assertNull(i.getNext());
		assertNull(i.getLeft());
		assertNull(i.getRight());
	}

	@Test
	public void testRightRotation() {
		tree.insert(Foo.G);
		tree.insert(Foo.I);
		tree.insert(Foo.E);
		tree.insert(Foo.H);
		tree.insert(Foo.F);
		tree.insert(Foo.C);
		tree.insert(Foo.D);
		tree.insert(Foo.B);
		tree.insert(Foo.A);
		final Node<String, Foo> g = tree.getRoot();
		assertEquals(g.getSKV(), "g");
		assertEquals(g.getPrevious().getSKV(), "f");
		assertEquals(g.getNext().getSKV(), "h");
		final Node<String, Foo> i = g.getRight();
		assertEquals(i.getSKV(), "i");
		assertEquals(i.getPrevious().getSKV(), "h");
		assertNull(i.getNext());
		assertNull(i.getRight());
		final Node<String, Foo> h = i.getLeft();
		assertEquals(h.getSKV(), "h");
		assertEquals(h.getPrevious().getSKV(), "g");
		assertEquals(h.getNext().getSKV(), "i");
		assertNull(h.getLeft());
		assertNull(h.getRight());
		final Node<String, Foo> c = g.getLeft();
		assertEquals(c.getSKV(), "c");
		assertEquals(c.getPrevious().getSKV(), "b");
		assertEquals(c.getNext().getSKV(), "d");
		final Node<String, Foo> e = c.getRight();
		assertEquals(e.getSKV(), "e");
		assertEquals(e.getPrevious().getSKV(), "d");
		assertEquals(e.getNext().getSKV(), "f");
		final Node<String, Foo> f = e.getRight();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "e");
		assertEquals(f.getNext().getSKV(), "g");
		assertNull(f.getLeft());
		assertNull(f.getRight());
		final Node<String, Foo> d = e.getLeft();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		assertEquals(d.getNext().getSKV(), "e");
		assertNull(d.getLeft());
		assertNull(d.getRight());
		final Node<String, Foo> b = c.getLeft();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getPrevious().getSKV(), "a");
		assertEquals(b.getNext().getSKV(), "c");
		assertNull(b.getRight());
		final Node<String, Foo> a = b.getLeft();
		assertEquals(a.getSKV(), "a");
		assertEquals(a.getNext().getSKV(), "b");
		assertNull(a.getPrevious());
		assertNull(a.getLeft());
		assertNull(a.getRight());
	}
}
