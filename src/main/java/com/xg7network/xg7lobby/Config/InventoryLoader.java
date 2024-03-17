package com.xg7network.xg7lobby.Config;

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

        File file = new File(XG7Lobby.getPlugin().getDataFolder(), "inventories");
        if (!file.exists()) {

            file.mkdirs();

            XG7Lobby.getPlugin().saveResource("inventories/profile.yml", false);
            XG7Lobby.getPlugin().saveResource("inventories/games.yml", false);

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

    public static FileConfiguration getInventoryConfig(int id) throws Exception {

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
