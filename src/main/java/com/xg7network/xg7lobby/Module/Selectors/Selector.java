package com.xg7network.xg7lobby.Module.Selectors;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.CustomInventories.SelectorItem;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Selector {

    private List<SelectorItem> items = new ArrayList<>();

    private Player player;

    public Selector(Player player) {

        this.player = player;
        if (configManager.getConfig(ConfigType.SELECTORS).getBoolean("selectors.enabled")) {
            if (PluginUtil.isInWorld(player)) {
                if (configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("selectors.items") != null) {
                    for (String path : configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("selectors.items").getKeys(false)) {
                            items.add(new SelectorItem("selectors.items." + path, player));
                    }

                }
            }
        }
    }

    public void giveItems() {
        for (SelectorItem item : items) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {

                    if (Objects.equals(new NBTItem(player.getInventory().getItem(i)).getString("id"), item.getId())) return;
                }
            }
        }

        for (SelectorItem item : items) if (item.getSlot() != -1) player.getInventory().setItem(item.getSlot(), item.getItemStack());

    }

    public void removeItems() {
        for (SelectorItem item : items) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {
                    if (Objects.equals(new NBTItem(player.getInventory().getItem(i)).getString("id"), item.getId())) player.getInventory().remove(item.getItemStack());
                }
            }
        }
    }

    public List<SelectorItem> getItems() {
        return items;
    }

    public SelectorItem getItemByName(String name) {

        for (SelectorItem selectorItem : items) {
            if (selectorItem.getName().equals(name))
                return selectorItem;
        }
        return null;

    }

}
