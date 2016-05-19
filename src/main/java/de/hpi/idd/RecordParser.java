package de.hpi.idd;

import org.apache.commons.csv.CSVRecord;

public interface RecordParser<T> {
	
	public T parse(CSVRecord record);
}
