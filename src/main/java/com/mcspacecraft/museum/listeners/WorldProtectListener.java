package com.mcspacecraft.museum.listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.util.ChatMessages;

public class WorldProtectListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            final Player player = (Player) event.getRemover();

            if (Museum.getInstance().canChangeWorld(player)) {
                return;
            }

            player.sendMessage(ChatMessages.getMessage("world.frozen"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPlaceIntoFrame(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        Entity entity = event.getRightClicked();
        if (entity instanceof ItemFrame || entity instanceof ArmorStand) {
            player.sendMessage(ChatMessages.getMessage("world.frozen"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerMessArmorStands(PlayerArmorStandManipulateEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }

    /**
     * Prevent TNT and creepers from blowing up blocks.
     * 
     * @param event
     */
    @EventHandler
    public void onBlockExplode(EntityExplodeEvent event) {
        event.blockList().clear();
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreakDrops(BlockDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractDoors(PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }

        if (event.getClickedBlock().getType().isInteractable()) {
            player.sendMessage(ChatMessages.getMessage("world.frozen"));
            event.setCancelled(true);
            return;
        }

        switch (event.getClickedBlock().getType()) {
            // wood door & trapdoor types
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case CRIMSON_DOOR:
            case DARK_OAK_DOOR:
            case JUNGLE_DOOR:
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case WARPED_DOOR:

            case ACACIA_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case CRIMSON_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case OAK_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
            case WARPED_TRAPDOOR:

            case CHEST:
            case TRAPPED_CHEST:
            case CHEST_MINECART:
            case SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case BARREL:
            case ENDER_CHEST:
            case CRAFTING_TABLE:
            case SMITHING_TABLE:

                // remove items from item frames
            case ITEM_FRAME:

                // flower pot types
            case FLOWER_POT:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZALEA_BUSH:
            case POTTED_AZURE_BLUET:
            case POTTED_BAMBOO:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_CORNFLOWER:
            case POTTED_CRIMSON_FUNGUS:
            case POTTED_CRIMSON_ROOTS:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_FLOWERING_AZALEA_BUSH:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_LILY_OF_THE_VALLEY:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WARPED_FUNGUS:
            case POTTED_WARPED_ROOTS:
            case POTTED_WHITE_TULIP:
            case POTTED_WITHER_ROSE:

            case BEACON:
                event.setCancelled(true);
                return;
            default:
                // nothingness
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player) || (event.getClickedInventory() == null)) {
            return;
        }

        final Player player = (Player) event.getWhoClicked();

        if (Museum.getInstance().canChangeWorld(player)) {
            return;
        }

        player.sendMessage(ChatMessages.getMessage("world.frozen"));
        event.setCancelled(true);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        Entity[] entities = chunk.getEntities();

        // Disable all living entities AI and prevent them from being damaged or pushed around.
        for (Entity entity : entities) {
            if (!(entity instanceof LivingEntity)) {
                continue;
            }

            LivingEntity livingEntity = (LivingEntity) entity;
            livingEntity.setAI(false);
            livingEntity.setInvulnerable(true);
            livingEntity.setCollidable(false);
            livingEntity.setPersistent(true);
            livingEntity.setRemoveWhenFarAway(false);
        }
    }
}
