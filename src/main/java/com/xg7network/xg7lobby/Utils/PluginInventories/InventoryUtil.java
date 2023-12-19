package com.xg7network.xg7lobby.Utils.PluginInventories;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.CustomInventories.InventoryItem;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class InventoryUtil {

    private Inventory inventory;
    private Player player;

    private List<Item> itemStacks = new ArrayList<>();

    public InventoryUtil(Player player, int rows, String title) {

        this.inventory = Bukkit.createInventory(player, rows * 9, new TextUtil(title).get(player));
        this.player = player;

    }

    public List<Item> getItemStacks() {
        return itemStacks;
    }

    public void execute(ItemStack stack) {
        if (stack != null) {
            for (Item item : itemStacks) {
                if (stack.equals(item.getItemStack())) item.execute();
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

    public void createItemStack(Player player, String material, String name, String lore, boolean glow, int slot, int ammount, List<Action> actions) {

        Item item = new Item(player, material, name, lore, glow, slot -1, ammount, actions);
        itemStacks.add(item);
        inventory.setItem(item.getSlot(), item.getItemStack());

    }

    public static Item getItemStack(Player player, String material, String name, String lore, boolean glow, int slot, int ammount, List<Action> actions) {

        return new Item(player, material, name, lore, glow, slot -1, ammount, actions);

    }

}
