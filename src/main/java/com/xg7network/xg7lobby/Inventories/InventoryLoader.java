package com.xg7network.xg7lobby.Inventories;

import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryLoader {

    private static List<ConfigInventoryBuilder> inventories = new ArrayList<>();

    public static void load() throws IOException {

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

                inventories.add(
                        new ConfigInventoryBuilder(
                                configuration, null
                        )
                )

            }
        }
    }

    public static ConfigInventoryBuilder get(int id, Player player) throws Exception {

        if (configs.isEmpty()) return null;

        for (FileConfiguration configuration : configs) if (configuration.getInt("id") == id) return new ConfigInventoryBuilder(configuration, player);

        throw new Exception("[XG7LOBBY] MENU ID ERROR! Inventory with this id doesn't exits!");
    }

}
