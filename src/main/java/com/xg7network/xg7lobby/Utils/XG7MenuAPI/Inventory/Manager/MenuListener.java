package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.Manager;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.MenuType;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.InventoryItem;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Menu menu = MenuManager.contains(event.getClickedInventory());
        if (menu == null) return;
        event.setCancelled(true);
        switch (menu.getType()) {
            case BASIC:
            case PAGE:

                InventoryItem inventoryItem = menu.getItem(event.getCurrentItem());
                if (inventoryItem == null) return;
                inventoryItem.execute();

        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        Menu menu = MenuManager.contains(event.getInventory());
        if (menu == null) return;
        MenuManager.unregister(menu);


    }

}
