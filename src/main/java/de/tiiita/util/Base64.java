package de.tiiita.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created on April 12, 2023 | 23:38:37
 * (●'◡'●)
 */
public class Base64 {

    //Object -> String
    public static String encode(Object obj) {
        try {
            String serialized = serialize(obj);
            byte[] bytes = java.util.Base64.getEncoder().encode(serialized.getBytes());
            return new String(bytes, StandardCharsets.UTF_8).substring(0, 64);
        } catch (IOException e) {
            throw new RuntimeException("Error encoding object", e);
        }
    }

    //String -> Object
    public static <T> T decode(String encoded, Class<T> clazz) {
        try {
            String decoded = new String(java.util.Base64.getDecoder().decode(encoded.getBytes()), StandardCharsets.UTF_8);
            return deserialize(decoded, clazz);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error decoding object", e);
        }
    }

    private static String serialize(Object obj) throws IOException {
        // Use any serialization method of your choice
        // Here we use Java built-in serialization
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toString(StandardCharsets.ISO_8859_1);
    }

    private static <T> T deserialize(String serialized, Class<T> clazz) throws IOException, ClassNotFoundException {
        // Use any deserialization method of your choice
        // Here we use Java built-in serialization
        ByteArrayInputStream bis = new ByteArrayInputStream(serialized.getBytes(StandardCharsets.ISO_8859_1));
        ObjectInputStream ois = new ObjectInputStream(bis);
        return clazz.cast(ois.readObject());
    }
}
