package de.tiiita.util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonStringBuilder {

    private final Map<String, Object> map = new HashMap<>();

    public static JsonStringBuilder instantiate() {
        return new JsonStringBuilder();
    }

    public JsonStringBuilder add(String key, String value) {
        map.put(key, value);
        return this;
    }

    public JsonStringBuilder add(String key, JsonStringBuilder nestedBuilder) {
        map.put(key, nestedBuilder);
        return this;
    }

    public JsonStringBuilder add(String key, List<Object> array) {
        map.put(key, array);
        return this;
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        stringBuilder.append("{");
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();

            stringBuilder.append("\"").append(key).append("\":");

            if (value instanceof String) {
                stringBuilder.append("\"").append(value).append("\"");
            } else if (value instanceof JsonStringBuilder) {
                stringBuilder.append(((JsonStringBuilder) value).build());
            } else if (value instanceof List) {
                stringBuilder.append("[");
                List<Object> array = (List<Object>) value;
                Iterator<Object> arrayIterator = array.iterator();
                while (arrayIterator.hasNext()) {
                    Object arrayValue = arrayIterator.next();
                    if (arrayValue instanceof String) {
                        stringBuilder.append("\"").append(arrayValue).append("\"");
                    } else if (arrayValue instanceof JsonStringBuilder) {
                        stringBuilder.append(((JsonStringBuilder) arrayValue).build());
                    } else {
                        throw new IllegalArgumentException("Unsupported array value type: " + arrayValue.getClass());
                    }
                    if (arrayIterator.hasNext()) {
                        stringBuilder.append(",");
                    }
                }
                stringBuilder.append("]");
            } else {
                throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
            }

            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
