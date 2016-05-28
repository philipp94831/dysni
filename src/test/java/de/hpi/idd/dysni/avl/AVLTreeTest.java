package de.hpi.idd.dysni.avl;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

public class AVLTreeTest {

	private final AVLTree<String, String> tree = new AVLTree<>();

	private void insert(final String s) {
		tree.insert(s, s.toLowerCase());
	}

	@Test
	public void testDeletion() {
		insert("C");
		insert("A");
		insert("D");
		insert("B");
		insert("F");
		insert("E");
		tree.delete("A", "a");
		tree.delete("E", "e");
		final Node<String, String> c = tree.getRoot();
		Assert.assertEquals(c.getKey(), "C");
		Assert.assertEquals(c.getPrevious().getKey(), "B");
		Assert.assertEquals(c.getNext().getKey(), "D");
		final Node<String, String> d = c.getRight();
		Assert.assertEquals(d.getKey(), "D");
		Assert.assertEquals(d.getPrevious().getKey(), "C");
		Assert.assertEquals(d.getNext().getKey(), "F");
		Assert.assertNull(d.getLeft());
		final Node<String, String> f = d.getRight();
		Assert.assertEquals(f.getKey(), "F");
		Assert.assertEquals(f.getPrevious().getKey(), "D");
		Assert.assertNull(f.getNext());
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final Node<String, String> b = c.getLeft();
		Assert.assertEquals(b.getKey(), "B");
		Assert.assertEquals(b.getNext().getKey(), "C");
		Assert.assertNull(b.getPrevious());
		Assert.assertNull(b.getLeft());
		Assert.assertNull(b.getRight());
	}

	@Test
	public void testDeletionWihtMultipleElements() {
		insert("A");
		insert("A");
		tree.delete("A", "a");
		Assert.assertFalse(tree.isEmpty());
		final Node<String, String> a = tree.getRoot();
		Assert.assertEquals(a.getKey(), "A");
		Assert.assertNull(a.getNext());
		Assert.assertNull(a.getPrevious());
		tree.delete("A", "a");
		Assert.assertTrue(tree.isEmpty());
	}

	@Test
	public void testIterator() {
		insert("D");
		insert("F");
		insert("C");
		insert("E");
		insert("A");
		insert("B");
		final Iterator<Node<String, String>> it = tree.iterator();
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
		insert("C");
		insert("A");
		insert("E");
		insert("B");
		insert("D");
		insert("G");
		insert("F");
		insert("H");
		insert("I");
		final Node<String, String> c = tree.getRoot();
		Assert.assertEquals(c.getKey(), "C");
		Assert.assertEquals(c.getPrevious().getKey(), "B");
		Assert.assertEquals(c.getNext().getKey(), "D");
		final Node<String, String> a = c.getLeft();
		Assert.assertEquals(a.getKey(), "A");
		Assert.assertEquals(a.getNext().getKey(), "B");
		Assert.assertNull(a.getPrevious());
		Assert.assertNull(a.getLeft());
		final Node<String, String> b = a.getRight();
		Assert.assertEquals(b.getKey(), "B");
		Assert.assertEquals(b.getPrevious().getKey(), "A");
		Assert.assertEquals(b.getNext().getKey(), "C");
		Assert.assertNull(b.getLeft());
		Assert.assertNull(b.getRight());
		final Node<String, String> g = c.getRight();
		Assert.assertEquals(g.getKey(), "G");
		Assert.assertEquals(g.getPrevious().getKey(), "F");
		Assert.assertEquals(g.getNext().getKey(), "H");
		final Node<String, String> e = g.getLeft();
		Assert.assertEquals(e.getKey(), "E");
		Assert.assertEquals(e.getPrevious().getKey(), "D");
		Assert.assertEquals(e.getNext().getKey(), "F");
		final Node<String, String> d = e.getLeft();
		Assert.assertEquals(d.getKey(), "D");
		Assert.assertEquals(d.getPrevious().getKey(), "C");
		Assert.assertEquals(d.getNext().getKey(), "E");
		Assert.assertNull(d.getLeft());
		Assert.assertNull(d.getRight());
		final Node<String, String> f = e.getRight();
		Assert.assertEquals(f.getKey(), "F");
		Assert.assertEquals(f.getPrevious().getKey(), "E");
		Assert.assertEquals(f.getNext().getKey(), "G");
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final Node<String, String> h = g.getRight();
		Assert.assertEquals(h.getKey(), "H");
		Assert.assertEquals(h.getPrevious().getKey(), "G");
		Assert.assertEquals(h.getNext().getKey(), "I");
		Assert.assertNull(h.getLeft());
		final Node<String, String> i = h.getRight();
		Assert.assertEquals(i.getKey(), "I");
		Assert.assertEquals(i.getPrevious().getKey(), "H");
		Assert.assertNull(i.getNext());
		Assert.assertNull(i.getLeft());
		Assert.assertNull(i.getRight());
	}

	@Test
	public void testRightRotation() {
		insert("G");
		insert("I");
		insert("E");
		insert("H");
		insert("F");
		insert("C");
		insert("D");
		insert("B");
		insert("A");
		final Node<String, String> g = tree.getRoot();
		Assert.assertEquals(g.getKey(), "G");
		Assert.assertEquals(g.getPrevious().getKey(), "F");
		Assert.assertEquals(g.getNext().getKey(), "H");
		final Node<String, String> i = g.getRight();
		Assert.assertEquals(i.getKey(), "I");
		Assert.assertEquals(i.getPrevious().getKey(), "H");
		Assert.assertNull(i.getNext());
		Assert.assertNull(i.getRight());
		final Node<String, String> h = i.getLeft();
		Assert.assertEquals(h.getKey(), "H");
		Assert.assertEquals(h.getPrevious().getKey(), "G");
		Assert.assertEquals(h.getNext().getKey(), "I");
		Assert.assertNull(h.getLeft());
		Assert.assertNull(h.getRight());
		final Node<String, String> c = g.getLeft();
		Assert.assertEquals(c.getKey(), "C");
		Assert.assertEquals(c.getPrevious().getKey(), "B");
		Assert.assertEquals(c.getNext().getKey(), "D");
		final Node<String, String> e = c.getRight();
		Assert.assertEquals(e.getKey(), "E");
		Assert.assertEquals(e.getPrevious().getKey(), "D");
		Assert.assertEquals(e.getNext().getKey(), "F");
		final Node<String, String> f = e.getRight();
		Assert.assertEquals(f.getKey(), "F");
		Assert.assertEquals(f.getPrevious().getKey(), "E");
		Assert.assertEquals(f.getNext().getKey(), "G");
		Assert.assertNull(f.getLeft());
		Assert.assertNull(f.getRight());
		final Node<String, String> d = e.getLeft();
		Assert.assertEquals(d.getKey(), "D");
		Assert.assertEquals(d.getPrevious().getKey(), "C");
		Assert.assertEquals(d.getNext().getKey(), "E");
		Assert.assertNull(d.getLeft());
		Assert.assertNull(d.getRight());
		final Node<String, String> b = c.getLeft();
		Assert.assertEquals(b.getKey(), "B");
		Assert.assertEquals(b.getPrevious().getKey(), "A");
		Assert.assertEquals(b.getNext().getKey(), "C");
		Assert.assertNull(b.getRight());
		final Node<String, String> a = b.getLeft();
		Assert.assertEquals(a.getKey(), "A");
		Assert.assertEquals(a.getNext().getKey(), "B");
		Assert.assertNull(a.getPrevious());
		Assert.assertNull(a.getLeft());
		Assert.assertNull(a.getRight());
	}
}
