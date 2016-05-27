package de.hpi.idd.dysni.avl;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.idd.dysni.Foo;

public class AVLTreeTest {

	private final AVLTree<String, Foo> tree = new AVLTree<>();

	private void insert(final Foo foo) {
		tree.insert(foo.name(), foo);
	}

	@Test
	public void testDeletion() {
		insert(Foo.C);
		insert(Foo.A);
		insert(Foo.D);
		insert(Foo.B);
		insert(Foo.F);
		insert(Foo.E);
		tree.delete(Foo.A.name(), Foo.A);
		tree.delete(Foo.E.name(), Foo.E);
		final Node<String, Foo> c = tree.getRoot();
		Assert.assertEquals(c.getKey(), "C");
		Assert.assertEquals(c.getPrevious().getKey(), "B");
		Assert.assertEquals(c.getNext().getKey(), "D");
		final Node<String, Foo> d = c.getRight();
		Assert.assertEquals(d.getKey(), "D");
		Assert.assertEquals(d.getPrevious().getKey(), "C");
		Assert.assertEquals(d.getNext().getKey(), "F");
		Assert.assertNull(d.getLeft());
		final Node<String, Foo> f = d.getRight();
		Assert.assertEquals(f.getKey(), "F");
		Assert.assertEquals(f.getPrevious().getKey(), "D");
		Assert.assertNull(f.getNext());
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final Node<String, Foo> b = c.getLeft();
		Assert.assertEquals(b.getKey(), "B");
		Assert.assertEquals(b.getNext().getKey(), "C");
		Assert.assertNull(b.getPrevious());
		Assert.assertNull(b.getLeft());
		Assert.assertNull(b.getRight());
	}

	@Test
	public void testDeletionWihtMultipleElements() {
		insert(Foo.A);
		insert(Foo.A);
		tree.delete(Foo.A.name(), Foo.A);
		Assert.assertFalse(tree.isEmpty());
		final Node<String, Foo> a = tree.getRoot();
		Assert.assertEquals(a.getKey(), "A");
		Assert.assertNull(a.getNext());
		Assert.assertNull(a.getPrevious());
		tree.delete(Foo.A.name(), Foo.A);
		Assert.assertTrue(tree.isEmpty());
	}

