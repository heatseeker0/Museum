package com.mcspacecraft.museum;

import org.bukkit.configuration.file.FileConfiguration;

public class MuseumConfig {
    public final static String ADMIN_PERMISSION_NODE = "museum.admin";

    private final Museum plugin;
    private FileConfiguration config;

    private boolean debug = false;
    private boolean stopLoginMessages = true;

    private volatile boolean chatDisabled;

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
        stopLoginMessages = config.getBoolean("stop-login-messages", true);
        chatDisabled = config.getBoolean("disable-chat", false);
    }

    public FileConfiguration getRawConfig() {
        return config;
    }

    public boolean getDebug() {
        return debug;
    }

    public void showLoginMesages(boolean value) {
        this.stopLoginMessages = value;
    }

    public boolean stopLoginMessages() {
        return stopLoginMessages;
    }

    public void setChatDisabled(boolean chatDisabled) {
        this.chatDisabled = chatDisabled;
    }

    public boolean isChatDisabled() {
        return chatDisabled;
    }
}
