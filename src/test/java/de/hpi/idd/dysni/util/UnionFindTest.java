package de.hpi.idd.dysni.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}
