package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.Manager.MenuManager;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.MenuType;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.Menu;
import org.bukkit.entity.Player;

public class BasicMenu extends Menu {
    public BasicMenu(String title, int size) {
        super(MenuType.BASIC, title, size);
    }

    public BasicMenu(String title, int size, Player player) {
        super(MenuType.BASIC, title, size, player);
    }
}
