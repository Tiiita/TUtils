package de.tiiita.json;

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

        values.forEach(item -> {
            if (item instanceof String) {
                arrayStringBuilder.append("\"").append(item).append("\"").append(",");
            }

            if (item instanceof JsonStringBuilder || item instanceof Number) {
                arrayStringBuilder.append(item).append(",");
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
