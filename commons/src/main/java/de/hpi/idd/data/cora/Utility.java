package de.hpi.idd.data.cora;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.simmetrics.StringDistance;
import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringDistanceBuilder;
import org.simmetrics.metrics.EuclideanDistance;
import org.simmetrics.metrics.StringDistances;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.tokenizers.Tokenizers;

import de.hpi.idd.DatasetUtils;

/**
 * Created by axel on 21.05.16.
 */
public class Utility extends DatasetUtils{

    public static StringMetric levenshtein = StringMetrics.levenshtein();
    public static StringMetric jaroWinkler = StringMetrics.jaroWinkler();
    public static StringMetric longestCommonSubsequence = StringMetrics.longestCommonSubsequence();
    public static StringMetric mongeElkan = StringMetrics.mongeElkan();
    public static StringMetric jaccard = StringMetrics.generalizedJaccard();
    public static final String filepath = "/media/axel/uni/idd/repo/irl/data/cora_ground_truth.csv"; // path to ground truth

    protected double datasetThreshold = 0.8;

    public double getDatasetThreshold() {
        return datasetThreshold;
    }

    public void setDatasetThreshold(double datasetThreshold) {
        this.datasetThreshold = datasetThreshold;
    }

    /**
     *
     * Given two records, return their similarity in the range of [0,1].
     *
     * @param record1
     * @param record2
     * @param parameters: You could pass your parameters in a key, value form.
     * @return: The similarity in a double value of a range [0,1].
     */
    public Double calculateSimilarity(Map<String, Object> record1, Map<String, Object> record2, Map<String, String> parameters){
        Double sim = 0.0;
        try {
            Float sim1 = mongeElkan.compare(record1.get("Authors").toString(), record2.get("Authors").toString());
            Float sim2 = levenshtein.compare(record1.get("title").toString(), record2.get("title").toString());
            Float sim3 = longestCommonSubsequence.compare(record1.get("pages").toString(), record2.get("pages").toString());
            sim = (double) (2 * sim1 * sim2) / (sim1 + sim2);
            if (sim3<0.5){
                sim -= 0.1;
            }
            if (record1.get("tech").toString().length() > 1 && record2.get("bookTitle").toString().length() > 1) {
                sim -= 1;
            }
            if (record2.get("tech").toString().length() > 1 && record1.get("bookTitle").toString().length() > 1) {
                sim -= 1;
            }
            return sim;
        }catch(Exception e){
            e.printStackTrace();
            return sim;
        }
    }

    /**
     *
     * @param values in a Key-Value <String, String> format.
     * 			For instance: <'id', '1'>, <'attribute1', 'value1'>, <'attribute2', 'value2'>, ..., <'attributeN', valueN'>
     * @return A dictionary with key-value objects: e.g. <attribute1, value1>
     * 			Each value can be of any type, thus it is Object (and not String).
     */
    public Map<String, Object> parseRecord(Map<String, String> values){
        Map<String, Object> asd = new HashMap<>();
        asd.putAll(values);
        return asd;
    }

    /**
     *
     * @param filePath: file to cora.csv
     * @return list of records of type Map<String, Object> found in the csv file
     * @throws IOException
     */
    public static ArrayList<Map<String, Object>> processCSV(String filePath) throws IOException {
        ArrayList<Map<String, Object>> records = new ArrayList<>(
        );
        String stringRead ="";
        try
        {
            int count = 1;
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            stringRead = br.readLine();
            ArrayList<String> fields = new ArrayList<>();
            String[] splittetFields = stringRead.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            for (String field : splittetFields){
                fields.add(field.replace("\"", "").trim());
            }

            stringRead = br.readLine();

            while( stringRead != null )
            {
                if (count == 1187){
                    System.out.println("hello");
                }
                String[] splittetString = stringRead.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                Map<String, Object> record = new HashMap<>();
                for (int i=0; i<splittetString.length && i<splittetFields.length; i++){
                    record.put(fields.get(i), splittetString[i].replace("\"", "").trim());
                }
                records.add(record);
                stringRead = br.readLine();
                count ++;
            }
            br.close( );
        }
        catch (Exception e){
            System.out.println(stringRead);
            e.printStackTrace();
        }
        return records;


    }

    public static void compareWithGroundTruth(ArrayList<Integer[]> duplicatesFound){
        ArrayList<Integer[]> groundTruth = getGroundTruth();
        int tp = 0;
        int max = 0;
        for (Integer[] entry2: duplicatesFound){
            if (entry2[1]>max){
                max = entry2[1];
            }
        }
        System.out.println(max);
        int fn = 0;
        for (Integer[] entry: groundTruth){
            if (entry[0]>=max){
                break;
            }
            if (entry[1]<=max){
                fn += 1;
            }
            for (Integer[] entry2: duplicatesFound){
                if (Arrays.equals(entry, entry2)){
                    tp+=1;
                    fn -=1;
                    break;
                }

            }
        }
        int fp = 0;
        for (Integer[] entry: duplicatesFound){
            if (entry[0]>=max){
                break;
            }
            if (entry[1]<=max){
                fp += 1;
            }
            Boolean foundOne2 = false;
            for (Integer[] entry2: groundTruth){
                if (Arrays.equals(entry, entry2)){
                    fp-=1;
                    break;
                }

            }
        }

        System.out.println("tp: " + tp);
        System.out.println("fn: " + fn);
        System.out.println("fp: " + fp);
        float precision = tp / (float)(tp+fp);
        float recall = tp / (float)(tp+fn);
        float f1 = 2*precision*recall / (precision+recall);
        System.out.println("precicion: " + precision);
        System.out.println("recall: " + recall);
        System.out.println("f1: " + f1);
    }

    private static ArrayList<Integer[]> getGroundTruth(){
        ArrayList<Integer[]> dups = new ArrayList<>();
        try
        {
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);
            String stringRead = br.readLine();
            stringRead = br.readLine();
            while( stringRead != null )
            {
                Integer[] pair = new Integer[2];
                ArrayList<String> record = new ArrayList<>();
                StringTokenizer st = new StringTokenizer(stringRead, ";");
                Integer id1 = Integer.parseInt(st.nextToken( ));
                Integer id2 = Integer.parseInt(st.nextToken( ));
                pair[0]=id1;
                pair[1]=id2;
                dups.add(pair);

                // read the next line
                stringRead = br.readLine();
            }
            br.close( );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Collections.sort(dups, new Comparator<Integer[]>() {
            public int compare(Integer[] ints, Integer[] otherints) {
                return ints[1].compareTo(otherints[1]);
            }
        });
        Collections.sort(dups, new Comparator<Integer[]>() {
            public int compare(Integer[] ints, Integer[] otherints) {
                return ints[0].compareTo(otherints[0]);
            }
        });
        return dups;
    }

}
