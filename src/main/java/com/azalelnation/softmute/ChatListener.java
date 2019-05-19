package com.azalelnation.softmute;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;

public class ChatListener implements Listener {

    private DataStore dataStore;

    ChatListener(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnline()) {
            event.setCancelled(true);
            return;
        }

        String message = event.getMessage();
        Set<Player> recipients = event.getRecipients();

        if (dataStore.isMuted(player.getUniqueId())) {
            String notificationMessage = "(Muted " + player.getName() + "): " + message;
            Set<Player> recipientsToKeep = new HashSet<>();
            for (Player recipient : recipients) {
                if(dataStore.isMuted(recipient.getUniqueId())) {
                    recipientsToKeep.add(recipient);
                } else if (recipient.hasPermission("softmute.eavesdrop")) {
                    recipient.sendMessage(ChatColor.GRAY + notificationMessage);
                }
            }
            recipients.clear();
            recipients.addAll(recipientsToKeep);
        }
    }

}
