package de.hpi.idd.data.cd;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.hpi.idd.data.cd.CDSimilarityMeasure.CDRecord;
import de.hpi.idd.data.cd.CDSimilarityMeasure.CDSimilarity;

/**
 * Created by dennis on 08.05.16.
 */
public class CDSimilarityMeasureTest {

	private static final double DOUBLE_TOLERANCE = 0.001;

	@Test
	public void testGetSimilarityOfRecords() {
		List<String> firstTracklist = new LinkedList<>();
		firstTracklist.add("Track 1");
		firstTracklist.add("Track 2");
		firstTracklist.add("Track 3");
		CDRecord firstRecord = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None",
				(short) 2016, firstTracklist);
		CDRecord secondRecord = new CDRecord("1", "The Rolling Stones", "Overpriced Test CD", "data", "Rock", "None",
				(short) 2016, firstTracklist);
		Map<CDSimilarity, Double> similarityMap = CDSimilarityMeasure.getSimilarityOfRecords(firstRecord, secondRecord);
		Assert.assertEquals(similarityMap.get(CDSimilarity.ARTIST), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.DTITLE), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.CATEGORY), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.GENRE), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.CDEXTRA), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.YEAR), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.TRACK), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(CDSimilarityMeasure.similarity(firstRecord, secondRecord), 1.0,
				CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		List<String> secondTracklist = new LinkedList<>();
		secondTracklist.add("Track 3");
		secondTracklist.add("Track 4");
		secondTracklist.add("Track 5");
		secondTracklist.add("Track 6");
		secondRecord = new CDRecord("1", "The Rolling Tones", "Overpriced Best CD", "trash", "Pop", "None",
				(short) 2017, secondTracklist);
		similarityMap = CDSimilarityMeasure.getSimilarityOfRecords(firstRecord, secondRecord);
		Assert.assertEquals(similarityMap.get(CDSimilarity.ARTIST), 17.0 / 18,
				CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.DTITLE), 17.0 / 18,
				CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.CATEGORY), 1.0 / 5,
				CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.GENRE), 1.0 / 4, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.CDEXTRA), 1.0, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.YEAR), 89.0 / 90, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		Assert.assertEquals(similarityMap.get(CDSimilarity.TRACK), 1.0 / 6, CDSimilarityMeasureTest.DOUBLE_TOLERANCE);
		double sim = CDSimilarityMeasure.similarity(firstRecord, secondRecord);
		Assert.assertTrue(0.0 <= sim);
		Assert.assertTrue(sim <= 1.0);
	}
}
