package com.azalelnation.softmute;

import com.earth2me.essentials.Essentials;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Command implements CommandExecutor {

    private static Softmute plugin;
    private DataStore dataStore;

    Command(DataStore dataStore, Softmute plugin) {
        this.plugin = plugin;
        this.dataStore = dataStore;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length != 0) {
            if (sender.hasPermission("softmute.use")) {
                OfflinePlayer player = resolvePlayer(args[0]);
                if (player != null) {
                    boolean muted = dataStore.toggleMute(player.getUniqueId());
                    if (muted) {
                        sender.sendMessage(Util.colorize("&aSoftmuted " + player.getName()));
                    } else {
                        sender.sendMessage(Util.colorize("&aUn-softmued " + player.getName()));
                    }
                } else {
                    sender.sendMessage(Util.colorize("&cCannot resolve player name!"));
                }
                return true;
            }
        }

        String authors = String.join(", ", plugin.getDescription().getAuthors());
        String version = plugin.getDescription().getVersion();
        sender.sendMessage(Util.colorize("Softmute &7version &c" + version));
        sender.sendMessage(Util.colorize("Authors: &c" + authors +"&c&l❤"));
        return true;
    }

    public OfflinePlayer resolvePlayer(String name) {
        Player player = plugin.getServer().getPlayerExact(name);
        if (player != null) {
            return player;
        }
        player = plugin.getServer().getPlayer(name);
        if (player != null) {
            return player;
        }
        Essentials ess = plugin.getEssentials();
        if (ess != null) {
            return ess.getUser(name).getBase();
        } else {
            return null;
        }
    }

}
