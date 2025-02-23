package com.xg7plugins.xg7lobby.inventories.warn_menu;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.modules.xg7menus.Slot;
import com.xg7plugins.modules.xg7menus.item.ClickableItem;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.modules.xg7menus.menus.holders.PageMenuHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WarnMenuHolder extends PageMenuHolder {

    private Player target;

    public WarnMenuHolder(String id, Plugin plugin, String title, int size, InventoryType type, WarnMenu pageMenu, Player player, Player target) {
        super(id, plugin, title, size, type, pageMenu, player);
        this.target = target;
    }

    @Override
    public void goPage(int page) {
        if (page < 0) return;
        if (page > getMaxPages()) return;
        this.currentPage = page;

        CompletableFuture.runAsync(() -> {

            WarnMenu pageMenu = (WarnMenu) this.menu;

            List<Item> pagedItems = pageMenu.pagedItems(player,target);

            List<Item> itemsToAdd = pagedItems.subList(page * getArea(), pagedItems.size());

            int index = 0;

            for (int x = pageMenu.getStartEdge().getRow(); x <= pageMenu.getEndEdge().getRow(); x++) {
                for (int y = pageMenu.getStartEdge().getColumn(); y <= pageMenu.getEndEdge().getColumn(); y++) {
                    if (index >= itemsToAdd.size()) {
                        if (inventory.getItem(Slot.get(x,y)) != null) inventory.setItem(Slot.get(x,y), new ItemStack(Material.AIR));
                        continue;
                    }
                    inventory.setItem(Slot.get(x,y), itemsToAdd.get(index).getItemFor(player, plugin));

                    if (itemsToAdd.get(index) instanceof ClickableItem) {
                        int finalIndexToAdd = index;
                        this.updatedClickEvents.compute(Slot.get(x,y), (k, v) -> ((ClickableItem)itemsToAdd.get(finalIndexToAdd)).getOnClick());
                        index++;
                        continue;
                    }
                    this.updatedClickEvents.remove(Slot.get(x,y));
                    index++;
                }
            }


        }, XG7Plugins.taskManager().getAsyncExecutors().get("menus"));
    }



}
