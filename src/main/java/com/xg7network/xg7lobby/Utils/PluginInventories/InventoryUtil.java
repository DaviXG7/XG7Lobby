package com.xg7network.xg7lobby.Utils.PluginInventories;

/*

    This class was made by me DaviXG7 to facilitate
    the creation of plugin items and inventories.

    The class is free to use if this text is copied
    into your plugin and your plugin is free.

    Thanks for reading/using my code <3

 */

import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryUtil {

    private Inventory inventory;
    private Player player;

    private List<Item> itemStacks = new ArrayList<>();

    public InventoryUtil(Player player, int rows, String title) {

        this.inventory = Bukkit.createInventory(player, rows * 9, TextUtil.get(title, player));
        this.player = player;

    }

    public List<Item> getItemStacks() {
        return itemStacks;
    }

    public void execute(ItemStack stack) {
        if (stack != null) {
            Item itemChose = null;
            for (Item item : itemStacks) {
                if (stack.getItemMeta().equals(item.getItemStack().getItemMeta())) {
                    itemChose = item;
                }
            }
            if (itemChose != null) {
                itemChose.execute();
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void open() {
        player.openInventory(inventory);
    }

    public void setItem(int slot, Item item) {
        itemStacks.add(item);
        inventory.setItem(slot - 1, item.getItemStack());
    }

    public void updateItem(Item item, String newName, String newLore) {
        int index = itemStacks.indexOf(item);
        ItemMeta meta = item.getItemStack().getItemMeta();
        meta.setDisplayName(TextUtil.get(newName, player));
        meta.setLore(Arrays.stream(TextUtil.get(newLore, player).split(" /// ")).toList());
        item.getItemStack().setItemMeta(meta);
        itemStacks.remove(index);
        itemStacks.add(index, item);
        inventory.setItem(item.getSlot(), item.getItemStack());
    }
    public void updateItem(Item item, String newName) {
        int index = itemStacks.indexOf(item);
        ItemMeta meta = item.getItemStack().getItemMeta();
        meta.setDisplayName(TextUtil.get(newName, player));
        item.getItemStack().setItemMeta(meta);
        itemStacks.remove(index);
        itemStacks.add(index, item);
        inventory.setItem(item.getSlot(), item.getItemStack());
    }

    public void createItemStack(Player player, String material, String name, String lore, boolean glow, int slot, int ammount, Action actions) {

        Item item = new Item(player, material, name, lore, glow, slot -1, ammount, actions);
        itemStacks.add(item);
        inventory.setItem(item.getSlot(), item.getItemStack());

    }

    public static Item getItemStack(Player player, String material, String name, String lore, boolean glow, int slot, int ammount, Action actions) {

        return new Item(player, material, name, lore, glow, slot -1, ammount, actions);

    }

}
