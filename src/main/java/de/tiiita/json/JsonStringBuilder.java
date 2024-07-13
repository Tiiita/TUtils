package de.tiiita.json;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class JsonStringBuilder implements JsonElementBuilder {

    private final Map<String, Object> map = new HashMap<>();

    /**
     * This is just a method for nested code.
     * This creates and returns a new instance of this class.
     *
     * @return the created instance.
     */
    @NotNull
    public static JsonStringBuilder instantiate() {
        return new JsonStringBuilder();
    }


    /**
     * This method adds a value as an object and the bound key to the string.
     *
     * @param key   the key in the json string.
     * @param value the value you want to bind.
     *              <p>
     *              <ul> Accepted value types:
     *               <li>Any numerical value that extends number</li>
     *               <li>Any String</li>
     *                <li>JsonArrayBuilder</li>
     *              </ul>
     *              If you want to add a JsonStringBuilder (nested Json) you have to use the {#{@link #add(String, JsonStringBuilder)}}
     * @return the updated instance of this.
     */
    @NotNull
    public JsonStringBuilder add(@NotNull String key, @NotNull Object value) {
        validateValue(value);
        map.put(key, value);
        return this;
    }


    /**
     * This is a special add method.
     * Using this method you can add a JsonStringBuilder into a JsonStringBuilder and so on.
     * We call this nested builder. For that you use this method. Not the default add method!
     *
     * @param key           the key in the string of the nested builder.
     * @param nestedBuilder the nested builder. Beware: Do not use build(); in the nested builder,
     *                      otherwise the default add() method is automatically
     *                      being called because you pass in a string!
     * @return this updated instance.
     */
    public JsonStringBuilder add(@NotNull String key, @NotNull JsonStringBuilder nestedBuilder) {
        map.put(key, nestedBuilder.map);
        return this;
    }


    /**
     * This is the build implementation. It uses a StringBuilder for that.
     * It appends '{' and then each item in the list.
     * At the end of the loop the last comma is getting deleted and the closing bracket '}' appended.
     * If the map is empty the last char will not be deleted because that would be the starting bracket.
     *
     * @return the built json string.
     */
    @NotNull
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

            if (value instanceof Number) {
                jsonStringBuilder.append("\"").append(key).append("\":").append(value).append(",");
            }
        });

        if (!map.isEmpty()) {
            jsonStringBuilder.deleteCharAt(jsonStringBuilder.length() - 1);
        }

        jsonStringBuilder.append("}");
        return jsonStringBuilder.toString();
    }

    /**
     * This method just uses the build method.
     *
     * @return the return value of the {@link #build()} method.
     */
    @Override
    public String toString() {
        return build();
    }

    private void validateValue(Object value) {
        if (value instanceof String ||
                value instanceof Number ||
                value instanceof JsonArrayBuilder)
            return;

        if (value.getClass().isInstance(JsonStringBuilder.class))
            throw new RuntimeException("For nested json builder please use the add(String, JsonStringBuilder) method!");

        throw new RejectedJsonValueTypeException("The class type: " +
                value.getClass().getName() + " is not accepted in this json builder");
    }
}