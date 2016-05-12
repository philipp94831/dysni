package de.hpi.idd;


public interface RecordComparator<T> {
	
	boolean areSimilar(T t1, T t2);
}
