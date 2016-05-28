package de.hpi.idd.dysni.util;

import org.junit.Assert;
import org.junit.Test;

public class SymmetricTableTest {

	@Test
	public void test() {
		final SymmetricTable<Integer, String> table = new SymmetricTable<>();
		table.put(1, 2, "foo");
		Assert.assertEquals("foo", table.get(1, 2));
		Assert.assertEquals("foo", table.get(2, 1));
	}

}
