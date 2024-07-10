package de.tiiita.util.config;

import java.io.IOException;

public class ConfigHelper {

    public static Config buildConfig(String filePath, int id) {
        Config config = new Config(filePath);
        try {
            config.load(filePath);
            config.addConfigVersion(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public static boolean isRightConfig(Config config, int id) {
      return config.getInt("config-version") == id;
    }
}
