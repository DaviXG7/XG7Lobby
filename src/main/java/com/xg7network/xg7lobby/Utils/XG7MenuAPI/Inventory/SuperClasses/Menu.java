package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.Manager.MenuManager;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.MenuType;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Menu {

    protected List<InventoryItem> items = new ArrayList<>();
    protected Inventory inventory;
    protected MenuType type;

    public Menu(MenuType type, String title, int size) {
        this.type = type;
        this.inventory = Bukkit.createInventory(null, size, TextUtil.get(title));
    }
    public Menu(MenuType type, String title, int size, Player player) {
        this.type = type;
        this.inventory = Bukkit.createInventory(player, size, TextUtil.get(title, player));
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
        if (itemStack != null) {
            for (InventoryItem item : items) {
                if (new NBTItem(itemStack).getString("xg7mid").equals(item.getId())) {
                    return item;
                }
            }
        }
        return null;
    }
    public InventoryItem getItem(int slot) {
        return items.stream().filter(item -> item.getSlot() == slot).findFirst().orElse(null);
    }

    public void open(Player player) {
        MenuManager.register(this);
        player.openInventory(inventory);
    }

    public MenuType getType() {
        return this.type;
    }
}
