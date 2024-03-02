package com.xg7network.xg7lobby.Utils.CustomInventories.Config;

import com.xg7network.xg7menus.API.Inventory.MenuType;
import com.xg7network.xg7menus.API.Inventory.SuperClasses.InventoryItem;
import com.xg7network.xg7menus.API.Inventory.SuperClasses.Menu;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

public class ConfigInventory extends Menu {
    private int id;

    public ConfigInventory(String title, int size, int id, Player player) {
        super(MenuType.BASIC, title, size, player);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ConfigInventory fromConfig(FileConfiguration configuration, Player player) {
        ConfigInventory configInventory = new ConfigInventory(configuration.getString("name"), configuration.getInt("rows") * 9, configuration.getInt("id"), player);

        if (!Objects.equals(configuration.getString("items.fill-item"), "") || !Objects.equals(configuration.getString("items.fill-item"), "AIR")) {

            ItemStack fillItem = new ConfigInventoryItem(new ItemStack(Material.getMaterial(configuration.getString("items.fill-item"))), 0, new ArrayList<>(), player).getItemStack();

            configInventory.setFillItem(fillItem);

        }

        for (String path : configuration.getConfigurationSection("items").getKeys(false)) {
            if (!path.equals("fill-item")) {
                configInventory.addItems(ConfigInventoryItem.fromConfig(configuration, path, player));
            }
        }

        return configInventory;
    }
}
