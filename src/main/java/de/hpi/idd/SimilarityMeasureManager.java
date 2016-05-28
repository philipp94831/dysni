package de.hpi.idd;

import de.hpi.idd.cd.CDRecordComparator;

public class SimilarityMeasureManager {

	public static SimilarityMeasure getSimilarityMeasure(final Dataset dataset) {
		switch (dataset) {
		case CD:
			return new CDRecordComparator();
		default:
			return null;
		}
	}
}
