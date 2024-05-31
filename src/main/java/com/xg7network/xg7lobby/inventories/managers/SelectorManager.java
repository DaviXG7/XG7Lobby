package com.xg7network.xg7lobby.inventories.managers;

import com.xg7network.xg7lobby.config.ConfigManager;
import com.xg7network.xg7lobby.config.ConfigType;
import com.xg7network.xg7lobby.inventories.action.Action;
import com.xg7network.xg7lobby.inventories.inventory.ConfigInventoryBuilder;
import com.xg7network.xg7menus.API.Inventory.Menus.InventoryItem;
import com.xg7network.xg7menus.API.Inventory.Menus.Others.PlayerSelector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SelectorManager {

    @Getter
    private static HashMap<UUID, PlayerSelector> selectors;
    @Getter
    private static ConfigInventoryBuilder builder;

    public static void load() throws Exception {
        selectors = new HashMap<>();
        builder = new ConfigInventoryBuilder(ConfigManager.getConfig(ConfigType.SELECTORS), "selectors.");
    }

    public static void addPlayer(Player player) {
        PlayerSelector selector = builder.buildPlayerSelector(player);
        selector.open(player);
        selectors.put(player.getUniqueId(), selector);
    }
    public static void removePlayer(Player player) {
        selectors.get(player.getUniqueId()).removeItems(player);
        selectors.remove(player.getUniqueId());
    }

}
