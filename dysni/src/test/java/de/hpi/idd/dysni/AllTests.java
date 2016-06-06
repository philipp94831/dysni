package de.hpi.idd.dysni;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.hpi.idd.dysni.avl.AVLTreeTest;

@RunWith(Suite.class)
@SuiteClasses({ AVLTreeTest.class, DySNIndexTest.class })
public class AllTests {
}
