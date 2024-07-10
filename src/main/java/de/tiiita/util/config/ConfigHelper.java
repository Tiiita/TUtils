package de.tiiita.util.config;

import java.io.IOException;

public class ConfigHelper {

    public static Config buildConfig(String filePath, int id) {
        Config config = new Config(filePath);
        try {
            config.load(filePath);
            config.addConfigId(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public static void checkRightConfig(Config config, int id) {
        if (config.getConfigId() != id)
            throw new IllegalArgumentException("Wrong config, config-id: " + config.getConfigId()
                    + ", wanted config-id: " + id);
    }
}
