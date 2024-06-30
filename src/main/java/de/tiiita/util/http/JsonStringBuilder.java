package de.tiiita.util.http;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonStringBuilder {

    private final Map<String, String> map = new HashMap<>();

    public static JsonStringBuilder instantiate() {
        return new JsonStringBuilder();
    }

    public JsonStringBuilder add(String key, String value) {
        map.put(key, value);
        return this;
    }

    public String build() {
        StringBuilder stringBuilder =  new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        stringBuilder.append("{");
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();

            stringBuilder.append("\"").append(key).append("\":\"").append(value).append("\"");

            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
