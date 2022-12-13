package com.mcspacecraft.museum;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.LoadOrder;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import com.google.common.collect.ImmutableList;
import com.mcspacecraft.museum.islands.IslandWorld;
import com.mcspacecraft.museum.listeners.ChatHandlerListener;
import com.mcspacecraft.museum.listeners.PlayerListener;
import com.mcspacecraft.museum.listeners.WeatherChangeListener;
import com.mcspacecraft.museum.listeners.WorldProtectListener;
import com.mcspacecraft.museum.util.ChatMessages;

import co.aikar.commands.PaperCommandManager;

// For more details on plugin annotations see https://www.spigotmc.org/wiki/spigot-plugin-yml-annotations/

@Plugin(name = "Museum", version = "1.0")
@Description("MCSpaceCraft Museum Core Functionality")
@LoadOrder(PluginLoadOrder.POSTWORLD)
@Author("Catalin Ionescu")
@ApiVersion(Target.v1_19)
public class Museum extends JavaPlugin {
    private static Museum plugin;
    public static final Logger logger = Logger.getLogger("Museum");

    private MuseumConfig config;
    private IslandWorld islandWorld;

    @Override
    public void onEnable() {
        plugin = this;

        ChatMessages.loadPluginMessages(getClassLoader());

        config = new MuseumConfig(this);
        config.load();

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new WorldProtectListener(), this);
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new WeatherChangeListener(), this);
        pluginManager.registerEvents(new ChatHandlerListener(), this);

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new MuseumCommandHandler(this));
        manager.registerCommand(new IslandCommandHandler(this));

        manager.getCommandCompletions().registerCompletion("onoff", c -> {
            return ImmutableList.of("on", "off");
        });

        islandWorld = new IslandWorld();
        islandWorld.loadIslandList();
        islandWorld.buildIslandLookupMap();

        manager.getCommandCompletions().registerCompletion("owners", c -> islandWorld.getOwnerList());
    }

    @Override
    public void onDisable() {
        // Empty for now
    }

    public static Museum getInstance() {
        return plugin;
    }

    public MuseumConfig getMuseumConfig() {
        return config;
    }

    public IslandWorld getIslandWorld() {
        return islandWorld;
    }

    public void logDebugMessage(final String msg, final Object... args) {
        if (!getMuseumConfig().getDebug()) {
            return;
        }

        if (args == null || args.length == 0) {
            logger.fine(msg);
        } else {
            logger.fine(String.format(msg, args));
        }
    }

    public void logInfoMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            logger.info(msg);
        } else {
            logger.info(String.format(msg, args));
        }
    }

    public void logErrorMessage(final String msg, final Object... args) {
        if (args == null || args.length == 0) {
            logger.severe(msg);
        } else {
            logger.severe(String.format(msg, args));
        }
    }
}