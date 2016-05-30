package de.hpi.idd.dysni;

import de.hpi.idd.dysni.sim.SimilarityMeasure;

public interface KeyHandler<ELEMENT, KEY extends Comparable<KEY>> {

	KEY computeKey(ELEMENT rec);

	SimilarityMeasure<KEY> getSimilarityMeasure();

	double getThreshold();
}
