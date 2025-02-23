package com.xg7plugins.xg7lobby.inventories.menu;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.modules.xg7menus.MenuPermissions;
import com.xg7plugins.modules.xg7menus.events.ClickEvent;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.modules.xg7menus.menus.player.PlayerMenu;
import com.xg7plugins.utils.Condition;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.InventoryManager;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.swing.event.MenuEvent;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class LobbySelector extends PlayerMenu {

    private HashMap<Integer, String> paths;
    private HashMap<String, LobbyItem> items;
    private Config config;

    public LobbySelector(Config config, String id, HashMap<Integer, String> paths, HashMap<String, LobbyItem> items) {
        super(XG7Lobby.getInstance(), id,null,true);

        this.config = config;
        this.paths = paths;
        this.items = items;

        Set<MenuPermissions> permissions = new HashSet<>();

        permissions.add(MenuPermissions.PLAYER_DROP);
        permissions.add(MenuPermissions.PLAYER_PICKUP);
        permissions.add(MenuPermissions.PLAYER_INTERACT);
        permissions.add(MenuPermissions.PLAYER_PLACE_BLOCKS);
        permissions.add(MenuPermissions.PLAYER_BREAK_BLOCKS);

        setMenuPermissions(permissions);
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
                if (lobbyItem.getCondition().getFirst().apply(new Condition.ConditionPack(player, Text.format(lobbyItem.getCondition().getSecond()).textFor(player).getPlainText()))) {
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
    public void onClick(ClickEvent event) {

        Item clickedItem = event.getClickedItem();
        if (clickedItem == null || clickedItem.getItemStack() == null || clickedItem.isAir()) return;
        if (!event.getClickAction().isRightClick()) return;
        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getWhoClicked().getUniqueId(), false).join();
        if (!lobbyPlayer.isBuildEnabled()) event.setCancelled(true);

        if (!lobbyPlayer.isBuildEnabled() && XG7Plugins.getInstance().getCooldownManager().containsPlayer("selector-click", (Player) event.getWhoClicked())) {

            double cooldownToToggle = XG7Plugins.getInstance().getCooldownManager().getReamingTime("selector-click", (Player) event.getWhoClicked());
            Text.fromLang(event.getWhoClicked(),XG7Lobby.getInstance(), "selector-cooldown").thenAccept(text -> text
                    .replace("player", event.getWhoClicked().getName())
                    .replace("milliseconds", String.valueOf((cooldownToToggle)))
                    .replace("seconds", String.valueOf((int) ((cooldownToToggle) / 1000)))
                    .replace("minutes", String.valueOf((int) ((cooldownToToggle) / 60000)))
                    .replace("hours", String.valueOf((int) ((cooldownToToggle) / 3600000)))
                    .send(event.getWhoClicked()));

            return;
        }


        List<String> actions = (List<String>) clickedItem.getTag("actions", List.class).orElse(Collections.emptyList()).stream().map(action -> {
            if (action.toString().startsWith("[SWAP] ")) {
                return "[SWAP] " + id + ", " + event.getClickedSlot() + ", " + action.toString().replace("[SWAP] ", "");
            }
            return action;
        }).collect(Collectors.toList());

        XG7Lobby.getInstance().getActionsProcessor().process(actions, (Player) event.getWhoClicked());

        XG7Plugins.getInstance().getCooldownManager().addCooldown((Player) event.getWhoClicked(), "selector-click", config.getTime("cooldown-to-use").orElse(2000L) + 0.0);


    }
}
