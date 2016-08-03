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

	private int comparisons = 0;
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

	/**
	 * Check a record and a candidate for similarity. This method may produce a
	 * {@link RuntimeException} if the candidate cannot be retrieved from the
	 * store. Since Java 8 streams do not support exceptions, this case cannot
	 * be caught.
	 *
	 * @param record
	 *            the record for which similarity should be resolved
	 * @param candidate
	 *            a potential duplicate represented by its unique id
	 * @return true if the record and the candidate are determined similar by
	 *         the similarity measure
	 */
	private boolean areSimilar(RECORD record, ID candidate) {
		try {
			RECORD candidateRec = store.getRecord(candidate);
			return sim.areSimilar(record, candidateRec);
		} catch (StoreException e) {
			throw new RuntimeException("Error retrieving element from store", e);
		}
	}

	@Override
	public void close() throws StoreException {
		store.close();
	}

	/**
	 * Retrieve candidates for possible similarity to a given record from each
	 * index.
	 *
	 * @param record
	 *            the record for which potential duplicates should be resolved
	 * @return ids of potential duplicates
	 */
	private Set<ID> findCandidates(RECORD record) {
		return indexes.stream().flatMap(index -> index.findCandidates(record).stream()).collect(Collectors.toSet());
	}

	public int getComparisons() {
		return comparisons;
	}

	public List<Integer> indexSizes() {
		return indexes.stream().map(DySNIndex::size).collect(Collectors.toList());
	}

	/**
	 * Add a new record to the indexer. Stores the record and inserts it into
	 * all indexes.
	 */
	@Override
	public void insert(RECORD record, ID recordId) throws StoreException {
		store.storeRecord(recordId, record);
		for (DySNIndex<RECORD, ?, ID> index : indexes) {
			index.insert(record, recordId);
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
	 * Check candidates for similarity to a given record. This is usually the
	 * most expensive part of DySNI. To improve performance, it is recommended
	 * to check whether parallelizing the calls to the similarity measure is
	 * helpful.
	 *
	 * @param record
	 *            the record for which similar records should be resolved
	 * @param candidates
	 *            potential duplicates represented by their unique id
	 * @return ids of similar records
	 */
	private Set<ID> matchCandidates(RECORD record, Set<ID> candidates) {
		return stream(candidates).filter(candidate -> areSimilar(record, candidate)).collect(Collectors.toSet());
	}

	/**
	 * Find duplicates by retrieving candidates from each index and comparing
	 * the record to each of these candidates using the similarity measure.
	 * Similar records are connected in the Union Find data structure.
	 */
	@Override
	public Collection<ID> resolve(RECORD record, ID recordId) {
		Set<ID> candidates = findCandidates(record);
		candidates.remove(recordId);
		comparisons += candidates.size();
		Set<ID> matches = matchCandidates(record, candidates);
		for (ID match : matches) {
			uf.union(recordId, match);
		}
		Collection<ID> component = uf.getComponent(recordId);
		component.remove(recordId);
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
	private <T> Stream<T> stream(Set<T> candidates) {
		if (parallelizable) {
			return candidates.parallelStream();
		} else {
			return candidates.stream();
		}
	}
}
