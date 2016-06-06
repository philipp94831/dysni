package de.hpi.idd.dysni.avl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;

public class AVLTreeTest {

	private final BraidedAVLTree<String, String> tree = new BraidedAVLTree<>();

	private void insert(String s) {
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
		Node<String, String> c = tree.getRoot();
		assertEquals(c.getKey(), "C");
		assertEquals(c.getPrevious().getKey(), "B");
		assertEquals(c.getNext().getKey(), "D");
		Node<String, String> d = c.getRight();
		assertEquals(d.getKey(), "D");
		assertEquals(d.getPrevious().getKey(), "C");
		assertEquals(d.getNext().getKey(), "F");
		assertNull(d.getLeft());
		Node<String, String> f = d.getRight();
		assertEquals(f.getKey(), "F");
		assertEquals(f.getPrevious().getKey(), "D");
		assertNull(f.getNext());
		assertNull(f.getLeft());
		assertNull(f.getRight());
		Node<String, String> b = c.getLeft();
		assertEquals(b.getKey(), "B");
		assertEquals(b.getNext().getKey(), "C");
		assertNull(b.getPrevious());
		assertNull(b.getLeft());
		assertNull(b.getRight());
	}

	@Test
	public void testDeletionWihtMultipleElements() {
		insert("A");
		insert("A");
		tree.delete("A", "a");
		assertFalse(tree.isEmpty());
		Node<String, String> a = tree.getRoot();
		assertEquals(a.getKey(), "A");
		assertNull(a.getNext());
		assertNull(a.getPrevious());
		tree.delete("A", "a");
		assertTrue(tree.isEmpty());
	}

	@Test
	public void testIterator() {
		insert("D");
		insert("F");
		insert("C");
		insert("E");
		insert("A");
		insert("B");
		Iterator<Node<String, String>> it = tree.iterator();
		assertEquals(it.next().getKey(), "A");
		assertEquals(it.next().getKey(), "B");
		assertEquals(it.next().getKey(), "C");
		assertEquals(it.next().getKey(), "D");
		assertEquals(it.next().getKey(), "E");
		assertEquals(it.next().getKey(), "F");
		assertFalse(it.hasNext());
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
		Node<String, String> c = tree.getRoot();
		assertEquals(c.getKey(), "C");
		assertEquals(c.getPrevious().getKey(), "B");
		assertEquals(c.getNext().getKey(), "D");
		Node<String, String> a = c.getLeft();
		assertEquals(a.getKey(), "A");
		assertEquals(a.getNext().getKey(), "B");
		assertNull(a.getPrevious());
		assertNull(a.getLeft());
		Node<String, String> b = a.getRight();
		assertEquals(b.getKey(), "B");
		assertEquals(b.getPrevious().getKey(), "A");
		assertEquals(b.getNext().getKey(), "C");
		assertNull(b.getLeft());
		assertNull(b.getRight());
		Node<String, String> g = c.getRight();
		assertEquals(g.getKey(), "G");
		assertEquals(g.getPrevious().getKey(), "F");
		assertEquals(g.getNext().getKey(), "H");
		Node<String, String> e = g.getLeft();
		assertEquals(e.getKey(), "E");
		assertEquals(e.getPrevious().getKey(), "D");
		assertEquals(e.getNext().getKey(), "F");
		Node<String, String> d = e.getLeft();
		assertEquals(d.getKey(), "D");
		assertEquals(d.getPrevious().getKey(), "C");
		assertEquals(d.getNext().getKey(), "E");
		assertNull(d.getLeft());
		assertNull(d.getRight());
		Node<String, String> f = e.getRight();
		assertEquals(f.getKey(), "F");
		assertEquals(f.getPrevious().getKey(), "E");
		assertEquals(f.getNext().getKey(), "G");
		assertNull(f.getLeft());
		assertNull(f.getRight());
		Node<String, String> h = g.getRight();
		assertEquals(h.getKey(), "H");
		assertEquals(h.getPrevious().getKey(), "G");
		assertEquals(h.getNext().getKey(), "I");
		assertNull(h.getLeft());
		Node<String, String> i = h.getRight();
		assertEquals(i.getKey(), "I");
		assertEquals(i.getPrevious().getKey(), "H");
		assertNull(i.getNext());
		assertNull(i.getLeft());
		assertNull(i.getRight());
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
		Node<String, String> g = tree.getRoot();
		assertEquals(g.getKey(), "G");
		assertEquals(g.getPrevious().getKey(), "F");
		assertEquals(g.getNext().getKey(), "H");
		Node<String, String> i = g.getRight();
		assertEquals(i.getKey(), "I");
		assertEquals(i.getPrevious().getKey(), "H");
		assertNull(i.getNext());
		assertNull(i.getRight());
		Node<String, String> h = i.getLeft();
		assertEquals(h.getKey(), "H");
		assertEquals(h.getPrevious().getKey(), "G");
		assertEquals(h.getNext().getKey(), "I");
		assertNull(h.getLeft());
		assertNull(h.getRight());
		Node<String, String> c = g.getLeft();
		assertEquals(c.getKey(), "C");
		assertEquals(c.getPrevious().getKey(), "B");
		assertEquals(c.getNext().getKey(), "D");
		Node<String, String> e = c.getRight();
		assertEquals(e.getKey(), "E");
		assertEquals(e.getPrevious().getKey(), "D");
		assertEquals(e.getNext().getKey(), "F");
		Node<String, String> f = e.getRight();
		assertEquals(f.getKey(), "F");
		assertEquals(f.getPrevious().getKey(), "E");
		assertEquals(f.getNext().getKey(), "G");
		assertNull(f.getLeft());
		assertNull(f.getRight());
		Node<String, String> d = e.getLeft();
		assertEquals(d.getKey(), "D");
		assertEquals(d.getPrevious().getKey(), "C");
		assertEquals(d.getNext().getKey(), "E");
		assertNull(d.getLeft());
		assertNull(d.getRight());
		Node<String, String> b = c.getLeft();
		assertEquals(b.getKey(), "B");
		assertEquals(b.getPrevious().getKey(), "A");
		assertEquals(b.getNext().getKey(), "C");
		assertNull(b.getRight());
		Node<String, String> a = b.getLeft();
		assertEquals(a.getKey(), "A");
		assertEquals(a.getNext().getKey(), "B");
		assertNull(a.getPrevious());
		assertNull(a.getLeft());
		assertNull(a.getRight());
	}
}
