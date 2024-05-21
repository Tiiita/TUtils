package de.tiiita.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamUtil {
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
}
