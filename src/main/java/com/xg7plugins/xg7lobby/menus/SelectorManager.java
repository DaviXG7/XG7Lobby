package com.xg7plugins.xg7lobby.menus;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.PlayerMenu;
import lombok.Getter;

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

            menu.addItems(MenuManager.getItemByConfig(Config.getConfig(ConfigType.SELECTOR), path, slot));

        }

        Config.getConfigurationSections(ConfigType.SELECTOR, "stored-items").forEach(s -> storedItems.put(s, MenuManager.getItemByConfig(Config.getConfig(ConfigType.SELECTOR), "stored-items." + s + ".", -1)));

    }

}
