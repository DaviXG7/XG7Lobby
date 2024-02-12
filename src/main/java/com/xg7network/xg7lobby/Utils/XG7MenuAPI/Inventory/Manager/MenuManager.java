package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.Manager;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.Menu;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MenuManager {

    private static List<Menu> inventories = new ArrayList<>();

    public static void register(Menu... menus) {

        for (Menu menu : menus) {

            if (!inventories.contains(menu)) inventories.add(menu);

        }

    }
    public static void unregister(Menu... menus) {

        for (Menu menu : menus) {

            if (inventories.contains(menu)) inventories.remove(menu);

        }

    }
    public static void inicialize(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    protected static Menu contains(Inventory inventory) {
        for (Menu menu : inventories) {
            if (menu.getInventory().equals(inventory)) {
                return menu;
            }
        }
        return null;
    }
}
