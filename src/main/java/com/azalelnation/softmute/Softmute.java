package com.azalelnation.softmute;

import com.earth2me.essentials.Essentials;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public final class Softmute extends JavaPlugin {

    private DataStore dataStore;
    private Essentials ess;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        dataStore = new FileDataStore(this);
        ConsoleCommandSender console = getServer().getConsoleSender();
        showBanner(console);

        ess = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        if (ess != null) {
            console.sendMessage(Util.colorize("&a[Softmute] Using Essentials to fetch offline player data!"));
        } else {
            console.sendMessage(Util.colorize("&c[Softmute] Essentials not found, Softmute will only work on online player!"));
        }

        getCommand("softmute").setExecutor(new Command(dataStore, this));
        getServer().getPluginManager().registerEvents(new ChatListener(dataStore), this);
    }

    @Override
    public void onDisable() {
        showBanner(getServer().getConsoleSender());
        getLogger().info("Saving data..");
        dataStore.saveData();
    }

    public Essentials getEssentials() {
        return ess;
    }

    public void showBanner(CommandSender sender) {
        sender.sendMessage(Util.colorize("&4 ---  &1-   |"));
        sender.sendMessage(Util.colorize("&4|___| &1| \\ |   &rSoftmute version &b" + getDescription().getVersion()));
        sender.sendMessage(Util.colorize("&4|   | &1|   -   &rShamelessly ripped from &cGriefPrevention &r| &eShivelight &4&l❤"));
    }
}
