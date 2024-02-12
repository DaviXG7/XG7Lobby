package com.xg7network.xg7lobby.DefautCommands.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems.Page.Page;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.InvAndItems.Page.PagesMenu;
import com.xg7network.xg7lobby.Utils.XG7MenuAPI.Inventory.SuperClasses.InventoryItem;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Warns implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
            return true;
        }

        Player player = (Player) commandSender;

        PlayerData data = PlayersManager.getData(player.getUniqueId().toString());

        List<ItemStack> warnsitems = new ArrayList<>();

        for (int i = 0; i < data.getInfractions().size(); i++) {
            warnsitems.add(new InventoryItem(Material.PAPER, "§f" + data.getInfractions().get(i).getWarn(), Collections.singletonList(data.getInfractions().get(i).getWhen()), 1, i, null).getItemStack());
        }
        PagesMenu pagesMenu = new PagesMenu(warnsitems, configManager.getConfig(ConfigType.SELECTORS).getString("warn-inventory.title"));

        pagesMenu.addItem(InventoryItem.getWarnItem("warn-inventory.go-next-item"));
        pagesMenu.addItem(InventoryItem.getWarnItem("warn-inventory.go-back-item"));

        pagesMenu.getPages().get(0).open(player);



        return true;

    }


}
