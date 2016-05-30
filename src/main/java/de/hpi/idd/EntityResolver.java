package de.hpi.idd;

import java.util.Collection;

public interface EntityResolver<RECORD, ID> {

	void add(RECORD rec, ID recId) throws Exception;

	Collection<ID> findDuplicates(RECORD rec, ID recId) throws Exception;

}
