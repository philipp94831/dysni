package de.hpi.idd.dysni.records;

import de.hpi.idd.dysni.records.CDRecord;
import de.hpi.idd.dysni.records.RecordComparator;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by dennis on 08.05.16.
 */
public class RecordComparatorTest {

    @Test
    public void testGetSimilarityOfRecords() {
        List<String> firstTracklist = new LinkedList<>();
        firstTracklist.add("Track 1");
        firstTracklist.add("Track 2");
        firstTracklist.add("Track 3");
        CDRecord firstRecord = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None", (short) 2016, firstTracklist);
        CDRecord secondRecord = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None", (short) 2016, firstTracklist);

        Map<String, Float> similarityMap = RecordComparator.getSimilarityOfRecords(firstRecord, secondRecord);

        assertEquals(similarityMap.get("artistSimilarity"), new Float(0.0));
        assertEquals(similarityMap.get("dTitleSimilarity"), new Float(0.0));
        assertEquals(similarityMap.get("categorySimilarity"), new Float(0.0));
        assertEquals(similarityMap.get("genreSimilarity"), new Float(0.0));
        assertEquals(similarityMap.get("cdExtraSimilarity"), new Float(0.0));
        assertEquals(similarityMap.get("yearSimilarity"), new Float(0.0));
        assertEquals(similarityMap.get("tracksSimilarity"), new Float(1.0));

        List<String> secondTracklist = new LinkedList<>();
        secondTracklist.add("Track 4");
        secondTracklist.add("Track 5");
        secondTracklist.add("Track 6");
        secondRecord = new CDRecord("1", "The Rolling Tones", "Overpriced Best CD", "trash", "Pop", "None", (short) 2017, secondTracklist);

        similarityMap = RecordComparator.getSimilarityOfRecords(firstRecord, secondRecord);

        assertEquals(similarityMap.get("artistSimilarity"), new Float(1.0));
        assertEquals(similarityMap.get("dTitleSimilarity"), new Float(1.0));
        assertEquals(similarityMap.get("categorySimilarity"), new Float(4.0));
        assertEquals(similarityMap.get("genreSimilarity"), new Float(3.0));
        assertEquals(similarityMap.get("cdExtraSimilarity"), new Float(0.0));
        assertEquals(similarityMap.get("yearSimilarity"), new Float(1.0));
        assertEquals(similarityMap.get("tracksSimilarity"), new Float(0.0));
    }

}
