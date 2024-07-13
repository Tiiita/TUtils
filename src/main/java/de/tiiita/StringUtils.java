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

    public static JsonObject fromJsonString(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
}
