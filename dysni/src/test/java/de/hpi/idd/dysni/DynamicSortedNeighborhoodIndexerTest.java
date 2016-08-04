package de.hpi.idd.dysni;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hpi.idd.dysni.window.FixedWindowBuilder;
import de.hpi.idd.sim.LevenshteinSimilarity;
import de.hpi.idd.store.MemoryStore;
import de.hpi.idd.store.StoreException;

public class DynamicSortedNeighborhoodIndexerTest {

	private DynamicSortedNeighborhoodIndexer<String, Integer> er;

	@Before
	public void setup() {
		MemoryStore<Integer, String> store = new MemoryStore<>();
		er = new DynamicSortedNeighborhoodIndexer<>(store, new LevenshteinSimilarity().asClassifier(0.5));
		er.addIndex(new DySNIndexConfiguration<String, String, Integer>(s -> s.substring(0, 1), new FixedWindowBuilder<>(1)));
	}

	@Test
	public void test() throws StoreException {
		assertEquals(0, er.insert("AA", 0).size());
		assertEquals(1, er.insert("BA", 1).size());
		assertEquals(1, er.insert("BA", 1).size());
		assertEquals(2, er.insert("CA", null).size());
		assertEquals(2, er.insert("CA", 2).size());
	}
	
	@After
	public void tearDown() throws StoreException {
		er.close();
	}

}
