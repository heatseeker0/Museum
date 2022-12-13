package com.mcspacecraft.museum.listeners;

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

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.ChatMessages;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Museum.getInstance().getMuseumConfig().stopLoginMessages()) {
            event.joinMessage(null);
        }

        // plugin.playerSetup(event.getPlayer());
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

            if (player.getLocation().getWorld().getSpawnLocation() != null) {
                player.teleport(player.getLocation().getWorld().getSpawnLocation());
                event.setDamage(0.0D);
                event.setCancelled(true);
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

    // @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    // public void onPlayerInventoryClick(InventoryClickEvent event) {
    // final Player player = (Player) event.getWhoClicked();
    // final ItemStack clickedItem = event.getCurrentItem();
    //
    // if (clickedItem == null || player == null) {
    // return;
    // }
    //
    // if (clickedItem.getType() == Material.COMPASS) {
    // player.closeInventory();
    // event.setCancelled(true);
    // Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
    // @Override
    // public void run() {
    // plugin.promptPlayer(player.getName());
    // }
    // }, 3L);
    // }
    // }
    //
    // @EventHandler
    // public void onPlayerInteractEvent(PlayerInteractEvent event) {
    // final Player player = event.getPlayer();
    // final ItemStack clickedItem = event.getItem();
    //
    // if (clickedItem == null) {
    // return;
    // }
    //
    // if (clickedItem.getType() == Material.COMPASS) {
    // player.closeInventory();
    // event.setCancelled(true);
    // Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
    // @Override
    // public void run() {
    // plugin.promptPlayer(player.getName());
    // }
    // }, 3L);
    // player.updateInventory();
    // }
    // }
}
