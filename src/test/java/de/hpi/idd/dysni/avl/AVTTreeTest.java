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
		final Node<String, Foo> f = g.getPrevious();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getNext().getSKV(), "g");
		final Node<String, Foo> e = f.getPrevious();
		assertEquals(e.getSKV(), "e");
		assertEquals(e.getNext().getSKV(), "f");
		final Node<String, Foo> d = e.getPrevious();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getNext().getSKV(), "e");
		final Node<String, Foo> c = d.getPrevious();
		assertEquals(c.getSKV(), "c");
		assertEquals(c.getNext().getSKV(), "d");
		final Node<String, Foo> b = c.getPrevious();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getNext().getSKV(), "c");
		final Node<String, Foo> a = b.getPrevious();
		assertEquals(a.getSKV(), "a");
		assertEquals(a.getNext().getSKV(), "b");
		assertNull(a.getPrevious());
		final Node<String, Foo> h = g.getNext();
		assertEquals(h.getSKV(), "h");
		assertEquals(h.getPrevious().getSKV(), "g");
		final Node<String, Foo> i = h.getNext();
		assertEquals(i.getSKV(), "i");
		assertEquals(i.getPrevious().getSKV(), "h");
		assertNull(i.getNext());
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
		final Node<String, Foo> d = c.getNext();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		final Node<String, Foo> e = d.getNext();
		assertEquals(e.getSKV(), "e");
		assertEquals(e.getPrevious().getSKV(), "d");
		final Node<String, Foo> f = e.getNext();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "e");
		final Node<String, Foo> g = f.getNext();
		assertEquals(g.getSKV(), "g");
		assertEquals(g.getPrevious().getSKV(), "f");
		final Node<String, Foo> h = g.getNext();
		assertEquals(h.getSKV(), "h");
		assertEquals(h.getPrevious().getSKV(), "g");
		final Node<String, Foo> i = h.getNext();
		assertEquals(i.getSKV(), "i");
		assertEquals(i.getPrevious().getSKV(), "h");
		assertNull(i.getNext());
		final Node<String, Foo> b = c.getPrevious();
		assertEquals(b.getSKV(), "b");
		assertEquals(b.getNext().getSKV(), "c");
		final Node<String, Foo> a = b.getPrevious();
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
		final Node<String, Foo> c = tree.getRoot();
		assertEquals(c.getSKV(), "c");
		final Node<String, Foo> d = c.getNext();
		assertEquals(d.getSKV(), "d");
		assertEquals(d.getPrevious().getSKV(), "c");
		final Node<String, Foo> f = d.getNext();
		assertEquals(f.getSKV(), "f");
		assertEquals(f.getPrevious().getSKV(), "d");
		assertNull(f.getNext());
		final Node<String, Foo> b = c.getPrevious();
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
		final Node<String, Foo> a = tree.getRoot();
		assertEquals(a.getSKV(), "a");
		assertNull(a.getNext());
		assertNull(a.getPrevious());
		tree.delete(Foo.A);
		assertTrue(tree.isEmpty());
	}
}
