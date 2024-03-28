package com.xg7network.xg7lobby.Inventories;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ConfigInventory extends Sta{
    public ConfigInventory(FileConfiguration configuration, Player player) {
        super(configuration.getString("title"), configuration.getInt("rows") * 9, player);
    }
}
