package de.hpi.idd.dysni;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.hpi.idd.cd.CDRecordComparatorTest;
import de.hpi.idd.dysni.avl.AVLTreeTest;
import de.hpi.idd.dysni.util.SymmetricTableTest;
import de.hpi.idd.dysni.util.UnionFindTest;

@RunWith(Suite.class)
@SuiteClasses({ AVLTreeTest.class, DisnyIndexTest.class, CDRecordComparatorTest.class, UnionFindTest.class,
		SymmetricTableTest.class })
public class AllTests {
}
