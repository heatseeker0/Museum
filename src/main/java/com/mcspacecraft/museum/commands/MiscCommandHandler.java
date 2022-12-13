package com.mcspacecraft.museum.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.ChatMessages;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;

@CommandAlias("museum|core|c")
public class MiscCommandHandler extends BaseCommand {
    private Museum plugin;

    public MiscCommandHandler(Museum plugin) {
        this.plugin = plugin;
    }

    @Subcommand("spawn")
    @CommandAlias("spawn")
    public void cmdHome(Player player) {
        World world = Bukkit.getWorlds().get(0);

        Location spawnLoc = world.getSpawnLocation();
        if (player.teleport(spawnLoc, TeleportCause.PLUGIN, false, true)) {
            player.sendMessage(ChatMessages.getMessage("teleport.ok"));
        } else {
            player.sendMessage(ChatMessages.getMessage("teleport.error"));
        }
    }

    @Subcommand("rules")
    @CommandAlias("rules")
    public void cmdRules(CommandSender sender) {
        sender.sendMessage(ChatMessages.getMessage("rules"));
    }
}
