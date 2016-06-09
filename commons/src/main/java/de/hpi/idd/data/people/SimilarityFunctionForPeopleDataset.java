package de.hpi.idd.data.people;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.hpi.idd.DatasetUtils;

import org.simmetrics.StringMetric;
import org.simmetrics.simplifiers.Simplifiers;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

import static org.simmetrics.builders.StringMetricBuilder.with;

public class SimilarityFunctionForPeopleDataset extends DatasetUtils {
    private final static String identifierJSON = "[[{\"attribute\":\"FNAME\",\"similarityFunction\":\"Levenshtein\"}," +
                                                 " {\"attribute\":\"LNAME\",\"similarityFunction\":\"Levenshtein\"}, " +
                                                 "{\"attribute\":\"STADD\",\"similarityFunction\":\"Levenshtein\"}]]";

    private final LinkedList<LinkedList<Attribute>> identifiers;

    public SimilarityFunctionForPeopleDataset() {
        datasetThreshold = 0.5;
        final Gson gson = new Gson();
        final Type type = new TypeToken<LinkedList<LinkedList<Attribute>>>() {
        }.getType();
        identifiers = gson.fromJson(identifierJSON, type);
    }


    /**
     * Given two records, return their similarity in the range of [0,1].
     *
     * @param record1
     * @param record2
     * @param parameters : You could pass your parameters in a key, value form.
     *
     * @return: The similarity in a double value of a range [0,1].
     */
    @Override
    public Double calculateSimilarity(final Map<String, Object> record1, final Map<String, Object> record2,
                                      final Map<String, String> parameters) {
        if (identifiers.stream()
                .anyMatch(identifier -> identifier.stream().map(attribute -> {
                    try {
                        if (attribute.getSimilarityFunction().equals("Equal")) {
                            return record1.get(attribute.getAttribute()).equals(record2.get(attribute.getAttribute()));
                        } else {
                            return SimilarityFunctions.getDistanceForStringMetric(
                                    record1.get(attribute.getAttribute()),
                                    record2.get(attribute.getAttribute()),
                                    attribute.getSimilarityFunction()) > datasetThreshold;
                        }
                    } catch (final ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).reduce(true, (a, b) -> a && b))){
            return 1.0;
        }else{
            return 0.0;
        }
    }

    /**
     * @param values@return A dictionary with key-value objects: e.g. <attribute1, value1> Each value can be of any
     *                      type, thus it is Object (and not String).
     */
    @Override
    public Map<String, Object> parseRecord(final Map<String, String> values) {
        return new HashMap<>(values);
    }


    private class Attribute {
        final private String attribute;
        final private String similarityFunction;

        public Attribute(final String attribute, final String similarityFunction) {
            this.attribute = attribute;
            this.similarityFunction = similarityFunction;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getSimilarityFunction() {
            return similarityFunction;
        }
    }

    private static class SimilarityFunctions {

        static public float getDistanceForStringMetric(final Object str1, final Object str2,
                                                       final String similarityFunction)
                throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            if ((str1 == null) || (str2 == null)) {
                return 0;
            }
            final StringMetric metric =
                    with((StringMetric) Class.forName("org.simmetrics.metrics." + similarityFunction).newInstance())
                            .simplify(Simplifiers.toLowerCase(Locale.ENGLISH))
                            .simplify(Simplifiers.replaceNonWord())
                            .build();
            return metric.compare(str1.toString(), str2.toString());
        }
    }
}
