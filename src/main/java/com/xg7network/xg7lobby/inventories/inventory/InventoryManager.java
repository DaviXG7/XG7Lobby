package com.xg7network.xg7lobby.inventories.inventory;

import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private static List<ConfigInventoryBuilder> inventories = new ArrayList<>();

    public static void load() throws Exception {

        inventories.clear();

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

                inventories.add(new ConfigInventoryBuilder(configuration));

            }
        }
    }

    public static ConfigInventory get(String id, Player player) throws Exception {
        if (inventories.isEmpty()) return null;

        for (ConfigInventoryBuilder configInventoryBuilder : inventories) if (configInventoryBuilder.getId().equals(id)) return configInventoryBuilder.build(player);

        throw new Exception("[XG7LOBBY] MENU ID ERROR! Inventory with this id doesn't exits!");
    }
    public static boolean contains(String id) {
        return inventories.stream().anyMatch(configInventoryBuilder -> configInventoryBuilder.getId().equals(id));
    }

}
