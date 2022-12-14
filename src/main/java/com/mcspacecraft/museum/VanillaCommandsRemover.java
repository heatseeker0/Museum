package com.mcspacecraft.museum;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

public class VanillaCommandsRemover {
    /**
     * Removes unwanted Minecraft or Bukkit vanilla commands.
     */
    public static void unregisterCommands() {
        // Get rid of unwanted commands
        unregisterCommand(
            "bukkit", true, "reload", "about", "rl", "plugins", "pl", "version", "ver", "timings"
        );

        // These ones need delay.
        Bukkit.getScheduler().runTaskLater(Museum.getInstance(), new Runnable() {
            @Override
            public void run() {
                unregisterCommand("bukkit", false, "help", "?");

                unregisterCommand("minecraft", false, "me", "help", "tell", "msg", "w", "kick", "ban", "ban-ip");
                unregisterCommand("minecraft", true, "say", "banlist", "trigger");
            }
        }, 20);
    }

    /**
     * Unregister the command.
     *
     * @param owner Owner, i.e bukkit, minecraft etc.
     * @param aliases Associated aliases.
     */
    private static void unregisterCommand(String owner, boolean root, String... aliases) {
        CommandMap commandMap = Bukkit.getCommandMap();

        for (String alias : aliases) {
            if (root) {
                unregisterCommandRoot(owner, aliases);
            }

            Command remove = commandMap.getKnownCommands().remove(owner + ":" + alias);
            if (remove != null) {
                Museum.getInstance().logDebugMessage("Unregistering command '%s:%s'", owner, alias);
                remove.unregister(commandMap);
            } else {
                Museum.getInstance().logErrorMessage("Could not unregister command '%s:%s'", owner, alias);
            }
        }
    }

    private static void unregisterCommandRoot(String owner, String... aliases) {
        CommandMap commandMap = Bukkit.getCommandMap();

        for (String alias : aliases) {
            Command rootRemove = commandMap.getKnownCommands().remove(alias);
            if (rootRemove != null) {
                Museum.getInstance().logDebugMessage("Unregistering root command %s (%s)", alias, owner);
                rootRemove.unregister(commandMap);
            } else {
                Museum.getInstance().logErrorMessage("Could not unregister root command %s (%s)'", alias, owner);
            }
        }
    }
}
