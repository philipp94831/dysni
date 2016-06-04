package de.hpi.idd.dysni;

import java.util.Collection;

import de.hpi.idd.dysni.avl.BraidedAVLTree;
import de.hpi.idd.dysni.window.WindowBuilder;

public class DySNIndex<RECORD, KEY extends Comparable<KEY>, ID> {

	private final KeyHandler<RECORD, KEY> keyHandler;
	private final BraidedAVLTree<KEY, ID> tree = new BraidedAVLTree<>();
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
