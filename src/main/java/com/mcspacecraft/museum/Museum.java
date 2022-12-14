package com.mcspacecraft.museum;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
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
import com.mcspacecraft.museum.commands.IslandCommandHandler;
import com.mcspacecraft.museum.commands.MiscCommandHandler;
import com.mcspacecraft.museum.commands.MuseumCommandHandler;
import com.mcspacecraft.museum.islands.IslandManager;
import com.mcspacecraft.museum.listeners.ChatHandlerListener;
import com.mcspacecraft.museum.listeners.PlayerListener;
import com.mcspacecraft.museum.listeners.WeatherChangeListener;
import com.mcspacecraft.museum.listeners.WorldProtectListener;
import com.mcspacecraft.museum.util.ChatMessages;
import com.mcspacecraft.museum.warps.WarpManager;

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
    private IslandManager islandManager;
    private WarpManager warpManager;

    @Override
    public void onEnable() {
        plugin = this;

        ChatMessages.loadPluginMessages(getClassLoader());

        config = new MuseumConfig(this);
        config.load();

        VanillaCommandsRemover.unregisterCommands();

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new WorldProtectListener(), this);
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new WeatherChangeListener(), this);
        pluginManager.registerEvents(new ChatHandlerListener(), this);

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new MuseumCommandHandler(this));
        manager.registerCommand(new IslandCommandHandler(this));
        manager.registerCommand(new MiscCommandHandler(this));

        manager.getCommandCompletions().registerCompletion("onoff", c -> {
            return ImmutableList.of("on", "off");
        });

        islandManager = new IslandManager();
        islandManager.load();

        manager.getCommandCompletions().registerCompletion("owners", c -> islandManager.getOwnerList());

        warpManager = new WarpManager();
        warpManager.load();

        manager.getCommandCompletions().registerCompletion("warps", c -> warpManager.getWarpList());
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

    public IslandManager getIslandManager() {
        return islandManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public boolean canChangeWorld(Player player) {
        return (player.isOp() || player.hasPermission(MuseumConfig.ADMIN_PERMISSION_NODE)) && !config.isWorldFrozen();
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