package com.xg7network.xg7lobby.Module.Selectors;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.CustomInventories.SelectorItem;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
                    if (player.getInventory().getItem(i).isSimilar(item.getItemStack())) return;
                }
            }
        }

        for (SelectorItem item : items) if (item.getSlot() != -1) player.getInventory().setItem(item.getSlot(), item.getItemStack());

    }

    public void removeItems() {
        for (SelectorItem item : items) {
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (player.getInventory().getItem(i) != null) {
                    if (player.getInventory().getItem(i).isSimilar(item.getItemStack())) player.getInventory().remove(item.getItemStack());
                }
            }
        }
    }

    public List<SelectorItem> getItems() {
        return items;
    }

}
