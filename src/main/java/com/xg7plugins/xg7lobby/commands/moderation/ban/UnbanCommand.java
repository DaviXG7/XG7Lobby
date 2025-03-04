package com.xg7plugins.xg7lobby.commands.moderation.ban;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "unban",
        description = "Unban a player",
        syntax = "/unban <player>",
        permission = "xg7lobby.command.moderation.unban"
)
public class UnbanCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() != 1) {
            syntaxError(sender, "unban <player>");
            return;
        }

        OfflinePlayer target = args.get(0, OfflinePlayer.class);

        if (target == null || !target.hasPlayedBefore()) {
            Text.fromLang(sender,XG7Plugins.getInstance(), "commands.player-not-found").thenAccept(text -> text.send(sender));
            return;
        }

        if (!target.isBanned()) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "commands.unban.not-banned").thenAccept(text -> text.send(sender));
            return;
        }

        XG7Plugins.getInstance().getServer().getBanList(org.bukkit.BanList.Type.NAME).pardon(target.getName());

        Text.fromLang(sender, XG7Lobby.getInstance(), "commands.unban.on-unban").thenAccept(text -> text.replace("target", target.getName()).send(sender));

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return args.len() == 1 ? Bukkit.getBanList(org.bukkit.BanList.Type.NAME).getBanEntries().stream().map(banEntry -> banEntry.getTarget()).collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.EMERALD, this);
    }
}
