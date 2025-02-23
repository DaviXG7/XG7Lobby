package com.xg7plugins.xg7lobby.commands.moderation;


import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.XG7Menus;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.inventories.warn_menu.WarnMenu;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "warns",
        permission = "xg7lobby.commands.warns",
        description = "View a player's warnings",
        syntax = "/warns %player%",
        isPlayerOnly = true
)
public class WarnsCommand implements ICommand {
    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        Player player = (Player) sender;

        Player target = player;

        if (args.len() == 1) {
            if (!sender.hasPermission("xg7lobby.commands.warns.others")) {
                Text.fromLang(sender, XG7Plugins.getInstance(), "commands.no-permission").thenAccept(text -> text.send(sender));
                return;
            }

            OfflinePlayer targetOffline = args.get(0, OfflinePlayer.class);
            if (!targetOffline.isOnline()) {
                Text.fromLang(sender, XG7Lobby.getInstance(), "commands.not-online").thenAccept(text -> text.send(sender));
                return;
            }

            target = targetOffline.getPlayer();
        }

        WarnMenu menu = XG7Menus.getInstance().getMenu(XG7Lobby.getInstance(), "warn-menu");

        menu.open(player, target);

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return args.len() == 1 ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.GOLDEN_AXE, this);
    }
}
