package com.xg7plugins.xg7lobby.menus;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.PlayerMenu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SelectorManager {

    @Getter
    private static PlayerMenu menu = null;
    @Getter
    private static final HashMap<String, InventoryItem> storedItems = new HashMap<>();

    public static void load() {


        menu = new PlayerMenu("xg7lselector");

        for (String s : Config.getConfigurationSections(ConfigType.SELECTOR, "items")) {

            String path = "items." + s + ".";
            int slot = Config.getInt(ConfigType.SELECTOR, path + "slot");

            if (Config.get(ConfigType.SELECTOR, path + "item") != null) {
                path = "stored-items." + Config.getString(ConfigType.SELECTOR, path + "item") + ".";
            }

            menu.addItems(MenuManager.getItemByConfig("xg7lselector", Config.getConfig(ConfigType.SELECTOR), path, slot));

        }

        if (Config.getConfigurationSections(ConfigType.SELECTOR, "stored-items") != null) Config.getConfigurationSections(ConfigType.SELECTOR, "stored-items").forEach(s -> storedItems.put(s, MenuManager.getItemByConfig("xg7lselector", Config.getConfig(ConfigType.SELECTOR), "stored-items." + s + ".", -1)));

    }

    public static void open(Player player) {
        menu.open(player);
        if (MenuManager.getSkulls().containsKey("xg7lselector")) MenuManager.getSkulls().get("xg7lselector").forEach(item -> {
            if (item.getSlot() == -1) return;
            menu.updateInventory(player, item.setOwner(player.getName(), Bukkit.getOnlineMode()));
        });

    }

}
