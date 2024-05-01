package com.xg7network.xg7lobby.inventories.selectors;

import com.xg7network.xg7lobby.config.ConfigManager;
import com.xg7network.xg7lobby.config.ConfigType;
import com.xg7network.xg7lobby.inventories.Action;
import com.xg7network.xg7lobby.inventories.inventory.ConfigInventoryBuilder;
import com.xg7network.xg7menus.API.Inventory.Manager.MenuManager;
import com.xg7network.xg7menus.API.Inventory.Menus.InventoryItem;
import com.xg7network.xg7menus.API.Inventory.Menus.Others.PlayerSelector;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SelectorManager {

    private static HashMap<UUID, PlayerSelector> selectors;
    private static HashMap<Integer, List<Action>> actions;
    private static HashMap<String, InventoryItem> storedItems;
    private static List<InventoryItem> items;

    public static void load() throws Exception {

        selectors = new HashMap<>();
        actions = new HashMap<>();
        storedItems = new HashMap<>();

        for (String path : ConfigManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("stored-items").getKeys(false)) {
            path = "stored-items." + path;

            String[] materialName = ConfigManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").split(", ");
            MaterialData data;

            String name = ConfigManager.getConfig(ConfigType.SELECTORS).getString(path + ".name");
            List<String> lore = ConfigManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore");
            int ammount = ConfigManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount");
            boolean glow = ConfigManager.getConfig(ConfigType.SELECTORS).getBoolean(path + ".glow");
            List<String> actions = ConfigManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".actions");
            storedItems.put(
                    path.split("\\.")[0],
                    ConfigInventoryBuilder.getItem(materialName,name,lore,ammount,-1,glow)
            );

        }


        for (String path : ConfigManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("items").getKeys(false)) {
            path = "items." + path;

            int slot = ConfigManager.getConfig(ConfigType.SELECTORS).getInt(path + ".slot") -1;

            if (ConfigManager.getConfig(ConfigType.SELECTORS).getString(path + ".stored-item") != null) path = "stored-items." + ConfigManager.getConfig(ConfigType.SELECTORS).getString(path + ".stored-item");


            String[] materialName = ConfigManager.getConfig(ConfigType.SELECTORS).getString(path + ".material").split(", ");
            MaterialData data;

            String name = ConfigManager.getConfig(ConfigType.SELECTORS).getString(path + ".name");
            List<String> lore = ConfigManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".lore");
            int ammount = ConfigManager.getConfig(ConfigType.SELECTORS).getInt(path + ".amount");
            boolean glow = ConfigManager.getConfig(ConfigType.SELECTORS).getBoolean(path + ".glow");
            List<String> actionsS = ConfigManager.getConfig(ConfigType.SELECTORS).getStringList(path + ".actions");

            actions.put(
                    slot,
                    actionsS.stream().map(Action::new).collect(Collectors.toList())
            );



        }

    }

    public PlayerSelector giveToPlayer(Player player) {
        PlayerSelector selector = new PlayerSelector("xg7playerselector");
        selector.addItems();
    }

}
