package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.MenuType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Menu {

    protected List<InventoryItem> items;
    protected Inventory inventory;
    protected MenuType type;

    public Menu(MenuType type, String title, int size) {
        this.type = type;
        this.inventory = Bukkit.createInventory(null, size, TextUtil.get(title));
    }
    public Menu(MenuType type, String title, int size, Player player) {
        this.type = type;
        this.inventory = Bukkit.createInventory(player, size, TextUtil.get(title));
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Menu setFillItem(ItemStack item) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (!this.inventory.getItem(i).getType().equals(Material.AIR)) {
                this.inventory.setItem(i, item);
            }
        }
        return this;
    }

    public Menu setFillItem(InventoryItem item) {
        for (int i = 0; i < this.inventory.getSize(); i++) {
            if (!this.inventory.getItem(i).getType().equals(Material.AIR)) {
                this.inventory.setItem(i, item.getItemStack());
            }
        }
        return this;
    }

    public Menu addItems(InventoryItem... items) {
        for (InventoryItem item : items) {
            this.inventory.setItem(item.getSlot(), item.getItemStack());
            this.items.add(item);
        }
        return this;
    }

    public InventoryItem getItem(ItemStack itemStack) {
        return items.stream().filter(item -> new NBTItem(itemStack).getString("xg7mid").equals(item.getId())).findFirst().orElse(null);
    }
    public InventoryItem getItem(int slot) {
        return items.stream().filter(item -> item.getSlot() == slot).findFirst().orElse(null);
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public MenuType getType() {
        return this.type;
    }
}
