package com.xg7network.xg7lobby.Utils.CustomInventories;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.Action.Action;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static com.xg7network.xg7lobby.Utils.Other.PluginUtil.isInWorld;
import static com.xg7network.xg7lobby.XG7Lobby.configManager;


public class InventoryListener implements Listener {

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (isInWorld(player)) {
            for (String s : configManager.getConfig(ConfigType.SELECTORS).getConfigurationSection("inventories").getKeys(false)) {
                Inventory inv = new Inventory("inventories." + s, player);
                if (inv.getInv() != null) {
                    for (InventoryItem item : inv.getItems()) {
                        if (item.getInv().equals(e.getClickedInventory())) {
                            if (e.getCurrentItem() != null) {
                                if (e.getCurrentItem().getItemMeta().equals(item.getItemStack().getItemMeta())) {
                                    for (String s2 : item.getActions()) {
                                        Action action = new Action(player, s2);
                                        e.setCancelled(true);
                                        action.execute();

                                    }
                                }

                            }
                            e.setCancelled(true);
                        }
                    }
                    if (e.isCancelled()) return;
                }
            }
            if (!e.isCancelled()) {

                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-interact-with-inventory")) {
                    e.setCancelled(!player.hasPermission(PermissionType.INV.getPerm()));
                } else if (configManager.getConfig(ConfigType.CONFIG).getBoolean("cancel-hotbar")) {
                    e.setCancelled(!player.hasPermission(PermissionType.INV.getPerm()) && (e.getClickedInventory().equals(player.getInventory()) && e.getRawSlot() <= 9));
                }

            }
        }
    }
}
