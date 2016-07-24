package de.hpi.idd.dysni;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.hpi.idd.EntityResolver;
import de.hpi.idd.sim.SimilarityClassifier;
import de.hpi.idd.store.RecordStore;
import de.hpi.idd.store.StoreException;
import de.hpi.idd.util.UnionFind;

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
	/**
	 * indicates whether parallel execution of the similarity function should be
	 * used
	 */
	private boolean parallelizable = true;
	/** similarity classifier to determine duplicates */
	private final SimilarityClassifier<RECORD> sim;
	/** external store to retrieve records by their id */
	private final RecordStore<ID, RECORD> store;
	/** Union find data structure to ensure transitivity of similarity */
	private final UnionFind<ID> uf = new UnionFind<>();
	private int comparisons = 0;
	
	public int getComparisons() {
		return comparisons;
	}

	/**
	 * Construct a new DySNIndexer with the specified store and similarity
	 * measure to be used.
	 *
	 * @param store
	 *            the external store where records should be put
	 * @param sim
	 *            the similarity classifier to determine duplicates
	 */
	public DynamicSortedNeighborhoodIndexer(RecordStore<ID, RECORD> store, SimilarityClassifier<RECORD> sim) {
		this.store = store;
		this.sim = sim;
	}

	/**
	 * Add an index to the tree using the specified configuration.
	 *
	 * @param config
	 *            the configuration specifying the index configuration
	 * @return this
	 */
	public DynamicSortedNeighborhoodIndexer<RECORD, ID> addIndex(DySNIndexConfiguration<RECORD, ?, ID> config) {
		indexes.add(new DySNIndex<>(config));
		return this;
	}

	/**
	 * Add multiple indexes to the tree using the specified configurations.
	 *
	 * @param configs
	 *            The configurations for the indexes to be added. For each
	 *            configuration <b> exactly </b> one index will be created.
	 * @return this
	 */
	public DynamicSortedNeighborhoodIndexer<RECORD, ID> addIndexes(
			Collection<DySNIndexConfiguration<RECORD, ?, ID>> configs) {
		for (DySNIndexConfiguration<RECORD, ?, ID> config : configs) {
			addIndex(config);
		}
		return this;
	}

	@Override
	public void close() throws StoreException {
		store.close();
	}

	/**
	 * Add a new record to the indexer. Stores the record and inserts it into
	 * all indexes.
	 */
	@Override
	public void insert(RECORD rec, ID recId) throws StoreException {
		store.storeRecord(recId, rec);
		for (DySNIndex<RECORD, ?, ID> index : indexes) {
			index.insert(rec, recId);
		}
	}

	/**
	 * Whether similarity function will be executed in parallel or not
	 *
	 * @return true, if similarity function will be executed in parallel
	 */
	public boolean isParallelizable() {
		return parallelizable;
	}

	/**
	 * Find duplicates by retrieving candidates from each index and comparing
	 * the record to each of these candidates using the similarity measure.
	 * Similar records are connected in the Union Find data structure.
	 */
	@Override
	public Collection<ID> resolve(RECORD rec, ID recId) {
		Set<ID> candidates = new HashSet<>();
		for (DySNIndex<RECORD, ?, ID> index : indexes) {
			candidates.addAll(index.findCandidates(rec));
		}
		candidates.remove(recId);
		comparisons += candidates.size();
		Set<ID> matches = streamCandidates(candidates).filter(candidate -> {
			try {
				RECORD candidateRec = store.getRecord(candidate);
				return sim.areSimilar(rec, candidateRec);
			} catch (StoreException e) {
				throw new RuntimeException("Error retrieving element from store", e);
			}
		}).collect(Collectors.toSet());
		for (ID match : matches) {
			uf.union(recId, match);
		}
		Collection<ID> component = uf.getComponent(recId);
		component.remove(recId);
		return component;
	}

	/**
	 * Set whether similarity function can be executed in parallel or not.
	 * Parallelization usually results in great speed up but is not always
	 * possible.
	 *
	 * @param parallelizable
	 *            whether parallel execution of similarity function should be
	 *            used
	 * @return this
	 */
	public DynamicSortedNeighborhoodIndexer<RECORD, ID> setParallelizable(boolean parallelizable) {
		this.parallelizable = parallelizable;
		return this;
	}

	/**
	 * Stream candidates based on the parallelization configuration
	 *
	 * @param candidates
	 *            candidates to stream
	 * @return stream of candidates, parallel if {@link #parallelizable} true
	 */
	private Stream<ID> streamCandidates(Set<ID> candidates) {
		if (parallelizable) {
			return candidates.parallelStream();
		} else {
			return candidates.stream();
		}
	}
	
	public List<Integer> indexSizes() {
		return indexes.stream().map(DySNIndex::size).collect(Collectors.toList());
	}
}
