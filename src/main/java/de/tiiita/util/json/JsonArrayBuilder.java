package de.tiiita.util.json;

import java.util.HashSet;
import java.util.Set;

 public class JsonArrayBuilder implements JsonElementBuilder {
    private final Set<Object> values = new HashSet<>();

     public static JsonArrayBuilder instantiate() {
         return new JsonArrayBuilder();
     }

    //Example: ["value", "value2", "value3"]
    public JsonArrayBuilder add(String value) {
        values.add(value);
        return this;
    }

    //Example: [{"key": "value", "key2": "value2"}]
    public JsonArrayBuilder add(JsonStringBuilder jsonStringBuilder) {
        values.add(jsonStringBuilder);
        return this;
    }

    @Override
    public String build() {
        StringBuilder arrayStringBuilder = new StringBuilder();
        arrayStringBuilder.append("[");

        values.forEach(s -> {
            if (s instanceof String) {
                arrayStringBuilder.append("\"").append(s).append("\"").append(",");
            }

            if (s instanceof JsonStringBuilder) {
                arrayStringBuilder.append(s).append(",");
            }
        });

        arrayStringBuilder.deleteCharAt(arrayStringBuilder.length() - 1);
        arrayStringBuilder.append("]");
        return arrayStringBuilder.toString();
    }

     @Override
     public String toString() {
         return build();
     }
 }