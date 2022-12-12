package com.mcspacecraft.museum;

import org.bukkit.configuration.file.FileConfiguration;

public class MuseumConfig {
    private final Museum plugin;
    private FileConfiguration config;

    private boolean debug = false;

    public MuseumConfig(Museum plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        applyConfig();
    }

    private void applyConfig() {
        plugin.reloadConfig();

        config = plugin.getConfig();
        debug = config.getBoolean("debug", false);
    }

    public boolean getDebug() {
        return debug;
    }

    public FileConfiguration getRawConfig() {
        return config;
    }
}
