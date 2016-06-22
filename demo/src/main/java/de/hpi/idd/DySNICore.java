package de.hpi.idd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.hpi.idd.data.Dataset;
import de.hpi.idd.dysni.DynamicSortedNeighborhoodIndexer;
import de.hpi.idd.store.MemoryStore;
import de.hpi.idd.store.RecordStore;
import de.hpi.idd.store.StoreException;

public class DySNICore implements Core {

	private static final Logger LOGGER = Logger.getLogger(DySNICore.class.getName());
	private EntityResolver<Map<String, Object>, String> er;
	private RecordStore<String, Map<String, Object>> store;

	@Override
	public void buildIndex(Map<String, String> parameters) {
		Dataset dataset = Dataset.getForName(parameters.get("dataset"));
		store = new MemoryStore<>();
		er = new DynamicSortedNeighborhoodIndexer<>(store, new IDDSimilarityClassifier(dataset.getDataset()),
				dataset.getConfigs());
	}

	@Override
	public boolean destroyIndex(Map<String, String> parameters) {
		try {
			store.close();
			return true;
		} catch (IOException e) {
			LOGGER.warning("Error closing store: " + e.getMessage());
		}
		return false;
	}

	@Override
	public List<String> getDuplicates(Map<String, Object> record, Map<String, String> parameters) {
		String id = (String) record.get(Dataset.ID);
		try {
			return new ArrayList<>(er.resolve(record, id));
		} catch (StoreException e) {
			LOGGER.severe("Error resolving record " + id + ": " + e);
		}
		return Collections.emptyList();
	}

	@Override
	public Map<String, Object> getRecord(String recordID) {
		try {
			return store.getRecord(recordID);
		} catch (StoreException e) {
			LOGGER.severe("Error getting record " + recordID + ": " + e);
		}
		return null;
	}

	@Override
	public boolean insertRecord(Map<String, Object> record, Map<String, String> parameters) {
		String id = (String) record.get(Dataset.ID);
		try {
			er.insert(record, id);
			return true;
		} catch (StoreException e) {
			LOGGER.severe("Error inserting record " + id + ": " + e);
		}
		return false;
	}

}
