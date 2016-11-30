package de.hpi.idd;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.hpi.idd.dysni.DySNIndexTest;
import de.hpi.idd.dysni.DynamicSortedNeighborhoodIndexerTest;
import de.hpi.idd.dysni.avl.AVLTreeTest;
import de.hpi.idd.util.SymmetricTableTest;
import de.hpi.idd.util.UnionFindTest;

@RunWith(Suite.class)
@SuiteClasses({ AVLTreeTest.class, DySNIndexTest.class, SymmetricTableTest.class, UnionFindTest.class,
		DynamicSortedNeighborhoodIndexerTest.class })
public class AllTests {
}
