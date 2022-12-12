package com.mcspacecraft.museum;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

import com.mcspacecraft.museum.util.MessageUtils;

public class MuseumConfig {
    private final Museum plugin;
    private FileConfiguration config;

    private boolean debug = false;

    private Map<String, String> messages = new HashMap<>();

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

        messages.clear();
        for (String msgKey : config.getConfigurationSection("messages").getKeys(false)) {
            messages.put(msgKey, MessageUtils.parseColors(config.getString("messages." + msgKey)));
        }
    }

    public boolean getDebug() {
        return debug;
    }

    public FileConfiguration getRawConfig() {
        return config;
    }

    public String getMessage(final String key) {
        if (messages.containsKey(key)) {
            return messages.get(key);
        }
        final String errorMsg = "No message text in config.yml for " + key;
        plugin.logErrorMessage(errorMsg);
        return errorMsg;
    }
}
