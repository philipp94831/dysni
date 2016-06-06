package de.hpi.idd.dysni;

/**
 * Interface to compute a sorting key of a record
 *
 * @param <RECORD>
 *            the type of records whose key should be computed
 * @param <KEY>
 *            the type of the keys computed
 */
public interface KeyHandler<RECORD, KEY extends Comparable<KEY>> {

	/**
	 * Compute the key for a given record
	 *
	 * @param rec
	 *            the record
	 * @return the computed key
	 */
	KEY computeKey(RECORD rec);
}
