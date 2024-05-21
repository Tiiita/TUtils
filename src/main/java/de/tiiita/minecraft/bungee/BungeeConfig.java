package de.tiiita.minecraft.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
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

    private Configuration fileConfiguration;
    private final File file;


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public BungeeConfig(String name, Plugin plugin) {
        this.plugin = plugin;
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

    public String color(String path) {
        return ChatColor.translateAlternateColorCodes('&', path);
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
}