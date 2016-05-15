package de.hpi.idd.dysni.records;

import de.hpi.idd.dysni.records.CDRecord;
import de.hpi.idd.dysni.records.CDRecordComparator;
import de.hpi.idd.dysni.records.CDRecordComparator.CDSimilarity;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dennis on 08.05.16.
 */
public class CDRecordComparatorTest {
	
	private static final double DOUBLE_TOLERANCE = 0.001;

    @Test
    public void testGetSimilarityOfRecords() {
        List<String> firstTracklist = new LinkedList<>();
        firstTracklist.add("Track 1");
        firstTracklist.add("Track 2");
        firstTracklist.add("Track 3");
        CDRecord firstRecord = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None", (short) 2016, firstTracklist);
        CDRecord secondRecord = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None", (short) 2016, firstTracklist);

        Map<CDSimilarity, Double> similarityMap = CDRecordComparator.getSimilarityOfRecords(firstRecord, secondRecord);

        assertEquals(similarityMap.get(CDSimilarity.ARTIST), 1.0, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.DTITLE), 1.0, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.CATEGORY), 1.0, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.GENRE), 1.0, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.CDEXTRA), 1.0, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.YEAR), 1.0, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.TRACK), 1.0, DOUBLE_TOLERANCE);
        assertEquals(CDRecordComparator.similarity(firstRecord, secondRecord), 1.0, DOUBLE_TOLERANCE);

        List<String> secondTracklist = new LinkedList<>();
        secondTracklist.add("Track 3");
        secondTracklist.add("Track 4");
        secondTracklist.add("Track 5");
        secondTracklist.add("Track 6");
        secondRecord = new CDRecord("1", "The Rolling Tones", "Overpriced Best CD", "trash", "Pop", "None", (short) 2017, secondTracklist);

        similarityMap = CDRecordComparator.getSimilarityOfRecords(firstRecord, secondRecord);

        assertEquals(similarityMap.get(CDSimilarity.ARTIST), 17.0 / 18, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.DTITLE), 17.0 / 18, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.CATEGORY), 1.0 / 5, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.GENRE), 1.0 / 4, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.CDEXTRA), 1.0, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.YEAR), 3.0 / 4, DOUBLE_TOLERANCE);
        assertEquals(similarityMap.get(CDSimilarity.TRACK), 1.0 / 6, DOUBLE_TOLERANCE);
        assertEquals(CDRecordComparator.similarity(firstRecord, secondRecord), 383.0 / 630, DOUBLE_TOLERANCE);
    }

}
