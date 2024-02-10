package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems.Page;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.MenuType;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Page extends Menu {

    private PagesMenu menu;
    private int index;

    public Page(String title, PagesMenu menu, int index) {
        super(MenuType.PAGE, title.replace("%page%", index + ""), 54);
        this.menu = menu;
        this.index = index;
    }

    public Page(String title, Player player, PagesMenu menu, int index) {
        super(MenuType.PAGE, title.replace("%page%", index + ""), 54, player);
        this.menu = menu;
        this.index = index;
    }

    public List<ItemStack> addItems(List<ItemStack> items) {

        List<ItemStack> itemStackList = items;

        for (int i = 0; i < 45; i++) {
            if (!itemStackList.isEmpty()) {
                this.inventory.setItem(i, itemStackList.get(0));
                itemStackList.remove(0);
            } else {
                return null;
            }

        }
        return itemStackList;

    }

    public int getIndex() {
        return index;
    }

    public PagesMenu getPageMenu() {
        return menu;
    }
}
