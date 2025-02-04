package com.xg7plugins.xg7lobby.commands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.libs.xg7menus.menus.BaseMenu;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.actions.ActionException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "open",
        permission = "xg7lobby.command.open",
        description = "Open the inventory",
        syntax = "/open <id>",
        isPlayerOnly = true
)
public class OpenInventoryCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.CHEST, this);
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        if (args.len() != 1) {
            syntaxError(sender, "/xg7lobby open <id>");
            return;
        }

        Player player = (Player) sender;

        BaseMenu menu = XG7Lobby.getInstance().getInventoryManager().getInventory(args.get(0, String.class));

        if (menu == null) {
            Text.formatLang(XG7Lobby.getInstance(), player, "menu-does-not-exist").thenAccept(text -> text.send(player));
            return;
        }

        menu.open(player);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        if (args.len() == 1) return new ArrayList<>(XG7Lobby.getInstance().getInventoryManager().getInventoriesMap().keySet());
        return Collections.emptyList();
    }
}
