package de.tiiita.util.json;

import java.util.HashMap;
import java.util.Map;

public class JsonStringBuilder implements JsonElementBuilder {

    private final Map<String, Object> map = new HashMap<>();

    public static JsonStringBuilder instantiate() {
        return new JsonStringBuilder();
    }

    public JsonStringBuilder add(String key, String value) {
        map.put(key, value);
        return this;
    }

    public JsonStringBuilder add(String key, JsonStringBuilder nestedBuilder) {
        map.put(key, nestedBuilder.map);
        return this;
    }

    public JsonStringBuilder add(String key, JsonArrayBuilder jsonArrayBuilder) {
        map.put(key, jsonArrayBuilder);
        return this;
    }

    public String build() {
        StringBuilder jsonStringBuilder = new StringBuilder();
        jsonStringBuilder.append("{");

        map.forEach((key, value) -> {
            if (value instanceof String) {
                jsonStringBuilder.append("\"").append(key).append("\":\"").append(value).append("\",");
            }

            if (value instanceof JsonStringBuilder) {
                JsonElementBuilder builder = (JsonElementBuilder) value;
                jsonStringBuilder.append("\"").append(key).append("\":").append(builder.build()).append(",");
            }


            if (value instanceof JsonArrayBuilder) {
                JsonElementBuilder builder = (JsonElementBuilder) value;
                jsonStringBuilder.append("\"").append(key).append("\":").append(builder.build()).append(",");
            }
        });

        //Delete last ','
        jsonStringBuilder.deleteCharAt(jsonStringBuilder.length() - 1);
        jsonStringBuilder.append("}");
        return jsonStringBuilder.toString();
    }

    @Override
    public String toString() {
        return build();
    }
}