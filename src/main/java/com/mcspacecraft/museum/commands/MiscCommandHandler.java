package com.mcspacecraft.museum.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.ChatMessages;
import com.mcspacecraft.museum.warps.Warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Values;

@CommandAlias("museum|core|c")
public class MiscCommandHandler extends BaseCommand {
    private Museum plugin;

    public MiscCommandHandler(Museum plugin) {
        this.plugin = plugin;
    }

    @Subcommand("spawn")
    @CommandAlias("spawn")
    public void cmdHome(Player player) {
        tpToWarp(player, "spawn");
    }

    @Subcommand("rules")
    @CommandAlias("rules")
    public void cmdRules(CommandSender sender) {
        sender.sendMessage(ChatMessages.getMessage("rules"));
    }

    @Subcommand("warp")
    @CommandAlias("warp")
    public void cmdWarp(Player player, @Values("@warps") String target) {
        if (!plugin.getWarpManager().hasWarp(target)) {
            player.sendMessage(ChatMessages.getMessage("warp.not-found", "playername", target));
            return;
        }

        tpToWarp(player, target);
    }

    private void tpToWarp(Player player, String target) {
        Warp warp = plugin.getWarpManager().getWarp(target);
        World world = Bukkit.getWorlds().get(0);

        if (player.teleport(warp.getLocation(world), TeleportCause.PLUGIN, false, true)) {
            player.sendMessage(ChatMessages.getMessage("teleport.ok"));
        } else {
            player.sendMessage(ChatMessages.getMessage("teleport.error"));
        }
    }
}
