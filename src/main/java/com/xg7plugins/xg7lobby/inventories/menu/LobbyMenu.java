package com.xg7plugins.xg7lobby.inventories.menu;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.events.ClickEvent;
import com.xg7plugins.libs.xg7menus.events.MenuEvent;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.gui.Menu;
import com.xg7plugins.utils.Condition;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.InventoryManager;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LobbyMenu extends Menu {

    private final HashMap<Integer, String> paths;
    private final HashMap<String, LobbyItem> items;
    private XMaterial fillItem;
    private Config config;

    public LobbyMenu(Config config, String id, String title, InventoryType type, HashMap<Integer, String> paths, HashMap<String, LobbyItem> items, XMaterial fillItem) {
        super(XG7Lobby.getInstance(), id, title, type);

        this.config = config;
        this.paths = paths;
        this.items = items;
        this.fillItem = fillItem;
    }

    public LobbyMenu(Config config, String id, String title, int size, HashMap<Integer, String> paths, HashMap<String, LobbyItem> items, XMaterial fillItem) {
        super(XG7Lobby.getInstance(), id, title, size);

        this.config = config;
        this.paths = paths;
        this.items = items;
        this.fillItem = fillItem;

    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected List<Item> items(Player player) {

        List<Item> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (paths.containsKey(i)) {
                String path = paths.get(i);

                LobbyItem lobbyItem = this.items.get(path);

                if (lobbyItem.getCondition().getFirst().apply(new Condition.ConditionPack(player, Text.format(lobbyItem.getCondition().getSecond()).getTextFor(player)))) {
                    items.add(lobbyItem.getItem().slot(i));
                    continue;
                }
                if (lobbyItem.getOtherItemPath() == null) {
                    if (fillItem != XMaterial.AIR) items.add(Item.from(fillItem).name(" "));
                    continue;
                }

                items.add(InventoryManager.fromConfig(config, lobbyItem.getOtherItemPath()).getItem().slot(i));

            } else if (fillItem != XMaterial.AIR) {
                items.add(Item.from(fillItem).name(" ").slot(i));
            }
        }

        return items;
    }

    @Override
    public <T extends MenuEvent> void onClick(T event) {
        if (!(event instanceof ClickEvent)) return;
        Item clickedItem = ((ClickEvent) event).getClickedItem();
        if (clickedItem == null || clickedItem.getItemStack() == null || clickedItem.isAir()) return;
        event.setCancelled(true);

        List<String> actions = (List<String>) clickedItem.getTag("actions", List.class).orElse(Collections.emptyList()).stream().map(action -> {
            if (action.toString().startsWith("[SWAP] ")) {
                return "[SWAP] " + id + ", " + ((ClickEvent) event).getClickedSlot() + ", " + action.toString().replace("[SWAP] ", "");
            }
            return action;
        }).collect(Collectors.toList());

        XG7Lobby.getInstance().getActionsProcessor().process(actions, (Player) event.getWhoClicked());

    }

}
