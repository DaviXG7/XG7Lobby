package com.xg7plugins.xg7lobby.inventories.menu;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.events.ClickEvent;
import com.xg7plugins.libs.xg7menus.events.MenuEvent;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.player.PlayerMenu;
import com.xg7plugins.utils.Condition;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.InventoryManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LobbySelector extends PlayerMenu {

    private HashMap<Integer, String> paths;
    private HashMap<String, LobbyItem> items;
    private Config config;

    public LobbySelector(Config config, String id, HashMap<Integer, String> paths, HashMap<String, LobbyItem> items) {
        super(XG7Lobby.getInstance(), id, true);

        this.config = config;
        this.paths = paths;
        this.items = items;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected List<Item> items(Player player) {

        List<Item> items = new ArrayList<>();

        for (int i = 0; i < 45; i++) {
            if (paths.containsKey(i)) {
                String path = paths.get(i);
                LobbyItem lobbyItem = this.items.get(path);
                if (lobbyItem.getCondition().getFirst().apply(new Condition.ConditionPack(player, lobbyItem.getCondition().getSecond()))) {
                    items.add(lobbyItem.getItem().slot(i));
                    continue;
                }
                if (lobbyItem.getOtherItemPath() == null) continue;


                items.add(InventoryManager.fromConfig(config, lobbyItem.getOtherItemPath()).getItem().slot(i));

            }
        }

        return items;
    }

    @Override
    public <T extends MenuEvent> void onClick(T event) {
        event.setCancelled(true);
        if (!(event instanceof ClickEvent)) return;

        List<String> actions = (List<String>) ((ClickEvent) event).getClickedItem().getTag("actions", List.class).orElse(Collections.emptyList()).stream().map(action -> {
            if (action.toString().startsWith("[SWAP] ")) {
                return "[SWAP] " + id + ", " + action.toString().replace("[SWAP] ", "");
            }
            return action;
        }).collect(Collectors.toList());

        XG7Lobby.getInstance().getActionsProcessor().process(actions);

    }
}
