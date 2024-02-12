package com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems.Page;

import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.Manager.MenuManager;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PagesMenu {
    private List<Page> pages = new ArrayList<>();

    public PagesMenu(List<ItemStack> itemStacks, String title) {
        List<ItemStack> stackList = new ArrayList<>(itemStacks);
        int index = 0;
        while (!stackList.isEmpty()) {
            Page page = new Page(title, this, index);
            stackList = page.addListOfItems(stackList);
            index++;
            pages.add(page);
        }

        if (pages.isEmpty()) {
            pages.add(new Page(title, this, 0));
        }
    }

    public void addItem(InventoryItem item) {

        if (-1 < item.getSlot() && item.getSlot() <=8) {
            item.setSlot(item.getSlot() + 45);
            for (Page page : pages) {
                page.addItems(item);
            }
        } else {
            Bukkit.getLogger().severe("Items of page inventory only sopports slots 0 to 8 in the bottom of the page, the list of items will be on top.");
        }
    }

    public void goNext(Player player, Inventory inventory) {
        for (Page page : pages) {
            if (page.getInventory().equals(inventory)) {
                if (page.getIndex() + 1 < pages.size()) {
                    pages.get(page.getIndex() + 1).open(player);
                }
                return;
            }
        }
    }

    public void goBack(Player player, Inventory inventory) {
        for (Page page : pages) {
            if (page.getInventory().equals(inventory)) {
                if (page.getIndex() - 1 >= 0) {
                    pages.get(page.getIndex() - 1).open(player);
                }
                return;
            }
        }
    }

    public List<Page> getPages() {
        return pages;
    }
}
