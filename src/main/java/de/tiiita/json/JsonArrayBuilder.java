package de.tiiita.json;

import java.util.HashSet;
import java.util.Set;

/**
 * A JsonArrayBuilder can build an array in the json format.
 * This class implements the JsonElementBuilder.
 * <p>
 * This class supports strings, numbers or JsonStringBuilder for a json string in an array.
 */
public class JsonArrayBuilder implements JsonElementBuilder {
    private final Set<Object> values = new HashSet<>();

    /**
     * This is just a method for nested code.
     * This creates and returns a new instance of this class.
     *
     * @return the created instance.
     */
    public static JsonArrayBuilder instantiate() {
        return new JsonArrayBuilder();
    }


    /**
     * This method adds a value to the array.
     *
     * @param value the string you want to add to the array.
     * @return the updated instance of this.
     */
    public JsonArrayBuilder add(String value) {
        values.add(value);
        return this;
    }

    /**
     * This method adds a value to the array.
     *
     * @param value the number you want to add to the array.
     *              This can be any that extends Number.
     * @return the updated instance of this.
     */
    public JsonArrayBuilder add(Number value) {
        values.add(value);
        return this;
    }

    /**
     * This method adds a json string to the array.
     *
     * @param jsonStringBuilder the instance of the json string builder.
     * <p>
     * This method is used if you want to create an array like this:
     *  {@code
     *  [{"key": "value}]
     *  }
     * @return the updated instance of this.
     */
    public JsonArrayBuilder add(JsonStringBuilder jsonStringBuilder) {
        values.add(jsonStringBuilder);
        return this;
    }

    /**
     * This is the build implementation. It uses a StringBuilder for that.
     * It appends '[' and then each item in the list.
     * At the end of the loop the last comma is getting deleted and the closing bracket ']' appended.
     * If the list is empty the last char will not be deleted because that would be the starting bracket.
     * @return the built json string.
     */
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

        if (!values.isEmpty()) {
            arrayStringBuilder.deleteCharAt(arrayStringBuilder.length() - 1);
        }
        arrayStringBuilder.append("]");
        return arrayStringBuilder.toString();
    }


    /**
     * This method just uses the build method.
     * @return the return value of the {@link #build()} method.
     */
    @Override
    public String toString() {
        return build();
    }
}
