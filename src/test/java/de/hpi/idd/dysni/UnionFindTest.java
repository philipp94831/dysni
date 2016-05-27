package de.hpi.idd.dysni;

import org.junit.Assert;
import org.junit.Test;

public class UnionFindTest {

	@Test
	public void test() {
		final UnionFind<Integer> uf = new UnionFind<>();
		Assert.assertFalse(uf.connected(1, 4));
		Assert.assertEquals(0, uf.count());
		uf.union(1, 2);
		Assert.assertTrue(uf.connected(1, 2));
		Assert.assertFalse(uf.connected(1, 4));
		Assert.assertEquals(1, uf.count());
		uf.union(3, 4);
		Assert.assertTrue(uf.connected(3, 4));
		Assert.assertFalse(uf.connected(1, 4));
		Assert.assertEquals(2, uf.count());
		uf.union(2, 4);
		Assert.assertTrue(uf.connected(2, 4));
		Assert.assertTrue(uf.connected(1, 4));
		uf.union(5, 5);
		uf.union(5, 5);
		Assert.assertEquals(2, uf.count());
		Assert.assertEquals(3, uf.getComponent(1).size());
		Assert.assertEquals(3, uf.getComponent(2).size());
		Assert.assertEquals(3, uf.getComponent(3).size());
		Assert.assertEquals(3, uf.getComponent(4).size());
		Assert.assertEquals(0, uf.getComponent(5).size());
	}
}
