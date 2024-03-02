package com.xg7network.xg7lobby.Utils.CustomInventories;

import com.xg7network.xg7lobby.Utils.CustomInventories.Config.ConfigInventory;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryLoader {

    private static List<FileConfiguration> configs = new ArrayList<>();

    public static void load() throws IOException {

        configs.clear();

        File file = new File(XG7Lobby.getPlugin().getDataFolder(), "Inventories");
        if (!file.exists()) {

            File profile = new File(XG7Lobby.getPlugin().getDataFolder(), "Inventories/profile.yml");
            File games = new File(XG7Lobby.getPlugin().getDataFolder(), "Inventories/games.yml");

            file.mkdirs();

            XG7Lobby.getPlugin().saveResource("Inventories/profile.yml", false);
            XG7Lobby.getPlugin().saveResource("Inventories/games.yml", false);

        }

        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {

                if (!file1.exists()) {
                    file1.createNewFile();
                }

                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file1);
                configs.add(configuration);

            }
        }
    }

    public static FileConfiguration getConfig(int id) throws Exception {

        if (configs.isEmpty()) return null;

        for (FileConfiguration configuration : configs) if (configuration.getInt("id") == id) return configuration;

        throw new Exception("Inventory with this id doesn't exits!");
    }

    public static ConfigInventory get(int id, Player player) throws Exception {

        if (configs.isEmpty()) return null;

        for (FileConfiguration configuration : configs) if (configuration.getInt("id") == id) return ConfigInventory.fromConfig(configuration, player);

        throw new Exception("[XG7LOBBY] MENU ID ERROR! Inventory with this id doesn't exits!");
    }

}
