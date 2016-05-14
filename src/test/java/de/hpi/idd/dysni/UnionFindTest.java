package de.hpi.idd.dysni;

import static org.junit.Assert.*;

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
		assertEquals(1, uf.count());
		assertEquals(4, uf.getComponent(1).size());
		assertEquals(4, uf.getComponent(2).size());
		assertEquals(4, uf.getComponent(3).size());
		assertEquals(4, uf.getComponent(4).size());
	}
}
