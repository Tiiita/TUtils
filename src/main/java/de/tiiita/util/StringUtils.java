package de.tiiita.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;

import java.io.*;

public class StringUtils {

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
