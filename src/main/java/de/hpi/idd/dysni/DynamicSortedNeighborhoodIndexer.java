package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.hpi.idd.EntityResolver;
import de.hpi.idd.dysni.sim.SimilarityClassifier;
import de.hpi.idd.dysni.store.RecordStore;
import de.hpi.idd.dysni.store.StoreException;
import de.hpi.idd.dysni.util.UnionFind;

public class DynamicSortedNeighborhoodIndexer<RECORD, ID> implements EntityResolver<RECORD, ID> {

	private final Collection<DySNIndex<RECORD, ?, ID>> indexes = new ArrayList<>();
	private final SimilarityClassifier<RECORD> sim;
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public DynamicSortedNeighborhoodIndexer(RecordStore<ID, RECORD> store, SimilarityClassifier<RECORD> sim,
			Collection<DySNIndexConfiguration<RECORD, ?, ID>> configs) {
		this.store = store;
		this.sim = sim;
		for (DySNIndexConfiguration<RECORD, ?, ID> conf : configs) {
			indexes.add(new DySNIndex<>(conf));
		}
	}

	@Override
	public void add(RECORD rec, ID recId) throws StoreException {
		store.storeRecord(recId, rec);
		for (DySNIndex<RECORD, ?, ID> index : indexes) {
			index.insert(rec, recId);
		}
	}

	@Override
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
