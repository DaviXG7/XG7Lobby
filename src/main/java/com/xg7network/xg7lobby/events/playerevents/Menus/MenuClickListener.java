package com.xg7network.xg7lobby.events.playerevents.Menus;

import com.xg7network.xg7lobby.inventories.inventory.ConfigInventory;
import com.xg7network.xg7lobby.inventories.managers.SelectorManager;
import com.xg7network.xg7menus.API.Inventory.Manager.Events.MenuClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class MenuClickListener implements Listener {

    @EventHandler
    public void onMenuClick(MenuClickEvent event) {
        Player player = event.getPlayer();
        if (event.getMenu() instanceof ConfigInventory) ((ConfigInventory) event.getMenu()).getAction(event.getSlot()).forEach(action -> action.execute(player));
        if (Objects.equals(SelectorManager.getSelectors().get(player.getUniqueId()).getId(), event.getMenu().getId())) SelectorManager.getBuilder().getActions().get(event.getSlot()).forEach(action -> action.execute(player));
    }

}
