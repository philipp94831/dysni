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

/**
 * Entity resolver based on the Sorted Neighborhood approach. In contrast to the
 * static SN approach, the records are stored in already sorted indexes which
 * guarantee fast insertion and retrieval of nodes. Only the identifiers of
 * nodes are stored so that the actual records need to be retrieved from an
 * external storage. Duplicates are resolved using a
 * {@link SimilarityClassifier}. The transitivity of similarity is ensured using
 * a {@link UnionFind} data structure.
 *
 * @param <RECORD>
 *            type of records to be resolved
 * @param <ID>
 *            type of identifiers for the records
 */
public class DynamicSortedNeighborhoodIndexer<RECORD, ID> implements EntityResolver<RECORD, ID> {

	/**
	 * the different indexes, each with a specific key function and window
	 * builder
	 */
	private final Collection<DySNIndex<RECORD, ?, ID>> indexes = new ArrayList<>();
	/** similarity classifier to determine duplicates */
	private final SimilarityClassifier<RECORD> sim;
	/** external store to retrieve records by their id */
	private final RecordStore<ID, RECORD> store;
	/** Union find data structure to ensure transitivity of similarity */
	private final UnionFind<ID> uf = new UnionFind<>();

	/**
	 * Construct a new DySNIndexer with the specified store, similarity measure
	 * and the configuration for the different indexes to be used.
	 *
	 * @param store
	 *            the external store where records should be put
	 * @param sim
	 *            the similarity classifier to determine duplicates
	 * @param configs
	 *            the configurations for the different indexes. For each
	 *            configuration, <b>exactly one</b> index is created.
	 */
	public DynamicSortedNeighborhoodIndexer(RecordStore<ID, RECORD> store, SimilarityClassifier<RECORD> sim,
			Collection<DySNIndexConfiguration<RECORD, ?, ID>> configs) {
		this.store = store;
		this.sim = sim;
		for (DySNIndexConfiguration<RECORD, ?, ID> conf : configs) {
			indexes.add(new DySNIndex<>(conf));
		}
	}

	/**
	 * Add a new record to the indexer. Stores the record and inserts it into
	 * all indexes.
	 */
	@Override
	public void add(RECORD rec, ID recId) throws StoreException {
		store.storeRecord(recId, rec);
		for (DySNIndex<RECORD, ?, ID> index : indexes) {
			index.insert(rec, recId);
		}
	}

	/**
	 * Find duplicates by retrieving candidates from each index and comparing
	 * the record to each of these candidates using the similarity measure.
	 * Similar records are connected in the Union Find data structure.
	 */
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
