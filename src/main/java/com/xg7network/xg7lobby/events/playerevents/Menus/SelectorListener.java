package com.xg7network.xg7lobby.events.playerevents.Menus;

import com.xg7network.xg7lobby.config.ConfigManager;
import com.xg7network.xg7lobby.config.ConfigType;
import com.xg7network.xg7lobby.events.playerevents.Handler.PlayersHandler;
import com.xg7network.xg7lobby.inventories.managers.SelectorManager;
import com.xg7network.xg7menus.API.Inventory.Manager.Managers.ItemsInventoryManager;
import com.xg7network.xg7menus.API.Inventory.Menus.Others.ItemsInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SelectorListener implements PlayersHandler {
    private static HashMap<UUID, ItemsInventory> inventories = new HashMap<>();

    @Override
    public boolean isEnabled() {
        return ConfigManager.getConfig(ConfigType.SELECTORS).getBoolean("selectors.enabled");
    }

    @Override
    public void init() {
        Bukkit.getOnlinePlayers().stream().filter(this::isPlayerInWorld).forEach(SelectorManager::addPlayer);
    }

    @Override
    public void onJoinWorld(Player player) {
        ItemsInventoryManager.register(player.getUniqueId(), player.getInventory());
        SelectorManager.addPlayer(player);
    }

    @Override
    public void onQuitWorld(Player player) {
        SelectorManager.removePlayer(player);
    }
}
