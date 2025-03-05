package com.xg7plugins.xg7lobby.help.menu;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.modules.xg7menus.Slot;
import com.xg7plugins.modules.xg7menus.events.ClickEvent;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.modules.xg7menus.menus.gui.PageMenu;
import com.xg7plugins.modules.xg7menus.menus.holders.PageMenuHolder;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.actions.Action;
import com.xg7plugins.xg7lobby.actions.ActionType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ActionsMenu extends PageMenu {
    public ActionsMenu() {
        super(XG7Lobby.getInstance(), "actions-menu-help", "lang:[help.menu.actions-menu.title]", 54, Slot.of(2, 2), Slot.of(5, 8));
    }

    @Override
    public List<Item> pagedItems(Player player) {
        return Arrays.stream(ActionType.values()).map(Action::actionItem).collect(Collectors.toList());
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    protected List<Item> items(Player player) {
        return Arrays.asList(
                Item.from(XMaterial.matchXMaterial("BARRIER").orElse(XMaterial.OAK_DOOR)).name("lang:[help.menu.close-item]").slot(45),
                Item.from("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmM5OWFhNmZjMmVjY2UzNTY2NWQ5NDhhMDEzMjUxNTNmZTUzZmMxNzcxZmIyNzg0ZjU3OTY3ZjEwZTJkZGNmOCJ9fX0=").name("lang:[help.menu.go-back-item]").slot(52),
                Item.from("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThhZTExYTljOTQwYzVhYzYyYjkwNTgzN2QyMTUzN2RiZTJmM2U1MDExZjBiYmJmZGMxMTIyNGI4NjAzZGJiZCJ9fX0=").name("lang:[help.menu.go-next-item]").slot(53)
        );
    }

    @Override
    public void onClick(ClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        switch (event.getClickedSlot()) {
            case 45:
                plugin.getHelpCommandGUI().getMenu("index").open(player);
                break;
            case 52:
                ((PageMenuHolder)event.getInventoryHolder()).previousPage();
                break;
            case 53:
                ((PageMenuHolder)event.getInventoryHolder()).nextPage();
                break;
        }
    }
}
