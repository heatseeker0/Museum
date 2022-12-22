package com.mcspacecraft.museum.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.timeismoney.PlayTimeManager;
import com.mcspacecraft.museum.util.ChatMessages;
import com.mcspacecraft.museum.warps.Warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Values;

public class MiscCommandHandler extends BaseCommand {
    private Museum plugin;

    public MiscCommandHandler(Museum plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("spawn")
    public void cmdHome(Player player) {
        tpToWarp(player, "spawn");
    }

    @CommandAlias("rules")
    public void cmdRules(CommandSender sender) {
        sender.sendMessage(ChatMessages.getMessage("rules"));
    }

    @CommandAlias("warp")
    @CommandCompletion("@warps")
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

    @CommandAlias("econ|bal|balance")
    public void cmdEcon(CommandSender sender) {
        sender.sendMessage(ChatMessages.getMessage("econ"));
    }

    @CommandAlias("played|playtime")
    @CommandCompletion("@playertime")
    public void cmdPlayed(CommandSender sender, @Values("@playertime") String target) {
        String targetName = target == null ? sender.getName() : target;

        if (!plugin.getPlayTimeManager().hasPlayTime(targetName)) {
            if (target == null) {
                sender.sendMessage(ChatMessages.getMessage("playtime.norecord.self"));
            } else {
                sender.sendMessage(ChatMessages.getMessage("playtime.norecord.other", "player", targetName));
            }
            return;
        }

        String playTime = PlayTimeManager.timestampToString(plugin.getPlayTimeManager().getPlayTime(targetName));
        if (target == null) {
            sender.sendMessage(ChatMessages.getMessage("playtime.self", "played", playTime));
        } else {
            sender.sendMessage(ChatMessages.getMessage("playtime.other", "player", target, "played", playTime));
        }
    }
}
