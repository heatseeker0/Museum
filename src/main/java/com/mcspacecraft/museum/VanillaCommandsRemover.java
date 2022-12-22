package com.mcspacecraft.museum;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import com.google.common.collect.ImmutableSet;

public class VanillaCommandsRemover {
    private static Set<String> KEEP_KEY_COMMANDS = ImmutableSet.of("stop", "ban", "ban-ip", "banlist", "whitelist", "save-all", "gamerule", "kick", "tps", "save-off", "save-on",
        // To remove when I've implemented own version
        "op", "kill", "weather", "gamemode", "list",
        // Our own commands
        "island", "is", "i",
        "museum", "core", "c",
        "spawn", "rules", "warp", "econ", "bal", "balance", "played", "playtime");

    /**
     * Removes unwanted Minecraft or Bukkit vanilla commands.
     */
    public static void unregisterCommands() {
        CommandMap commandMap = Bukkit.getCommandMap();

        Map<String, Command> knownCommands = commandMap.getKnownCommands();

        Museum.getInstance().logInfoMessage("========== Known commands before ============");
        printKnownCommands(knownCommands);

        realClearCommands();

        Museum.getInstance().logInfoMessage("========== Known commands after ============");
        printKnownCommands(commandMap.getKnownCommands());
    }

    private static void realClearCommands() {
        Field field;
        try {
            CommandMap commandMap = Bukkit.getCommandMap();

            field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, Command> knownCommands = (Map<String, Command>) field.get(commandMap);

            List<Command> cmdsToKeep = new ArrayList<>();

            for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
                if (KEEP_KEY_COMMANDS.contains(entry.getKey())) {
                    cmdsToKeep.add(entry.getValue());
                }
                entry.getValue().unregister(commandMap);
            }
            knownCommands.clear();

            for (Command cmd : cmdsToKeep) {
                knownCommands.put(cmd.getName(), cmd);
                cmd.register(commandMap);
            }
        } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void printKnownCommands(Map<String, Command> cmdList) {
        Museum plugin = Museum.getInstance();

        for (Entry<String, Command> cmd : cmdList.entrySet()) {
            plugin.logInfoMessage("key - %s -- [name: %s, label: %s, aliases: %s]", cmd.getKey(), cmd.getValue().getName(), cmd.getValue().getLabel(), Strings.join(cmd.getValue().getAliases(), ','));
        }
    }
}
