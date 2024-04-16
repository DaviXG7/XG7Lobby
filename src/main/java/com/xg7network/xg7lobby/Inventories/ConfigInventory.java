package com.xg7network.xg7lobby.Inventories;

import com.xg7network.xg7menus.API.Inventory.Menus.Others.StandardMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ConfigInventory extends StandardMenu {

    HashMap<Integer, List<String>> actions = new HashMap<>();

    public ConfigInventory(String title, int size, int id) {
        super(title, size, id);
    }

    public ConfigInventory(String title, int size, Player player, int id) {
        super(title, size, player, id);
    }

    public List<String> getAction(int slot) {
        return actions.get(slot);
    }
    public void setAction(int slot, List<String> action) {
        actions.put(slot, action);
    }
}
