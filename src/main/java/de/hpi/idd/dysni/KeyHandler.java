package de.hpi.idd.dysni;

import de.hpi.idd.dysni.sim.SimilarityAssessor;

public interface KeyHandler<ELEMENT, KEY extends Comparable<KEY>> {

	KEY computeKey(ELEMENT rec);

	SimilarityAssessor<KEY> getSimilarityMeasure();
}
