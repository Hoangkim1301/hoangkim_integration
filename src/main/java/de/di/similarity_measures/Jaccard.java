package de.di.similarity_measures;

import de.di.similarity_measures.helper.Tokenizer;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import it.unimi.dsi.fastutil.objects.*;
import it.unimi.dsi.fastutil.ints.*;

@AllArgsConstructor
public class Jaccard implements SimilarityMeasure {

    // The tokenizer that is used to transform string inputs into token lists.
    private final Tokenizer tokenizer;

    // A flag indicating whether the Jaccard algorithm should use set or bag semantics for the similarity calculation.
    private final boolean bagSemantics;

    /**
     * Calculates the Jaccard similarity of the two input strings. Note that the Jaccard similarity may use set or
     * multiset, i.e., bag semantics for the union and intersect operations. The maximum Jaccard similarity with
     * multiset semantics is 1/2 and the maximum Jaccard similarity with set semantics is 1.
     * @param string1 The first string argument for the similarity calculation.
     * @param string2 The second string argument for the similarity calculation.
     * @return The multiset Jaccard similarity of the two arguments.
     */
    @Override
    public double calculate(String string1, String string2) {
        string1 = (string1 == null) ? "" : string1;
        string2 = (string2 == null) ? "" : string2;

        String[] strings1 = this.tokenizer.tokenize(string1);
        String[] strings2 = this.tokenizer.tokenize(string2);
        return this.calculate(strings1, strings2);
    }

    /**
     * Calculates the Jaccard similarity of the two string lists. Note that the Jaccard similarity may use set or
     * multiset, i.e., bag semantics for the union and intersect operations. The maximum Jaccard similarity with
     * multiset semantics is 1/2 and the maximum Jaccard similarity with set semantics is 1.
     * @param strings1 The first string list argument for the similarity calculation.
     * @param strings2 The second string list argument for the similarity calculation.
     * @return The multiset Jaccard similarity of the two arguments.
     */
    @Override
    public double calculate(String[] strings1, String[] strings2) {
        double jaccardSimilarity = 0;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                      DATA INTEGRATION ASSIGNMENT                                           //
        // Calculate the Jaccard similarity of the two String arrays. Note that the Jaccard similarity needs to be    //
        // calculated differently depending on the token semantics: set semantics remove duplicates while bag         //
        // semantics consider them during the calculation. The solution should be able to calculate the Jaccard       //
        // similarity either of the two semantics by respecting the inner bagSemantics flag.                          //

        if (strings1 == null || strings2 == null) {
            return jaccardSimilarity;
        }
        // Empty arrays
        if (strings1.length == 0 && strings2.length == 0) {
            return 1.0;
        }

        if (!bagSemantics) {
            // Set semantics: convert arrays to sets
            Set<String> set1 = new HashSet<>(Arrays.asList(strings1));
            Set<String> set2 = new HashSet<>(Arrays.asList(strings2));

            Set<String> intersection = new HashSet<>(set1);
            intersection.retainAll(set2);

            Set<String> union = new HashSet<>(set1);
            union.addAll(set2);

            jaccardSimilarity = union.isEmpty() ? 1.0 : (double) intersection.size() / union.size();
        } else {
            // Bag (multiset) semantics: count token frequencies
            Map<String, Long> freq1 = Arrays.stream(strings1)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            Map<String, Long> freq2 = Arrays.stream(strings2)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

            Set<String> allTokens = Stream.concat(freq1.keySet().stream(), freq2.keySet().stream())
                    .collect(Collectors.toSet());

            long intersectionCount = 0;
            for (String token : freq1.keySet()) {
                if (freq2.containsKey(token)) {
                    intersectionCount += Math.min(freq1.get(token), freq2.get(token));
                }
            }

            long totalTokens = strings1.length + strings2.length;
            jaccardSimilarity = (double) intersectionCount / totalTokens;
        }
        return jaccardSimilarity;
    }


}
