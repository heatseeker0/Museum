package com.mcspacecraft.museum.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.mcspacecraft.museum.Museum;
import com.mcspacecraft.museum.MuseumConfig;
import com.mcspacecraft.museum.util.ChatMessages;

import io.papermc.paper.event.player.AsyncChatEvent;

public class ChatHandlerListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncChatEvent event) {
        final Player player = event.getPlayer();

        if (player.isOp() || player.hasPermission(MuseumConfig.ADMIN_PERMISSION_NODE)) {
            return;
        }

        if (Museum.getInstance().getMuseumConfig().isChatDisabled()) {
            event.setCancelled(true);
            player.sendMessage(ChatMessages.getMessage("chat-disabled"));
        }
    }
}
