package com.mcspacecraft.museum.islands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.mcspacecraft.islandworld.entity.SimpleIslandV4;
import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.ChatMessages;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;

public class IslandDisplayManager implements Listener {
    private Map<UUID, BossBar> locationBar = new HashMap<>();

    public void load() {
        Museum.getInstance().getServer().getPluginManager().registerEvents(this, Museum.getInstance());
    }

    private BossBar getOrCreateLocationBar(Player player) {
        UUID playerId = player.getUniqueId();

        BossBar bar = locationBar.getOrDefault(playerId, BossBar.bossBar(ChatMessages.getMessage("island.bossbar.unknown"), 0, Color.YELLOW, Overlay.PROGRESS));
        locationBar.putIfAbsent(playerId, bar);

        return bar;
    }

    private void updateDisplayedLocation(Player player) {
        Location loc = player.getLocation();
        SimpleIslandV4 island = Museum.getInstance().getIslandManager().getIslandFromLocation(loc);

        BossBar bar = getOrCreateLocationBar(player);
        if (island == null) {
            bar.name(ChatMessages.getMessage("island.bossbar.unknown"));
        } else {
            bar.name(ChatMessages.getMessage("island.bossbar.known", "name", island.getOwnerName()));
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        BossBar bar = getOrCreateLocationBar(player);
        player.showBossBar(bar);

        updateDisplayedLocation(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        updateDisplayedLocation(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Bukkit.getScheduler().runTaskLater(Museum.getInstance(), () -> updateDisplayedLocation(event.getPlayer()), 5L);
    }
}
