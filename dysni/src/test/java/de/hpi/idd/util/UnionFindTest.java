package de.hpi.idd.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;

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
		assertEquals(4, uf.getComponent(1).size());
		assertEquals(4, uf.getComponent(2).size());
		assertEquals(4, uf.getComponent(3).size());
		assertEquals(4, uf.getComponent(4).size());
		assertEquals(1, uf.getComponent(5).size());
		List<Set<Integer>> components = Lists.newArrayList(uf);
		components.sort((s1, s2) -> Integer.compare(Collections.min(s1), Collections.min(s2)));
		assertEquals(2, components.size());
		assertEquals(4, components.get(0).size());
		assertEquals(1, components.get(1).size());
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
