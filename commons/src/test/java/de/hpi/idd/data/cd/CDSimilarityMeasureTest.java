package de.hpi.idd.data.cd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.hpi.idd.data.cd.CDDataset.CDRecord;
import de.hpi.idd.data.cd.CDDataset.Attribute;

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
		CDDataset cd = new CDDataset();
		Map<String, Double> similarityMap = cd.getSimilarityOfRecords(firstRecord, secondRecord);
		assertEquals(similarityMap.get(Attribute.ARTIST.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.TITLE.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.CATEGORY.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.GENRE.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.CDEXTRA.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.YEAR.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.TRACKS.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(cd.similarity(firstRecord, secondRecord), 1.0, DOUBLE_TOLERANCE);
		List<String> secondTracklist = new LinkedList<>();
		secondTracklist.add("Track 3");
		secondTracklist.add("Track 4");
		secondTracklist.add("Track 5");
		secondTracklist.add("Track 6");
		secondRecord = new CDRecord("1", "The Rolling Tones", "Overpriced Best CD", "trash", "Pop", "None",
				(short) 2017, secondTracklist);
		similarityMap = cd.getSimilarityOfRecords(firstRecord, secondRecord);
		assertEquals(similarityMap.get(Attribute.ARTIST.getName()), 17.0 / 18, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.TITLE.getName()), 17.0 / 18, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.CATEGORY.getName()), 1.0 / 5, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.GENRE.getName()), 1.0 / 4, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.CDEXTRA.getName()), 1.0, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.YEAR.getName()), 89.0 / 90, DOUBLE_TOLERANCE);
		assertEquals(similarityMap.get(Attribute.TRACKS.getName()), 1.0 / 6, DOUBLE_TOLERANCE);
		double sim = cd.similarity(firstRecord, secondRecord);
		assertTrue(0.0 <= sim);
		assertTrue(sim <= 1.0);
	}
}
