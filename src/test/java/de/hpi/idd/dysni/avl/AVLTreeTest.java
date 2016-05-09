package de.hpi.idd.dysni.avl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class AVLTreeTest {

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
		assertEquals(c.getKey(), "c");
		assertEquals(c.getPrevious().getKey(), "b");
		assertEquals(c.getNext().getKey(), "d");
		final Node<String, Foo> d = c.getRight();
		assertEquals(d.getKey(), "d");
		assertEquals(d.getPrevious().getKey(), "c");
		assertEquals(d.getNext().getKey(), "f");
		assertNull(d.getLeft());
		final Node<String, Foo> f = d.getRight();
		assertEquals(f.getKey(), "f");
		assertEquals(f.getPrevious().getKey(), "d");
		assertNull(f.getNext());
		assertNull(f.getLeft());
		assertNull(f.getRight());
		final Node<String, Foo> b = c.getLeft();
		assertEquals(b.getKey(), "b");
		assertEquals(b.getNext().getKey(), "c");
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
		assertEquals(a.getKey(), "a");
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
		assertEquals(it.next().getKey(), "a");
		assertEquals(it.next().getKey(), "b");
		assertEquals(it.next().getKey(), "c");
		assertEquals(it.next().getKey(), "d");
		assertEquals(it.next().getKey(), "e");
		assertEquals(it.next().getKey(), "f");
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
		assertEquals(c.getKey(), "c");
		assertEquals(c.getPrevious().getKey(), "b");
		assertEquals(c.getNext().getKey(), "d");
		final Node<String, Foo> a = c.getLeft();
		assertEquals(a.getKey(), "a");
		assertEquals(a.getNext().getKey(), "b");
		assertNull(a.getPrevious());
		assertNull(a.getLeft());
		final Node<String, Foo> b = a.getRight();
		assertEquals(b.getKey(), "b");
		assertEquals(b.getPrevious().getKey(), "a");
		assertEquals(b.getNext().getKey(), "c");
		assertNull(b.getLeft());
		assertNull(b.getRight());
		final Node<String, Foo> g = c.getRight();
		assertEquals(g.getKey(), "g");
		assertEquals(g.getPrevious().getKey(), "f");
		assertEquals(g.getNext().getKey(), "h");
		final Node<String, Foo> e = g.getLeft();
		assertEquals(e.getKey(), "e");
		assertEquals(e.getPrevious().getKey(), "d");
		assertEquals(e.getNext().getKey(), "f");
		final Node<String, Foo> d = e.getLeft();
		assertEquals(d.getKey(), "d");
		assertEquals(d.getPrevious().getKey(), "c");
		assertEquals(d.getNext().getKey(), "e");
		assertNull(d.getLeft());
		assertNull(d.getRight());
		final Node<String, Foo> f = e.getRight();
		assertEquals(f.getKey(), "f");
		assertEquals(f.getPrevious().getKey(), "e");
		assertEquals(f.getNext().getKey(), "g");
		assertNull(f.getLeft());
		assertNull(f.getRight());
		final Node<String, Foo> h = g.getRight();
		assertEquals(h.getKey(), "h");
		assertEquals(h.getPrevious().getKey(), "g");
		assertEquals(h.getNext().getKey(), "i");
		assertNull(h.getLeft());
		final Node<String, Foo> i = h.getRight();
		assertEquals(i.getKey(), "i");
		assertEquals(i.getPrevious().getKey(), "h");
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
		assertEquals(g.getKey(), "g");
		assertEquals(g.getPrevious().getKey(), "f");
		assertEquals(g.getNext().getKey(), "h");
		final Node<String, Foo> i = g.getRight();
		assertEquals(i.getKey(), "i");
		assertEquals(i.getPrevious().getKey(), "h");
		assertNull(i.getNext());
		assertNull(i.getRight());
		final Node<String, Foo> h = i.getLeft();
		assertEquals(h.getKey(), "h");
		assertEquals(h.getPrevious().getKey(), "g");
		assertEquals(h.getNext().getKey(), "i");
		assertNull(h.getLeft());
		assertNull(h.getRight());
		final Node<String, Foo> c = g.getLeft();
		assertEquals(c.getKey(), "c");
		assertEquals(c.getPrevious().getKey(), "b");
		assertEquals(c.getNext().getKey(), "d");
		final Node<String, Foo> e = c.getRight();
		assertEquals(e.getKey(), "e");
		assertEquals(e.getPrevious().getKey(), "d");
		assertEquals(e.getNext().getKey(), "f");
		final Node<String, Foo> f = e.getRight();
		assertEquals(f.getKey(), "f");
		assertEquals(f.getPrevious().getKey(), "e");
		assertEquals(f.getNext().getKey(), "g");
		assertNull(f.getLeft());
		assertNull(f.getRight());
		final Node<String, Foo> d = e.getLeft();
		assertEquals(d.getKey(), "d");
		assertEquals(d.getPrevious().getKey(), "c");
		assertEquals(d.getNext().getKey(), "e");
		assertNull(d.getLeft());
		assertNull(d.getRight());
		final Node<String, Foo> b = c.getLeft();
		assertEquals(b.getKey(), "b");
		assertEquals(b.getPrevious().getKey(), "a");
		assertEquals(b.getNext().getKey(), "c");
		assertNull(b.getRight());
		final Node<String, Foo> a = b.getLeft();
		assertEquals(a.getKey(), "a");
		assertEquals(a.getNext().getKey(), "b");
		assertNull(a.getPrevious());
		assertNull(a.getLeft());
		assertNull(a.getRight());
	}
}
