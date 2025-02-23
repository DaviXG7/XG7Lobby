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
        name = "unbanip",
        description = "Unban a player by ip",
        syntax = "/unban <ip>",
        permission = "xg7lobby.command.moderation.unban"
)
public class UnbanIPCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() != 1) {
            syntaxError(sender, "unban <ip>");
            return;
        }

        String ip = args.get(0, String.class);

        XG7Plugins.getInstance().getServer().getBanList(org.bukkit.BanList.Type.IP).pardon(ip);

        Text.fromLang(sender, XG7Lobby.getInstance(), "commands.unban.on-unban-ip").thenAccept(text -> text.replace("ip", ip).send(sender));

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return args.len() == 1 ? Bukkit.getBanList(org.bukkit.BanList.Type.IP).getBanEntries().stream().map(banEntry -> banEntry.getTarget()).collect(Collectors.toList()) : new ArrayList<>();
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.EMERALD, this);
    }
}
