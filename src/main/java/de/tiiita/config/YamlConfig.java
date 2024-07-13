package de.tiiita.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * A YamlConfig represents a config file that uses the yaml format.
 * Using this class, you can create custom configs, as many as you want and get or set
 * information to it.
 * <p>
 * It also allows to load an existing config preset in the /resources folder.
 */
public class YamlConfig {
    private final Map<String, Object> config;
    private final String filePath;
    private final String CONFIG_ID = "config-id";

    /**
     * When creating a new config, this is called.
     * This loads the existing config from the resource folder, adds the config identifier and saves this to a real file.
     * @param fileName the file name. Example: config.yml
     * @param identifier the identifier. A good method to use is to use ids like 1, 2 or 3.
     */
    public YamlConfig(@NotNull String fileName, int identifier) {
        this.filePath = fileName;
        config = new HashMap<>();
        try {
            load(fileName);
            addConfigId(identifier);
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This gets a value as a string from the currently loaded config.
     *
     * @param path the path to the value, also called the key.
     * @return the string or null of no value was found with the given path.
     * @see #get(String, Class)
     */
    @Nullable
    public String getString(@NotNull String path) {
        return get(path, String.class);
    }

    /**
     * This gets a value as an integer from the currently loaded config.
     *
     * @param path the path to the value, also called the key.
     * @return the integer. If no value was found a NullPointerException will be thrown.
     * @see #get(String, Class)
     */
    public int getInt(@NotNull String path) {
        return get(path, Integer.class);
    }

    /**
     * This gets a value as a boolean from the currently loaded config.
     *
     * @param path the path to the value, also called the key.
     * @return the boolean or null of no value was found with the given path.
     * @see #get(String, Class)
     */
    public boolean getBoolean(String path) {
        return get(path, Boolean.class);
    }


    /**
     * Using this method you can set a string value the into virtually loaded config.
     * Please do not forget to {@link #save()} the config after setting in a value!
     *
     * @param path  this is the path the value will be set to.
     *              It does not matter if the path already exists, because the value bind to the existing key will just be overwritten.
     *              <p>
     * @param value this is the value of the path.
     * @see #setString(String, String)
     */
    public void setString(String path, String value) {
        set(path, value);
    }

    /**
     * Using this method you can set an integer value into the virtually loaded config.
     * Please do not forget to {@link #save()} the config after setting in a value!
     *
     * @param path  this is the path the value will be set to.
     *              It does not matter if the path already exists, because the value bind to the existing key will just be overwritten.
     *              <p>
     * @param value this is the value of the path.
     * @see #setString(String, String)
     */
    public void setInt(String path, int value) {
        set(path, value);
    }


    /**
     * Using this method you can set any value,
     * does not matter the type until yaml supports it, into the virtually loaded config.
     * There are already methods that use this method to get a specific type. {@link #getString(String)}
     * <p>
     * Do not forget to {@link #save()} after this!
     * <p>
     *
     * @param path  the path to the value. Also called keys.
     * @param clazz the class the value should be cast to. If you get a value but the class does not match
     *              a ClassCastException will be thrown.
     * @throws NullPointerException if the value is null and the class cannot cast null.
     * For example, this will happen if you try to get an int, but it cannot be found.
     */
    public <T> T get(String path, Class<T> clazz) {
        return clazz.cast(config.get(path));
    }


    /**
     * Using this method you can set any value,
     * does not matter the type until yaml supports it, into the virtually loaded config.
     * There are already methods that use this method to set a specific type. {@link #setString(String, String)}
     * <p>
     *
     * @param path  the path to the value. Also called keys.
     * @param value the value bind to the path. This can be any value yaml supports.
     */
    public void set(String path, Object value) {
        config.put(path, value);
    }

    /**
     * This removes a whole config entry, so the path (key) AND the value.
     * When you want to override a value just use one of the set() methods!
     * Do not forget to {@link #save()} after this!
     *
     * @param path the path to the position in the config.
     */
    public void removeEntry(String path) {
        config.remove(path);
    }

    void addConfigId(int id) {
        removeEntry(CONFIG_ID);
        setInt(CONFIG_ID, id);
    }

    /**
     * Every config has a config id. That is an identifier.
     * To check if a config is the right one in the code, use the ConfigHelper class.
     *
     * @return the config id.
     */
    public int getConfigId() {
        return getInt(CONFIG_ID);
    }


    /**
     * This saves the virtual config (the hashmap) to a real file on the system.
     */
    public void save() {
        Path path = Path.of(filePath);
        String fileExtension = getFileExtension(filePath);

        try {
            if (fileExtension.equals("yml") || fileExtension.equals("yaml")) {
                saveYaml(path);
            } else {
                savePlainText(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This checks if this config's id matches the identifier passes as argument.
     * If the id matches (the config is the right one) nothing happens.
     * <p>
     * This is useful when you have multiple configs and want to check if the right one
     * has been passes as arguments or whatever.
     * @param identifier the identifier for the config you expect this to be.
     * @throws WrongConfigException if the config ids don't match.
     */
    public void checkRightConfig(int identifier) {
        if (getConfigId() != identifier)
            throw new WrongConfigException("Wrong config, config-id: " + getConfigId()
                    + ", wanted config-id: " + identifier);

    }

    private void loadYaml(InputStream inputStream, Map<String, Object> target, String pathPrefix) throws IOException {
        Yaml yaml = new Yaml();
        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Map<String, Object> yamlConfig = yaml.load(reader);
            if (yamlConfig == null) {
                throw new IllegalArgumentException("Invalid YAML syntax");
            }
            for (Map.Entry<String, Object> entry : yamlConfig.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String s = pathPrefix.isEmpty() ? key : pathPrefix + "." + key;
                if (value instanceof Map) {
                    loadYaml(new ByteArrayInputStream(yaml.dump(value).getBytes()), target, s);
                } else {
                    target.put(s, value);
                }
            }
        }
    }

    void load(String fileName) throws IOException {
        File configFile = new File(fileName);
        if (configFile.exists()) {
            // If the config file exists, load it from the file
            try (InputStream inputStream = Files.newInputStream(configFile.toPath())) {
                String fileExtension = getFileExtension(fileName);
                if (fileExtension.equals("yml") || fileExtension.equals("yaml")) {
                    loadYaml(inputStream, config, "");
                } else {
                    loadPlainText(inputStream);
                }
            }
        } else {
            // If the config file doesn't exist, load it from the jar
            ClassLoader classLoader = getClass().getClassLoader();
            try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Config file not found: " + fileName);
                }
                String fileExtension = getFileExtension(fileName);
                if (fileExtension.equals("yml") || fileExtension.equals("yaml")) {
                    loadYaml(inputStream, config, "");
                } else {
                    loadPlainText(inputStream);
                }
            }
        }
    }


    private String getFileExtension(String filePath) {
        String fileName = new File(filePath).getName();
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    private void loadPlainText(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                config.put(parts[0].trim(), parts[1].trim());
            }
        }
    }

    private void savePlainText(Path path) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : config.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        Files.writeString(path, sb.toString());
    }

    private void saveYaml(Path path) throws IOException {
        Yaml yaml = new Yaml();
        String yamlString = yaml.dump(config);
        Files.writeString(path, yamlString);
    }
}