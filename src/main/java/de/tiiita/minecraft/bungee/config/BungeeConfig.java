package de.tiiita.minecraft.bungee.config;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author NieGestorben, tiiita_
 * Created on Juli 29, 2022 | 18:31:59
 * (●'◡'●)
 */


@SuppressWarnings("unused")
public class BungeeConfig {
    private final Plugin plugin;

    private boolean loaded;
    private final String name;
    private Configuration fileConfiguration;
    private final File file;


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public BungeeConfig(String name, Plugin plugin) {
        this.plugin = plugin;
        this.name = name;

        File path = plugin.getDataFolder();
        file = new File(path, name);
        if (!file.exists()) {
            path.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "There was an error creating the config!");
                throw new RuntimeException(e);
            }
        }
        try {
            fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error loading the config!");
        }

        loadConfig();
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.fileConfiguration, this.file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error saving the config!");
            throw new RuntimeException(e);
        }
    }


    public void reload() {
        try {
            fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(this.file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error loading the config!");
        }

    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public Configuration getSection(String path) {
        return fileConfiguration.getSection(path);
    }
    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    public String getString(String path, String placeholder, String replacement) {
        return getString(path).replaceAll(placeholder, replacement);
    }

    public String getStringColored(String path) {
        return color(getString(path));
    }

    public List<String> getStringListColored(String path) {
        List<String> coloredStrings = new ArrayList<>();
        for (String string : getStringList(path)) {
            coloredStrings.add(color(string));
        }

        return coloredStrings;
    }
    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }
    public List<Integer> getIntList(String path) {
        return fileConfiguration.getIntList(path);
    }
    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }

    public void setString(String path, String value) {
        fileConfiguration.set(path, value);
    }

    public void setBoolean(String path, boolean value) {
        fileConfiguration.set(path, value);
    }

    public void setInt(String path, int value) {
        fileConfiguration.set(path, value);
    }
    public void set(String path, Object object) {
        fileConfiguration.set(path, object);
    }

    public void setDouble(String path, double value) {
        fileConfiguration.set(path, value);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadConfig() {
        if (loaded)
            throw new IllegalStateException("Config is already loaded");
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        File file = new File(plugin.getDataFolder(), name);

        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream(name)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        loaded = true;
    }
}