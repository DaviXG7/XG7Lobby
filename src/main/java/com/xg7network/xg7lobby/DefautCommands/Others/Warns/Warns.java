package com.xg7network.xg7lobby.DefautCommands.Others.Warns;

import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.Action.Action;
import com.xg7network.xg7lobby.Utils.CustomInventories.InventoryItem;
import com.xg7network.xg7lobby.Utils.PluginInventories.InventoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class Warns implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
            return true;
        }

        Player player = (Player) commandSender;

        WarnsGUIManager.update();

        WarnsGUIManager.get(player).open();



        return true;

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        for (InventoryUtil inventoryUtil : WarnsGUIManager.get(player).getInventoryUtils()) {
            if (inventoryUtil.getInventory().equals(event.getClickedInventory())) {

                inventoryUtil.execute(event.getCurrentItem());

                event.setCancelled(true);

            }
        }

    }


}
