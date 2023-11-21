package com.xg7network.xg7lobby.Utils.Inventory;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Inventory {

    private org.bukkit.inventory.Inventory inv;
    private int id;
    private static List<InventoryItem> items = new ArrayList<>();

    private Player player;
    private ItemStack fillItem;

    /*
     * Essa classe foi feita para ser mais fácil de guardar os inventários e
     * achá-las dentro da config.
     */

    public Inventory(String path, Player player) {
        if (configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("inventories") != null) {

            this.id = configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".id");
            this.inv = Bukkit.createInventory(player, configManager.getConfig(ConfigType.SELECTORS).getInt(path + ".rows") * 9);
            this.player = player;

            if (configManager.getConfig(ConfigType.SELECTORS).getString(path + ".items.fill-item") != null && !(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".items.fill-item").equals("AIR"))) {
                if (configManager.getConfig(ConfigType.SELECTORS).getString(path + ".items.fill-item").contains(", ")) {
                    String[] materialByte = configManager.getConfig(ConfigType.SELECTORS).getString(path + ".items.fill-item").split(", ");
                    MaterialData data = new MaterialData(Material.valueOf(materialByte[0].toUpperCase()), Byte.parseByte(materialByte[1]));

                    this.fillItem = data.toItemStack(1);
                    ItemMeta meta = fillItem.getItemMeta();
                    meta.setDisplayName("");
                    meta.setLore(new ArrayList<>());
                    fillItem.setItemMeta(meta);
                    for (int i = 0; i < inv.getSize(); i++) {
                        inv.setItem(i, fillItem);
                    }

                } else {
                    this.fillItem = new ItemStack(Material.valueOf(configManager.getConfig(ConfigType.SELECTORS).getString(path + ".items.fill-item").toUpperCase()));
                    ItemMeta meta = fillItem.getItemMeta();
                    meta.setDisplayName("");
                    meta.setLore(new ArrayList<>());
                    fillItem.setItemMeta(meta);
                    for (int i = 0; i < inv.getSize(); i++) {
                        inv.setItem(i, fillItem);
                    }
                }
            }

            for (String s : configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection(path + ".items").getKeys(false)) {
                if (!s.equals("fill-item")) {
                    InventoryItem item = new InventoryItem(path + ".items." + s, this, player);
                    inv.setItem(item.getSlot(), item.getItemStack());
                    items.add(item);
                }
            }
        }

    }

    public int getId() {
        return id;
    }

    public ItemStack getFillItem() {
        return fillItem;
    }

    public org.bukkit.inventory.Inventory getInv() {
        return this.inv;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void open() {
        player.openInventory(inv);
    }
}
