package de.di.similarity_measures;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public class Levenshtein implements SimilarityMeasure {

    public static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }

    // The choice of whether Levenshtein or DamerauLevenshtein should be calculated.
    private final boolean withDamerau;

    /**
     * Calculates the Levenshtein similarity of the two input strings.
     * The Levenshtein similarity is defined as "1 - normalized Levenshtein distance".
     * @param string1 The first string argument for the similarity calculation.
     * @param string2 The second string argument for the similarity calculation.
     * @return The (Damerau) Levenshtein similarity of the two arguments.
     */
    @Override
    public double calculate(final String string1, final String string2) {
        double levenshteinSimilarity = 0;

        int[] upperupperLine = new int[string1.length() + 1];   // line for Damerau lookups
        int[] upperLine = new int[string1.length() + 1];        // line for regular Levenshtein lookups
        int[] lowerLine = new int[string1.length() + 1];        // line to be filled next by the algorithm

        String s1 = string1 == null ? "" : string1;
        String s2 = string2 == null ? "" : string2;

        int len1 = s1.length();
        int len2 = s2.length();

        if (len1 == 0) return len2 == 0 ? 1.0 : 0.0;
        if (len2 == 0) return 0.0;

        // Fill the first line with the initial positions (= edits to generate string1 from nothing)
        for (int i = 0; i <= string1.length(); i++)
            upperLine[i] = i;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                      DATA INTEGRATION ASSIGNMENT                                           //
        // Use the three provided lines to successively calculate the Levenshtein matrix with the dynamic programming //
        // algorithm. Depending on whether the inner flag withDamerau is set, the Damerau extension rule should be    //
        // used during calculation or not. Hint: Implement the Levenshtein algorithm here first, then copy the code   //
        // to the String tuple function and adjust it a bit to work on the arrays - the algorithm is the same.        //



        //                                                                                                            //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for (int j = 1; j <= string2.length(); j++) {
            lowerLine[0] = j;
            for (int i = 1; i <= len1; i++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                int deletion = upperLine[i] + 1;
                int insertion = lowerLine[i - 1] + 1;
                int substitution = upperLine[i - 1] + cost;

                int value = min(deletion, insertion, substitution);

                if (withDamerau && i > 1 && j > 1 &&
                        s1.charAt(i - 1) == s2.charAt(j - 2) &&
                        s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    value = min(value, upperupperLine[i - 2] + 1);
                }

                lowerLine[i] = value;
            }
            // rotate lines
            int[] temp = upperupperLine;
            upperupperLine = upperLine;
            upperLine = lowerLine;
            lowerLine = temp;
        }

        int distance = upperLine[len1];
        int maxLen = Math.max(len1, len2);
        return 1.0 - ((double) distance / maxLen);
    }

    /**
     * Calculates the Levenshtein similarity of the two input string lists.
     * The Levenshtein similarity is defined as "1 - normalized Levenshtein distance".
     * For string lists, we consider each list as an ordered list of tokens and calculate the distance as the number of
     * token insertions, deletions, replacements (and swaps) that transform one list into the other.
     * @param strings1 The first string list argument for the similarity calculation.
     * @param strings2 The second string list argument for the similarity calculation.
     * @return The (multiset) Levenshtein similarity of the two arguments.
     */
    @Override
    public double calculate(final String[] strings1, final String[] strings2) {
        String[] s1 = strings1 == null ? new String[0] : strings1;
        String[] s2 = strings2 == null ? new String[0] : strings2;

        int len1 = s1.length;
        int len2 = s2.length;

        if (len1 == 0) return len2 == 0 ? 1.0 : 0.0;
        if (len2 == 0) return 0.0;

        double levenshteinSimilarity = 0;

        int[] upperupperLine = new int[strings1.length + 1];   // line for Damerau lookups
        int[] upperLine = new int[strings1.length + 1];        // line for regular Levenshtein lookups
        int[] lowerLine = new int[strings1.length + 1];        // line to be filled next by the algorithm

        // Fill the first line with the initial positions (= edits to generate string1 from nothing)
        for (int i = 0; i <= strings1.length; i++)
            upperLine[i] = i;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //                                      DATA INTEGRATION ASSIGNMENT                                           //
        // Use the three provided lines to successively calculate the Levenshtein matrix with the dynamic programming //
        // algorithm. Depending on whether the inner flag withDamerau is set, the Damerau extension rule should be    //
        // used during calculation or not. Hint: Implement the Levenshtein algorithm above first, then copy the code  //
        // to this function and adjust it a bit to work on the arrays - the algorithm is the same.                    //
        for (int j = 1; j <= len2; j++) {
            lowerLine[0] = j;
            for (int i = 1; i <= len1; i++) {
                int cost = (s1[i - 1].equals(s2[j - 1])) ? 0 : 1;
                int deletion = upperLine[i] + 1;
                int insertion = lowerLine[i - 1] + 1;
                int substitution = upperLine[i - 1] + cost;

                int value = min(deletion, insertion, substitution);

                if (withDamerau && i > 1 && j > 1 &&
                        s1[i - 1].equals(s2[j - 2]) &&
                        s1[i - 2].equals(s2[j - 1])) {
                    value = min(value, upperupperLine[i - 2] + 1);
                }

                lowerLine[i] = value;
            }

            // rotate lines
            int[] temp = upperupperLine;
            upperupperLine = upperLine;
            upperLine = lowerLine;
            lowerLine = temp;
        }

        int distance = upperLine[len1];
        int maxLen = Math.max(len1, len2);
        return 1.0 - ((double) distance / maxLen);


        //                                                                                                            //
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //return levenshteinSimilarity;
    }
}
