package com.mcspacecraft.museum.commands;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.mcspacecraft.islandworld.entity.SimpleIslandV4;
import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.ChatMessages;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Values;

@CommandAlias("island|is|i")
public class IslandCommandHandler extends BaseCommand {
    private Museum plugin;

    public IslandCommandHandler(Museum plugin) {
        this.plugin = plugin;
    }

    @Subcommand("info")
    @CommandCompletion("@owners")
    public void cmdInfo(CommandSender sender, @Values("@owners") String playerName) {
        if (!plugin.getIslandWorld().ownsIsland(playerName)) {
            sender.sendMessage(ChatMessages.getMessage("island.info.no-island", "playername", playerName));
            return;
        }

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        SimpleIslandV4 island = plugin.getIslandWorld().getOwnedIsland(playerName);
        String createTime = date.format(island.getCreateTime());
        String lastLoginTime = date.format(island.getOwnerLoginTime());
        String members = island.getMembers().isEmpty() ? "none" : StringUtils.join(island.getMembers(), ", ");
        String owner = island.getOwnerName();
        String gridX = Integer.toString(island.getIslandGridX());
        String gridZ = Integer.toString(island.getIslandGridZ());

        sender.sendMessage(ChatMessages.getMessage("island.info", "owner", owner, "members", members, "createtime", createTime, "lastlogintime", lastLoginTime, "gridx", gridX, "gridz", gridZ));
    }

    @Subcommand("tp|visit")
    @CommandCompletion("@owners")
    public void cmdTp(Player player, @Values("@owners") String playerName) {
        if (!plugin.getIslandWorld().ownsIsland(playerName)) {
            player.sendMessage(ChatMessages.getMessage("island.info.no-island", "playername", playerName));
            return;
        }

        tpToIsland(player, playerName);
    }

    @Subcommand("home")
    @CommandAlias("home")
    public void cmdHome(Player player) {
        if (!plugin.getIslandWorld().ownsIsland(player.getName())) {
            player.sendMessage(ChatMessages.getMessage("island.info.no-island.you"));
            return;
        }

        tpToIsland(player);
    }

    private void tpToIsland(Player player) {
        tpToIsland(player, player.getName());
    }

    private void tpToIsland(Player player, String ownerName) {
        SimpleIslandV4 island = plugin.getIslandWorld().getOwnedIsland(ownerName);
        if (island == null) {
            return;
        }

        World world = Bukkit.getWorlds().get(0);
        Location spawnLoc = new Location(world, island.spawn_x, island.spawn_y, island.spawn_z, island.spawn_yaw, island.spawn_pitch);
        if (player.teleport(spawnLoc, TeleportCause.PLUGIN, false, true)) {
            player.sendMessage(ChatMessages.getMessage("teleport.ok"));
        } else {
            player.sendMessage(ChatMessages.getMessage("teleport.error"));
        }
    }
}
