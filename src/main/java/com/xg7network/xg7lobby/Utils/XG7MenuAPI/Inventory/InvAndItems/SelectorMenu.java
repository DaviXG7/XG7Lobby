package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.MenuType;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.Menu;
import org.bukkit.entity.Player;

//Coming soon

public class SelectorMenu extends Menu {
    public SelectorMenu(String title, int size) {
        super(MenuType.SELECTOR, title, size);
    }

    public SelectorMenu(String title, int size, Player player) {
        super(MenuType.SELECTOR, title, size, player);
    }
}
