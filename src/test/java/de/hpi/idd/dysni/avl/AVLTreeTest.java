package de.hpi.idd.dysni.avl;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class AVLTreeTest {

	private final SimpleAVLTree<String, Foo> tree = new SimpleAVLTree<>();

	@Test
	public void testDeletion() {
		tree.insert(Foo.C.getKey(), Foo.C);
		tree.insert(Foo.A.getKey(), Foo.A);
		tree.insert(Foo.D.getKey(), Foo.D);
		tree.insert(Foo.B.getKey(), Foo.B);
		tree.insert(Foo.F.getKey(), Foo.F);
		tree.insert(Foo.E.getKey(), Foo.E);
		tree.delete(Foo.A.getKey(), Foo.A);
		tree.delete(Foo.E.getKey(), Foo.E);
		final SimpleNode<String, Foo> c = tree.getRoot();
		Assert.assertEquals(c.getKey(), "c");
		Assert.assertEquals(c.getPrevious().getKey(), "b");
		Assert.assertEquals(c.getNext().getKey(), "d");
		final SimpleNode<String, Foo> d = c.getRight();
		Assert.assertEquals(d.getKey(), "d");
		Assert.assertEquals(d.getPrevious().getKey(), "c");
		Assert.assertEquals(d.getNext().getKey(), "f");
		Assert.assertNull(d.getLeft());
		final SimpleNode<String, Foo> f = d.getRight();
		Assert.assertEquals(f.getKey(), "f");
		Assert.assertEquals(f.getPrevious().getKey(), "d");
		Assert.assertNull(f.getNext());
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final SimpleNode<String, Foo> b = c.getLeft();
		Assert.assertEquals(b.getKey(), "b");
		Assert.assertEquals(b.getNext().getKey(), "c");
		Assert.assertNull(b.getPrevious());
		Assert.assertNull(b.getLeft());
		Assert.assertNull(b.getRight());
	}

	@Test
	public void testDeletionWihtMultipleElements() {
		tree.insert(Foo.A.getKey(), Foo.A);
		tree.insert(Foo.A.getKey(), Foo.A);
		tree.delete(Foo.A.getKey(), Foo.A);
		Assert.assertFalse(tree.isEmpty());
		final SimpleNode<String, Foo> a = tree.getRoot();
		Assert.assertEquals(a.getKey(), "a");
		Assert.assertNull(a.getNext());
		Assert.assertNull(a.getPrevious());
		tree.delete(Foo.A.getKey(), Foo.A);
		Assert.assertTrue(tree.isEmpty());
	}

	@Test
	public void testIterator() {
		tree.insert(Foo.D.getKey(), Foo.D);
		tree.insert(Foo.F.getKey(), Foo.F);
		tree.insert(Foo.C.getKey(), Foo.C);
		tree.insert(Foo.E.getKey(), Foo.E);
		tree.insert(Foo.A.getKey(), Foo.A);
		tree.insert(Foo.B.getKey(), Foo.B);
		final Iterator<SimpleNode<String, Foo>> it = tree.iterator();
		Assert.assertEquals(it.next().getKey(), "a");
		Assert.assertEquals(it.next().getKey(), "b");
		Assert.assertEquals(it.next().getKey(), "c");
		Assert.assertEquals(it.next().getKey(), "d");
		Assert.assertEquals(it.next().getKey(), "e");
		Assert.assertEquals(it.next().getKey(), "f");
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testLeftRotation() {
		tree.insert(Foo.C.getKey(), Foo.C);
		tree.insert(Foo.A.getKey(), Foo.A);
		tree.insert(Foo.E.getKey(), Foo.E);
		tree.insert(Foo.B.getKey(), Foo.B);
		tree.insert(Foo.D.getKey(), Foo.D);
		tree.insert(Foo.G.getKey(), Foo.G);
		tree.insert(Foo.F.getKey(), Foo.F);
		tree.insert(Foo.H.getKey(), Foo.H);
		tree.insert(Foo.I.getKey(), Foo.I);
		final SimpleNode<String, Foo> c = tree.getRoot();
		Assert.assertEquals(c.getKey(), "c");
		Assert.assertEquals(c.getPrevious().getKey(), "b");
		Assert.assertEquals(c.getNext().getKey(), "d");
		final SimpleNode<String, Foo> a = c.getLeft();
		Assert.assertEquals(a.getKey(), "a");
		Assert.assertEquals(a.getNext().getKey(), "b");
		Assert.assertNull(a.getPrevious());
		Assert.assertNull(a.getLeft());
		final SimpleNode<String, Foo> b = a.getRight();
		Assert.assertEquals(b.getKey(), "b");
		Assert.assertEquals(b.getPrevious().getKey(), "a");
		Assert.assertEquals(b.getNext().getKey(), "c");
		Assert.assertNull(b.getLeft());
		Assert.assertNull(b.getRight());
		final SimpleNode<String, Foo> g = c.getRight();
		Assert.assertEquals(g.getKey(), "g");
		Assert.assertEquals(g.getPrevious().getKey(), "f");
		Assert.assertEquals(g.getNext().getKey(), "h");
		final SimpleNode<String, Foo> e = g.getLeft();
		Assert.assertEquals(e.getKey(), "e");
		Assert.assertEquals(e.getPrevious().getKey(), "d");
		Assert.assertEquals(e.getNext().getKey(), "f");
		final SimpleNode<String, Foo> d = e.getLeft();
		Assert.assertEquals(d.getKey(), "d");
		Assert.assertEquals(d.getPrevious().getKey(), "c");
		Assert.assertEquals(d.getNext().getKey(), "e");
		Assert.assertNull(d.getLeft());
		Assert.assertNull(d.getRight());
		final SimpleNode<String, Foo> f = e.getRight();
		Assert.assertEquals(f.getKey(), "f");
		Assert.assertEquals(f.getPrevious().getKey(), "e");
		Assert.assertEquals(f.getNext().getKey(), "g");
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final SimpleNode<String, Foo> h = g.getRight();
		Assert.assertEquals(h.getKey(), "h");
		Assert.assertEquals(h.getPrevious().getKey(), "g");
		Assert.assertEquals(h.getNext().getKey(), "i");
		Assert.assertNull(h.getLeft());
		final SimpleNode<String, Foo> i = h.getRight();
		Assert.assertEquals(i.getKey(), "i");
		Assert.assertEquals(i.getPrevious().getKey(), "h");
		Assert.assertNull(i.getNext());
		Assert.assertNull(i.getLeft());
		Assert.assertNull(i.getRight());
	}

	@Test
	public void testRightRotation() {
		tree.insert(Foo.G.getKey(), Foo.G);
		tree.insert(Foo.I.getKey(), Foo.I);
		tree.insert(Foo.E.getKey(), Foo.E);
		tree.insert(Foo.H.getKey(), Foo.H);
		tree.insert(Foo.F.getKey(), Foo.F);
		tree.insert(Foo.C.getKey(), Foo.C);
		tree.insert(Foo.D.getKey(), Foo.D);
		tree.insert(Foo.B.getKey(), Foo.B);
		tree.insert(Foo.A.getKey(), Foo.A);
		final SimpleNode<String, Foo> g = tree.getRoot();
		Assert.assertEquals(g.getKey(), "g");
		Assert.assertEquals(g.getPrevious().getKey(), "f");
		Assert.assertEquals(g.getNext().getKey(), "h");
		final SimpleNode<String, Foo> i = g.getRight();
		Assert.assertEquals(i.getKey(), "i");
		Assert.assertEquals(i.getPrevious().getKey(), "h");
		Assert.assertNull(i.getNext());
		Assert.assertNull(i.getRight());
		final SimpleNode<String, Foo> h = i.getLeft();
		Assert.assertEquals(h.getKey(), "h");
		Assert.assertEquals(h.getPrevious().getKey(), "g");
		Assert.assertEquals(h.getNext().getKey(), "i");
		Assert.assertNull(h.getLeft());
		Assert.assertNull(h.getRight());
		final SimpleNode<String, Foo> c = g.getLeft();
		Assert.assertEquals(c.getKey(), "c");
		Assert.assertEquals(c.getPrevious().getKey(), "b");
		Assert.assertEquals(c.getNext().getKey(), "d");
		final SimpleNode<String, Foo> e = c.getRight();
		Assert.assertEquals(e.getKey(), "e");
		Assert.assertEquals(e.getPrevious().getKey(), "d");
		Assert.assertEquals(e.getNext().getKey(), "f");
		final SimpleNode<String, Foo> f = e.getRight();
		Assert.assertEquals(f.getKey(), "f");
		Assert.assertEquals(f.getPrevious().getKey(), "e");
		Assert.assertEquals(f.getNext().getKey(), "g");
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final SimpleNode<String, Foo> d = e.getLeft();
		Assert.assertEquals(d.getKey(), "d");
		Assert.assertEquals(d.getPrevious().getKey(), "c");
		Assert.assertEquals(d.getNext().getKey(), "e");
		Assert.assertNull(d.getLeft());
		Assert.assertNull(d.getRight());
		final SimpleNode<String, Foo> b = c.getLeft();
		Assert.assertEquals(b.getKey(), "b");
		Assert.assertEquals(b.getPrevious().getKey(), "a");
		Assert.assertEquals(b.getNext().getKey(), "c");
		Assert.assertNull(b.getRight());
		final SimpleNode<String, Foo> a = b.getLeft();
		Assert.assertEquals(a.getKey(), "a");
		Assert.assertEquals(a.getNext().getKey(), "b");
		Assert.assertNull(a.getPrevious());
		Assert.assertNull(a.getLeft());
		Assert.assertNull(a.getRight());
	}
}
