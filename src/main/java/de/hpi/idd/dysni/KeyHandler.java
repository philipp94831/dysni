package de.hpi.idd.dysni;

public interface KeyHandler<ELEMENT, KEY extends Comparable<KEY>> {

	KEY computeKey(ELEMENT rec);
}
