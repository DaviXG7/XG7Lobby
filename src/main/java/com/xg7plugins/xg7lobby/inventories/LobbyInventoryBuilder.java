package com.xg7plugins.xg7lobby.inventories;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.builders.MenuBuilder;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.BaseMenu;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.menu.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class LobbyInventoryBuilder {

    private final String id;
    private final LobbyInventoryType type;
    private final Config config;
    private final String title;

    private final HashMap<String, LobbyItem> items = new HashMap<>();
    private final HashMap<Integer, String> slots = new HashMap<>();
    private final XMaterial fillItem;
    private final int size;

    public LobbyInventoryBuilder(String id, Config config) {
        this.id = id;
        this.config = config;
        this.title = config.get("title", String.class).orElse("No title");

        List<List<String>> slots = (List<List<String>>) config.getConfig().getList("grid");

        type = LobbyInventoryType.valueOf(config.get("type", String.class).orElse("NORMAL"));

        if (type == LobbyInventoryType.SELECTOR) {
            Collections.reverse(slots);
        }

        if (slots != null) {
            int index = 0;
            for (List<String> rows : slots) {
                for (String slot : rows) {
                    if (slot.isEmpty()) {
                        index++;
                        continue;
                    }
                    this.slots.put(index, slot);
                    index++;
                }
            }
        }

        ConfigurationSection section = config.getConfig().getConfigurationSection("items");

        if (section != null) {
            for (String key : section.getKeys(false)) items.put(key, InventoryManager.fromConfig(config, key));
        }

        this.fillItem = XMaterial.matchXMaterial(config.get("fill-material",String.class).orElse("AIR")).orElse(XMaterial.AIR);

        this.size = config.get("rows", Integer.class).orElse(6) * 9;

    }

    public BaseMenu loadMenu() {

        if (!type.equals(LobbyInventoryType.SELECTOR)) {
            return type == LobbyInventoryType.NORMAL ? new LobbyMenu(config, id, title, size, slots, items, fillItem)
                    : new LobbyMenu(config, id, title, type.toBukkitInventoryType(), slots, items, fillItem);
        }

        return new LobbySelector(config, id, slots, items);

    }
}
