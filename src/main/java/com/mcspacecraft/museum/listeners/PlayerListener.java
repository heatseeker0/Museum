package com.mcspacecraft.museum.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.ChatMessages;
import com.mcspacecraft.museum.warps.Warp;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Museum.getInstance().getMuseumConfig().stopLoginMessages()) {
            event.joinMessage(null);
        }

        final Player player = event.getPlayer();

        player.setAllowFlight(true);
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

        if (!player.isOp()) {
            player.getInventory().clear();
        }

        player.teleport(Museum.getInstance().getWarpManager().getWarp("spawn").getLocation(Bukkit.getWorlds().get(0)), false, true);
        player.sendMessage(ChatMessages.getMessage("welcome"));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (Museum.getInstance().getMuseumConfig().stopLoginMessages()) {
            event.quitMessage(null);
        }
    }

    /**
     * Prevents players from dropping items or XP on death. Shouldn't be needed in this context but better safe than
     * sorry.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    /**
     * Prevent non-OP players from dropping items.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        // Stop entities from picking up items.
        if (event.getEntityType() != EntityType.PLAYER) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();
        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }

    /**
     * Prevent players from losing hunger.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerStarving(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    /**
     * Prevent players from drowning.
     * 
     * @param event
     */
    @EventHandler
    public void onPlayerDrown(EntityAirChangeEvent event) {
        event.setCancelled(true);
    }

    /**
     * Prevent players from getting damaged, except by void (this is so they don't fall forever in a void world).
     *
     * @param event
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getCause() == DamageCause.VOID) {
            final Player player = (Player) event.getEntity();

            Warp warp = Museum.getInstance().getWarpManager().getWarp("spawn");
            World world = Bukkit.getWorlds().get(0);

            if (player.teleport(warp.getLocation(world), TeleportCause.PLUGIN, false, true)) {
                event.setDamage(0.0D);
                event.setCancelled(true);

                // Teleport them again 1 tick later to work around a bug being stuck in place
                Bukkit.getScheduler().runTask(Museum.getInstance(), () -> player.teleport(warp.getLocation(world), TeleportCause.PLUGIN, false, true));
            }

            // Don't cancel void damage if we can't teleport them
            return;
        }

        event.setDamage(0.0D);
        event.setCancelled(true);
    }

    /**
     * Prevent players from setting homes.
     *
     * @param event
     */
    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }
        
        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }
}
