package de.hpi.idd.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class UnionFindTest {

	@Test
	public void test() {
		UnionFind<Integer> uf = new UnionFind<>();
		assertFalse(uf.connected(1, 4));
		assertEquals(0, uf.count());
		uf.union(1, 2);
		assertTrue(uf.connected(1, 2));
		assertFalse(uf.connected(1, 4));
		assertEquals(1, uf.count());
		uf.union(3, 4);
		assertTrue(uf.connected(3, 4));
		assertFalse(uf.connected(1, 4));
		assertEquals(2, uf.count());
		uf.union(2, 4);
		assertTrue(uf.connected(2, 4));
		assertTrue(uf.connected(1, 4));
		uf.union(5, 5);
		uf.union(5, 5);
		assertEquals(2, uf.count());
		assertEquals(3, uf.getComponent(1).size());
		assertEquals(3, uf.getComponent(2).size());
		assertEquals(3, uf.getComponent(3).size());
		assertEquals(3, uf.getComponent(4).size());
		assertEquals(0, uf.getComponent(5).size());
	}

	@Test
	public void testNullInsertion() {
		UnionFind<Integer> uf = new UnionFind<>();
		try {
			uf.union(null, null);
			fail("NullPointerException should be thrown");
		} catch (NullPointerException e) {
			assertEquals(e.getMessage(), "Element must not be null");
		}
	}

	@Test
	public void testNullQuery() {
		UnionFind<Integer> uf = new UnionFind<>();
		try {
			uf.connected(null, null);
			fail("NullPointerException should be thrown");
		} catch (NullPointerException e) {
			assertEquals(e.getMessage(), "Element must not be null");
		}
	}
}
