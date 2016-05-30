package de.hpi.idd.dysni;

import java.util.Collection;

import de.hpi.idd.dysni.avl.AVLTree;
import de.hpi.idd.dysni.window.WindowBuilder;

public class DySNIndex<RECORD, KEY extends Comparable<KEY>, ID> {

	private final KeyHandler<RECORD, KEY> keyHandler;
	private final AVLTree<KEY, ID> tree = new AVLTree<>();
	private final WindowBuilder<RECORD, KEY, ID> windowBuilder;

	public DySNIndex(DySNIndexConfiguration<RECORD, KEY, ID> conf) {
		this.keyHandler = conf.getHandler();
		this.windowBuilder = conf.getBuilder();
	}

	public Collection<ID> findCandidates(RECORD rec) {
		return windowBuilder.buildWindow(rec, tree.find(keyHandler.computeKey(rec)));
	}

	public void insert(RECORD element, ID value) {
		tree.insert(keyHandler.computeKey(element), value);
	}
}
