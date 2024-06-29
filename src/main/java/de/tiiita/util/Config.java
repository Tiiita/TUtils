package de.tiiita.util;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on April 10, 2023 | 17:46:08
 * (●'◡'●)
 */
public class Config {
    private final Map<String, Object> config;
    private final String filePath;

    public Config(String filePath) {
        this.filePath = filePath;
        config = new HashMap<>();
        try {
            load(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setString(String path, String value) {
        config.put(path, value);
    }

    public void setInt(String path, int value) {
        config.put(path, value);
    }

    public String getString(String path) {
        return (String) config.get(path);
    }

    public int getInt(String path) {
        return (int) config.get(path);
    }
    public boolean getBoolean(String path) {
        return (boolean) config.get(path);
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
                    // Recursively process nested maps
                    loadYaml(new ByteArrayInputStream(yaml.dump(value).getBytes()), target, s);
                } else {
                    // Construct the full path for this leaf value
                    // Store the value in the target map
                    target.put(s, value);
                }
            }
        }
    }

    public void load(String fileName) throws IOException {
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