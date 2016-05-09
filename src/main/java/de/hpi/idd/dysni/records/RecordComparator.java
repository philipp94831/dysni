package de.hpi.idd.dysni.records;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dennis on 08.05.16.
 */
public class RecordComparator {

    public static Map<String, Float> getSimilarityOfRecords(CDRecord firstRecord, CDRecord secondRecord) {
        Map<String, Float> similarityMap = new HashMap<>();

        similarityMap.put("artistSimilarity", levenshteinDistance(firstRecord.getArtist(), secondRecord.getArtist()));
        similarityMap.put("dTitleSimilarity", levenshteinDistance(firstRecord.getdTitle(), secondRecord.getdTitle()));
        similarityMap.put("categorySimilarity", levenshteinDistance(firstRecord.getCategory(), secondRecord.getCategory()));
        similarityMap.put("genreSimilarity", levenshteinDistance(firstRecord.getGenre(), secondRecord.getGenre()));
        similarityMap.put("yearSimilarity", new Float(Math.abs(firstRecord.getYear() - secondRecord.getYear())));
        similarityMap.put("cdExtraSimilarity", levenshteinDistance(firstRecord.getCdExtra(), secondRecord.getCdExtra()));
        similarityMap.put("tracksSimilarity", getSimilarityOfTracks(firstRecord.getTracks(), secondRecord.getTracks()));

        return similarityMap;
    }

    private static float getSimilarityOfTracks(List<String> firstTracklist, List<String> secondTracklist) {
        int counter = 0;

        for(String track: firstTracklist) {
            if (secondTracklist.contains(track)) {
                counter++;
            }
        }

        List<String> mergedTracklist = new LinkedList<>();
        mergedTracklist.addAll(firstTracklist);
        mergedTracklist.addAll(secondTracklist);
        HashSet<String> mergedTrackset = new HashSet<>(firstTracklist);

        float similarity = (float) counter/ (float) mergedTrackset.size();
        return similarity;
    }

    public static Float levenshteinDistance(String a, String b) {
        return (float) StringUtils.getLevenshteinDistance(a.toLowerCase(), b.toLowerCase());
    }
}