	@Test
	public void testIterator() {
		insert(Foo.D);
		insert(Foo.F);
		insert(Foo.C);
		insert(Foo.E);
		insert(Foo.A);
		insert(Foo.B);
		final Iterator<Node<String, Foo>> it = tree.iterator();
		Assert.assertEquals(it.next().getKey(), "A");
		Assert.assertEquals(it.next().getKey(), "B");
		Assert.assertEquals(it.next().getKey(), "C");
		Assert.assertEquals(it.next().getKey(), "D");
		Assert.assertEquals(it.next().getKey(), "E");
		Assert.assertEquals(it.next().getKey(), "F");
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testLeftRotation() {
		insert(Foo.C);
		insert(Foo.A);
		insert(Foo.E);
		insert(Foo.B);
		insert(Foo.D);
		insert(Foo.G);
		insert(Foo.F);
		insert(Foo.H);
		insert(Foo.I);
		final Node<String, Foo> c = tree.getRoot();
		Assert.assertEquals(c.getKey(), "C");
		Assert.assertEquals(c.getPrevious().getKey(), "B");
		Assert.assertEquals(c.getNext().getKey(), "D");
		final Node<String, Foo> a = c.getLeft();
		Assert.assertEquals(a.getKey(), "A");
		Assert.assertEquals(a.getNext().getKey(), "B");
		Assert.assertNull(a.getPrevious());
		Assert.assertNull(a.getLeft());
		final Node<String, Foo> b = a.getRight();
		Assert.assertEquals(b.getKey(), "B");
		Assert.assertEquals(b.getPrevious().getKey(), "A");
		Assert.assertEquals(b.getNext().getKey(), "C");
		Assert.assertNull(b.getLeft());
		Assert.assertNull(b.getRight());
		final Node<String, Foo> g = c.getRight();
		Assert.assertEquals(g.getKey(), "G");
		Assert.assertEquals(g.getPrevious().getKey(), "F");
		Assert.assertEquals(g.getNext().getKey(), "H");
		final Node<String, Foo> e = g.getLeft();
		Assert.assertEquals(e.getKey(), "E");
		Assert.assertEquals(e.getPrevious().getKey(), "D");
		Assert.assertEquals(e.getNext().getKey(), "F");
		final Node<String, Foo> d = e.getLeft();
		Assert.assertEquals(d.getKey(), "D");
		Assert.assertEquals(d.getPrevious().getKey(), "C");
		Assert.assertEquals(d.getNext().getKey(), "E");
		Assert.assertNull(d.getLeft());
		Assert.assertNull(d.getRight());
		final Node<String, Foo> f = e.getRight();
		Assert.assertEquals(f.getKey(), "F");
		Assert.assertEquals(f.getPrevious().getKey(), "E");
		Assert.assertEquals(f.getNext().getKey(), "G");
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final Node<String, Foo> h = g.getRight();
		Assert.assertEquals(h.getKey(), "H");
		Assert.assertEquals(h.getPrevious().getKey(), "G");
		Assert.assertEquals(h.getNext().getKey(), "I");
		Assert.assertNull(h.getLeft());
		final Node<String, Foo> i = h.getRight();
		Assert.assertEquals(i.getKey(), "I");
		Assert.assertEquals(i.getPrevious().getKey(), "H");
		Assert.assertNull(i.getNext());
		Assert.assertNull(i.getLeft());
		Assert.assertNull(i.getRight());
	}

	@Test
	public void testRightRotation() {
		insert(Foo.G);
		insert(Foo.I);
		insert(Foo.E);
		insert(Foo.H);
		insert(Foo.F);
		insert(Foo.C);
		insert(Foo.D);
		insert(Foo.B);
		insert(Foo.A);
		final Node<String, Foo> g = tree.getRoot();
		Assert.assertEquals(g.getKey(), "G");
		Assert.assertEquals(g.getPrevious().getKey(), "F");
		Assert.assertEquals(g.getNext().getKey(), "H");
		final Node<String, Foo> i = g.getRight();
		Assert.assertEquals(i.getKey(), "I");
		Assert.assertEquals(i.getPrevious().getKey(), "H");
		Assert.assertNull(i.getNext());
		Assert.assertNull(i.getRight());
		final Node<String, Foo> h = i.getLeft();
		Assert.assertEquals(h.getKey(), "H");
		Assert.assertEquals(h.getPrevious().getKey(), "G");
		Assert.assertEquals(h.getNext().getKey(), "I");
		Assert.assertNull(h.getLeft());
		Assert.assertNull(h.getRight());
		final Node<String, Foo> c = g.getLeft();
		Assert.assertEquals(c.getKey(), "C");
		Assert.assertEquals(c.getPrevious().getKey(), "B");
		Assert.assertEquals(c.getNext().getKey(), "D");
		final Node<String, Foo> e = c.getRight();
		Assert.assertEquals(e.getKey(), "E");
		Assert.assertEquals(e.getPrevious().getKey(), "D");
		Assert.assertEquals(e.getNext().getKey(), "F");
		final Node<String, Foo> f = e.getRight();
		Assert.assertEquals(f.getKey(), "F");
		Assert.assertEquals(f.getPrevious().getKey(), "E");
		Assert.assertEquals(f.getNext().getKey(), "G");
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final Node<String, Foo> d = e.getLeft();
		Assert.assertEquals(d.getKey(), "D");
		Assert.assertEquals(d.getPrevious().getKey(), "C");
		Assert.assertEquals(d.getNext().getKey(), "E");
		Assert.assertNull(d.getLeft());
		Assert.assertNull(d.getRight());
		final Node<String, Foo> b = c.getLeft();
		Assert.assertEquals(b.getKey(), "B");
		Assert.assertEquals(b.getPrevious().getKey(), "A");
		Assert.assertEquals(b.getNext().getKey(), "C");
		Assert.assertNull(b.getRight());
		final Node<String, Foo> a = b.getLeft();
		Assert.assertEquals(a.getKey(), "A");
		Assert.assertEquals(a.getNext().getKey(), "B");
		Assert.assertNull(a.getPrevious());
		Assert.assertNull(a.getLeft());
		Assert.assertNull(a.getRight());
	}
}
