package de.tiiita;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;

public class StringUtils {

    /**
     * This converts an input stream into a String.
     * @param stream the input stream you want to convert.
     * @return the converted String.
     */
    public static String asString(InputStream stream) {
        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String inputLine;
        StringBuilder string = new StringBuilder();

        while (true) {
            try {
                if ((inputLine = input.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            string.append(inputLine);
        }

        return string.toString();
    }

    public static int getStringSimilarity(String a, String b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        int lenA = a.length();
        int lenB = b.length();

        if (lenA == 0 && lenB == 0) {
            return 100;
        }

        if (lenA == 0 || lenB == 0) {
            return 0;
        }

        int[][] dp = new int[lenA + 1][lenB + 1];

        for (int i = 0; i <= lenA; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= lenB; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= lenA; i++) {
            for (int j = 1; j <= lenB; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        int levenshteinDistance = dp[lenA][lenB];
        int maxLen = Math.max(lenA, lenB);
        return (int) ((1.0 - (double) levenshteinDistance / maxLen) * 100);
    }

    public static JsonObject fromJsonString(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
}
