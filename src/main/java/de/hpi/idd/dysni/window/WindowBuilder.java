package de.hpi.idd.dysni.window;

import java.util.Collection;

import de.hpi.idd.dysni.avl.Node;

public interface WindowBuilder<RECORD, KEY extends Comparable<KEY>, ID> {

	Collection<ID> buildWindow(RECORD rec, Node<KEY, ID> node);

}
