package com.xg7network.xg7lobby.Inventories;

import com.xg7network.xg7menus.API.Inventory.Menus.Menu;
import com.xg7network.xg7menus.API.Inventory.Menus.Others.StandardMenu;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ConfigInventory extends StandardMenu {

    private HashMap<Integer, List<String>> actions = new HashMap<>();

    public ConfigInventory(FileConfiguration configuration, Player player) {
        super(configuration.getString("title"), configuration.getInt("rows") * 9, player, configuration.getInt("id"));
    }



    public static ConfigInventory fromConfig() {



    }





}
