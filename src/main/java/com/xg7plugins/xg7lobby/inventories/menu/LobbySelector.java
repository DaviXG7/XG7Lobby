package com.xg7plugins.xg7lobby.inventories.menu;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.MenuPrevents;
import com.xg7plugins.libs.xg7menus.events.ClickEvent;
import com.xg7plugins.libs.xg7menus.events.MenuEvent;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.player.PlayerMenu;
import com.xg7plugins.tasks.CooldownManager;
import com.xg7plugins.utils.Condition;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.InventoryManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class LobbySelector extends PlayerMenu {

    private HashMap<Integer, String> paths;
    private HashMap<String, LobbyItem> items;
    private Config config;

    public LobbySelector(Config config, String id, HashMap<Integer, String> paths, HashMap<String, LobbyItem> items) {
        super(XG7Lobby.getInstance(), id, true);

        this.config = config;
        this.paths = paths;
        this.items = items;

        setMenuPrevents(new HashSet<>());
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
                if (lobbyItem.getCondition().getFirst().apply(new Condition.ConditionPack(player, Text.format(lobbyItem.getCondition().getSecond()).getTextFor(player)))) {
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
        if (!(event instanceof ClickEvent)) return;
        Item clickedItem = ((ClickEvent) event).getClickedItem();
        if (clickedItem == null || clickedItem.getItemStack() == null || clickedItem.isAir()) return;
        if (!event.getClickAction().isRightClick()) return;
        event.setCancelled(true);

        if (XG7Plugins.getInstance().getCooldownManager().containsPlayer("selector-click", (Player) event.getWhoClicked())) {

            double cooldownToToggle = XG7Plugins.getInstance().getCooldownManager().getReamingTime("selector-click", (Player) event.getWhoClicked());
            Text.formatLang(XG7Lobby.getInstance(), event.getWhoClicked(), "selector-cooldown").thenAccept(text -> text
                    .replace("[PLAYER]", event.getWhoClicked().getName())
                    .replace("[MILLISECONDS]", String.valueOf((cooldownToToggle)))
                    .replace("[SECONDS]", String.valueOf((int) ((cooldownToToggle) / 1000)))
                    .replace("[MINUTES]", String.valueOf((int) ((cooldownToToggle) / 60000)))
                    .replace("[HOURS]", String.valueOf((int) ((cooldownToToggle) / 3600000)))
                    .send(event.getWhoClicked()));

            return;
        }


        List<String> actions = (List<String>) clickedItem.getTag("actions", List.class).orElse(Collections.emptyList()).stream().map(action -> {
            if (action.toString().startsWith("[SWAP] ")) {
                return "[SWAP] " + id + ", " + ((ClickEvent) event).getClickedSlot() + ", " + action.toString().replace("[SWAP] ", "");
            }
            return action;
        }).collect(Collectors.toList());

        XG7Lobby.getInstance().getActionsProcessor().process(actions, (Player) event.getWhoClicked());

        XG7Plugins.getInstance().getCooldownManager().addCooldown((Player) event.getWhoClicked(), "selector-click", config.getTime("cooldown-to-use").orElse(2000L) + 0.0);

    }
}
