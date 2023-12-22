package com.xg7network.xg7lobby.DefautCommands.Others.Warns;

import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.PluginInventories.Action;
import com.xg7network.xg7lobby.Utils.PluginInventories.InventoryUtil;
import com.xg7network.xg7lobby.Utils.PluginInventories.Item;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class WarnsGUI {

    private List<InventoryUtil> inventoryUtils = new ArrayList<>();

    private Player player;

    public WarnsGUI(Player player) {

        this.player = player;

        PlayerData data = PlayersManager.getData(player.getUniqueId().toString());

        List<Item> warnsitems = new ArrayList<>();

        for (int i = 0; i < data.getInfractions().size(); i++) {

            warnsitems.add(InventoryUtil.getItemStack(player, "PAPER", "§f" + data.getInfractions().get(i).getWarn(), data.getInfractions().get(i).getWhen(), false, i + 1, 1, null));

        }

        int page = 1;

        while (!warnsitems.isEmpty()) {
            InventoryUtil inventoryUtil = new InventoryUtil(player, 6, "&2Warns &fPage: &c" + page);

            int endIndex = Math.min(warnsitems.size(), 45);

            for (int i = 0; i < endIndex; i++) {
                inventoryUtil.setItem(i + 1, warnsitems.get(0));
                warnsitems.remove(0);
            }

            inventoryUtil.createItemStack(player, "REDSTONE", "&c&lGO BACK", " ", false, 46, 1, () -> {
                        int index = inventoryUtils.indexOf(inventoryUtil);
                        if (index - 1 != -1) {
                            player.closeInventory();
                            inventoryUtils.get(index - 1).open();
                        }
            });

            inventoryUtil.createItemStack(player, "EMERALD", "&a&lGO NEXT", " ", false, 54, 1, () -> {
                int index = inventoryUtils.indexOf(inventoryUtil);
                if (index + 1 < inventoryUtils.size()) {
                    player.closeInventory();
                    inventoryUtils.get(index + 1).open();
                }
            });

            inventoryUtils.add(inventoryUtil);
            page++;
        }

    }

    public void open() {
        if (inventoryUtils.isEmpty()) {
            player.sendMessage(prefix + ChatColor.GREEN + "You do not have warns!");
            return;
        }
        player.openInventory(inventoryUtils.get(0).getInventory());
    }

    public List<InventoryUtil> getInventoryUtils() {
        return inventoryUtils;
    }

    public Player getPlayer() {
        return player;
    }


}
