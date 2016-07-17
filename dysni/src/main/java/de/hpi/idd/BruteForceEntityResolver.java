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
	public void insert(RECORD rec, ID recId) throws StoreException {
		store.storeRecord(recId, rec);
	}

	@Override
	public Collection<ID> resolve(RECORD rec, ID recId) throws StoreException {
		List<ID> matches = StreamSupport.stream(store.spliterator(), true)
				.filter(candidate -> !recId.equals(candidate.getKey()) && sim.areSimilar(rec, candidate.getValue()))
				.map(Entry::getKey).collect(Collectors.toList());
		for (ID match : matches) {
			uf.union(recId, match);
		}
		return uf.getComponent(recId);
	}

}
