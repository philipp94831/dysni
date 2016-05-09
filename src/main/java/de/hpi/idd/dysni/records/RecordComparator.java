package de.hpi.idd.dysni.records;

import java.util.*;

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
        HashSet mergedTrackset = new HashSet<>(firstTracklist);

        float similarity = (float) counter/ (float) mergedTrackset.size();
        return similarity;
    }

    public static Float levenshteinDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();

        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return new Float(costs[b.length()]);
    }
}
