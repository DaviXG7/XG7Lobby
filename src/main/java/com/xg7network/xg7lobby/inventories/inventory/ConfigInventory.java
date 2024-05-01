package com.xg7network.xg7lobby.inventories.inventory;

import com.xg7network.xg7lobby.inventories.Action;
import com.xg7network.xg7menus.API.Inventory.Menus.InventoryItem;
import com.xg7network.xg7menus.API.Inventory.Menus.Others.StandardMenu;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigInventory extends StandardMenu {

    private HashMap<Integer, List<Action>> actions = new HashMap<>();
    private HashMap<String, InventoryItem> storedItems = new HashMap<>();

    public ConfigInventory(String title, int size, String id) {
        super(title, size, id);
    }

    public ConfigInventory(String title, int size, Player player, String id) {
        super(title, size, player, id);
    }

    public List<Action> getAction(int slot) {
        return actions.get(slot);
    }

    public void setActions(HashMap<Integer, List<Action>> actions) {
        this.actions = actions;
    }

    public void setStoredItems(HashMap<String, InventoryItem> storedItems) {
        this.storedItems = storedItems;
    }

    public InventoryItem getStoredItem(String path) {
        return storedItems.get(path);
    }
}
