package com.xg7network.xg7lobby.events.playerevents.Menus;

import com.xg7network.xg7menus.API.Inventory.Manager.Events.MenuClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MenuClickListener implements Listener {

    @EventHandler
    public void onMenuClick(MenuClickEvent event) {
        Player player = event.getPlayer();



    }

}
