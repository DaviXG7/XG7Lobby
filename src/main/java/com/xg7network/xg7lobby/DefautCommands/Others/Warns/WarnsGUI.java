package com.xg7network.xg7lobby.DefautCommands.Others.Warns;

import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.PluginInventories.InventoryUtil;
import com.xg7network.xg7lobby.Utils.PluginInventories.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarnsGUI {

    private List<InventoryUtil> inventoryUtils = new ArrayList<>();

    private Player player;

    public WarnsGUI(Player player) {

        this.player = player;

        PlayerData data = PlayersManager.getData(player.getUniqueId().toString());

        List<Item> warnsitems = new ArrayList<>();

        for (int i = 0; i < data.getInfractions().size(); i++) {

            warnsitems.add(InventoryUtil.getItemStack(player, "PAPER", "§f" + data.getInfractions().get(i).getWarn(), data.getInfractions().get(i).getWhen().toString(), false, i + 1, 1, null));

        }

        while (!warnsitems.isEmpty()) {

            InventoryUtil inventoryUtil = new InventoryUtil(player, 6, "&6Warns");

            for (int i = 1; i < warnsitems.size(); i++) {

                if (i > 46) break;

                inventoryUtil.setItem(i, warnsitems.get(i - 1));
                warnsitems.remove(warnsitems.get(i - 1));



                inventoryUtil.createItemStack(player, "REDSTONE", "&c&lGO BACK", " ", false, 46, 1, Collections.singletonList(() -> {

                    player.closeInventory();

                    if (inventoryUtils.indexOf(inventoryUtil) + 1 < inventoryUtils.size())
                        inventoryUtils.get(inventoryUtils.indexOf(inventoryUtil) + 1).open();


                }));

                inventoryUtil.createItemStack(player, "EMERALD", "&a&lGO NEXT", " ", false, 54, 1, Collections.singletonList(() -> {

                    player.closeInventory();

                    if (inventoryUtils.indexOf(inventoryUtil) - 1 != -1)
                        inventoryUtils.get(inventoryUtils.indexOf(inventoryUtil) - 1).open();


                }));

                inventoryUtils.add(inventoryUtil);



            }

        }

    }

    public void open() {
        player.openInventory(inventoryUtils.get(0).getInventory());
    }

    public List<InventoryUtil> getInventoryUtils() {
        return inventoryUtils;
    }

    public Player getPlayer() {
        return player;
    }


}
