package com.mcspacecraft.museum;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import co.aikar.commands.PaperCommandManager;

// For more details on plugin annotations see https://www.spigotmc.org/wiki/spigot-plugin-yml-annotations/

@Plugin(name = "Museum", version = "1.0")
@Description("MCSpaceCraft Museum Core Functionality")
@LoadOrder(PluginLoadOrder.POSTWORLD)
@Author("Catalin Ionescu")
@ApiVersion(Target.v1_19)
public class Museum extends JavaPlugin {
    private static Museum plugin;
    public static final Logger logger = Logger.getLogger("Minecraft.Museum");

    private MuseumConfig config;

    @Override
    public void onEnable() {
        plugin = this;

        config = new MuseumConfig(this);
        config.load();

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new MuseumCommandHandler(this));
    }

    @Override
    public void onDisable() {
        // Empty for now
    }

    public static Museum getPlugin() {
        return plugin;
    }

    public MuseumConfig getMuseumConfig() {
        return config;
    }


    public void logInfoMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            logger.info(String.format("[%s] %s", getDescription().getName(), msg));
        } else {
            logger.info(String.format("[%s] %s", getDescription().getName(), String.format(msg, args)));
        }
    }

    public void logErrorMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            logger.severe(String.format("[%s] %s", getDescription().getName(), msg));
        } else {
            logger.severe(String.format("[%s] %s", getDescription().getName(), String.format(msg, args)));
        }
    }
}