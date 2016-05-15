package de.hpi.idd.dysni;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.hpi.idd.dysni.avl.AVLTreeTest;
import de.hpi.idd.dysni.avl.SimilarityTest;
import de.hpi.idd.dysni.records.CDRecordComparatorTest;

@RunWith(Suite.class)
@SuiteClasses({ AVLTreeTest.class, SimilarityTest.class, CDRecordComparatorTest.class, UnionFindTest.class })
public class AllTests {
}
