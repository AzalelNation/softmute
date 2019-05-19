package com.azalelnation.softmute;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class FileDataStore implements DataStore {

    private ConcurrentHashMap<UUID, Boolean> players = new ConcurrentHashMap<>();
    private Softmute plugin;

    FileDataStore(Softmute plugin) {
        this.plugin = plugin;
        this.loadData();
    }

    @Override
    public void loadData() {
        File file = new File(plugin.getDataFolder(), "muteData");
        if (file.exists()) {
            BufferedReader in = null;
            try {
                in = new BufferedReader(new FileReader(file));
                String strID = in.readLine();
                while (strID != null) {
                    try {
                        players.put(UUID.fromString(strID), true);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Skipping corrupted data " + strID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    strID = in.readLine();
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Something went wrong when trying to read data!");
                e.printStackTrace();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {}
            }
        }
    }

    @Override
    public void saveData() {
        File file = new File(plugin.getDataFolder(), "muteData");
        BufferedWriter out = null;
        try {
            file.createNewFile();
            out = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<UUID, Boolean> entry : players.entrySet()) {
                if (entry.getValue()) {
                    out.write(entry.getKey().toString());
                    out.newLine();
                }
            }
        } catch (Exception e) {
            plugin.getLogger().severe("Something went wrong when trying to write data!");
            e.printStackTrace();
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {}
        }
    }

    @Override
    public boolean isMuted(UUID uuid) {
        if (players.get(uuid) == Boolean.TRUE) {
            return true;
        }
        return false;
    }

    @Override
    public boolean toggleMute(UUID uuid) {
        boolean val = !isMuted(uuid);
        players.put(uuid, val);
        saveData();
        return val;
    }

}
