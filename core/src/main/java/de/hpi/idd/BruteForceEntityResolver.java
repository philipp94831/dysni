package de.hpi.idd;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import de.hpi.idd.sim.SimilarityClassifier;
import de.hpi.idd.store.RecordStore;
import de.hpi.idd.store.StoreException;
import de.hpi.idd.util.UnionFind;

/**
 * Brute force entity resolver which compares every record to every other
 *
 * @param <RECORD>
 *            the type of records to be resolved
 * @param <ID>
 *            the type of ids representing each record
 */
public class BruteForceEntityResolver<RECORD, ID> implements EntityResolver<RECORD, ID> {

	private boolean parallelizable = true;
	private final SimilarityClassifier<RECORD> sim;
	private final RecordStore<ID, RECORD> store;
	private final UnionFind<ID> uf = new UnionFind<>();

	public BruteForceEntityResolver(RecordStore<ID, RECORD> store, SimilarityClassifier<RECORD> sim) {
		this.store = store;
		this.sim = sim;
	}

	@Override
	public void close() throws StoreException {
		store.close();
	}

	@Override
	public Collection<ID> insert(RECORD record, ID recordId) throws StoreException {
		store.storeRecord(recordId, record);
		return resolve(record, recordId);
	}

	public boolean isParallelizable() {
		return parallelizable;
	}

	@Override
	public Collection<ID> resolve(RECORD record, ID recordId) throws StoreException {
		List<ID> matches = StreamSupport.stream(store.spliterator(), parallelizable).filter(
				candidate -> !recordId.equals(candidate.getKey()) && sim.areSimilar(record, candidate.getValue()))
				.map(Entry::getKey).collect(Collectors.toList());
		for (ID match : matches) {
			uf.union(recordId, match);
		}
		Collection<ID> component = uf.getComponent(recordId);
		component.remove(recordId);
		return component;
	}

	public BruteForceEntityResolver<RECORD, ID> setParallelizable(boolean parallelizable) {
		this.parallelizable = parallelizable;
		return this;
	}

}
