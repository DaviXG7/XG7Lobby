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
    private static List<Page> pages = new ArrayList<>();

    public PagesMenu(List<ItemStack> itemStacks, String title) {

        List<ItemStack> stackList = itemStacks;
        int index = 0;
        while (!stackList.isEmpty()) {
            index++;
            Page page = new Page(title,this,index);
            stackList = page.addListOfItems(itemStacks);
            pages.add(page);
        }

        if (pages.isEmpty()) pages.add(new Page(title, this, 0));

    }

    public void addItem(InventoryItem item) {
        if (-1 < item.getSlot() && item.getSlot() <= 8) {
            for (Page page : pages) {
                page.addItems(item.setSlot(item.getSlot() + 45));
            }
        } else {
            Bukkit.getLogger().log(Level.SEVERE, "Page inventories only support items at the bottom of the inventory, at the top are page items. Slots are from 0 to 8");
        }
    }

    public void goNext(Player player, Inventory inventory) {

        for (Page page : pages) {
            if (page.getInventory().equals(inventory)) {
                if (page.getIndex() + 1 != pages.size()) {
                    pages.get(page.getIndex() + 1).open(player);
                }
                return;
            }
        }

    }

    public void goBack(Player player, Inventory inventory) {

        for (Page page : pages) {
            if (page.getInventory().equals(inventory)) {
                if (page.getIndex() - 1 != -1) {
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
