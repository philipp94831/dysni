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

        Map<String, Integer> similarityMap = RecordComparator.getSimilarityOfRecords(firstRecord, secondRecord);

        assertEquals(similarityMap.get("artistSimilarity"), (Integer) 0);
        assertEquals(similarityMap.get("dTitleSimilarity"), (Integer) 0);
        assertEquals(similarityMap.get("categorySimilarity"), (Integer) 0);
        assertEquals(similarityMap.get("genreSimilarity"), (Integer) 0);
        assertEquals(similarityMap.get("cdExtraSimilarity"), (Integer) 0);
        assertEquals(similarityMap.get("yearSimilarity"), (Integer) 0);
        assertEquals(similarityMap.get("tracksSimilarity"), (Integer) firstRecord.getTracks().size());

        List<String> secondTracklist = new LinkedList<>();
        secondTracklist.add("Track 4");
        secondTracklist.add("Track 5");
        secondTracklist.add("Track 6");
        secondRecord = new CDRecord("1", "The Rolling Tones", "Overpriced Best CD", "trash", "Pop", "None", (short) 2017, secondTracklist);

        similarityMap = RecordComparator.getSimilarityOfRecords(firstRecord, secondRecord);

        assertEquals(similarityMap.get("artistSimilarity"), (Integer) 1);
        assertEquals(similarityMap.get("dTitleSimilarity"), (Integer) 1);
        assertEquals(similarityMap.get("categorySimilarity"), (Integer) 4);
        assertEquals(similarityMap.get("genreSimilarity"), (Integer) 3);
        assertEquals(similarityMap.get("cdExtraSimilarity"), (Integer) 0);
        assertEquals(similarityMap.get("yearSimilarity"), (Integer) 1);
        assertEquals(similarityMap.get("tracksSimilarity"), (Integer) 0);
    }

}
