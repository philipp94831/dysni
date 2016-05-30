package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.hpi.idd.dysni.sim.SimilarityAssessor;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.UnionFind;

public class DynamicSortedNeighborhoodIndexer<RECORD, ID> {

	private final Collection<DySNIndex<RECORD, ?, ID>> indexes = new ArrayList<>();
	private final SimilarityAssessor<RECORD> sim;
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public DynamicSortedNeighborhoodIndexer(RecordStore<ID, RECORD> store, SimilarityAssessor<RECORD> sim,
			Collection<DySNIndexConfiguration<RECORD, ?, ID>> configs) {
		this.store = store;
		this.sim = sim;
		for (DySNIndexConfiguration<RECORD, ?, ID> conf : configs) {
			indexes.add(new DySNIndex<>(conf));
		}
	}

	public void add(RECORD rec, ID recId) throws StoreException {
		store.storeRecord(recId, rec);
		for (DySNIndex<RECORD, ?, ID> index : indexes) {
			index.insert(rec, recId);
		}
	}

	public Collection<ID> findDuplicates(RECORD rec, ID recId) throws StoreException {
		Set<ID> candidates = new HashSet<>();
		for (DySNIndex<RECORD, ?, ID> index : indexes) {
			candidates.addAll(index.findCandidates(rec));
		}
		candidates.remove(recId);
		for (ID candidate : candidates) {
			if (sim.areSimilar(rec, store.getRecord(candidate))) {
				uf.union(recId, candidate);
			}
		}
		return uf.getComponent(recId);
	}
}
