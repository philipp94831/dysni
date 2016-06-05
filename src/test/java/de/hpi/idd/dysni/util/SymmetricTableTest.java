package de.hpi.idd.dysni.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SymmetricTableTest {

	@Test
	public void test() {
		SymmetricTable<Integer, String> table = new SymmetricTable<>();
		table.put(1, 2, "foo");
		assertEquals("foo", table.get(1, 2));
		assertEquals("foo", table.get(2, 1));
	}
}
