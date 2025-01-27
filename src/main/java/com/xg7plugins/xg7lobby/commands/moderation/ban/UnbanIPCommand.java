package com.xg7plugins.xg7lobby.commands.moderation.ban;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(
        name = "unbanip",
        description = "Unban a player by ip",
        syntax = "/unban <ip>",
        permission = "xg7lobby.command.moderation.unban"
)
public class UnbanIPCommand implements ICommand {
    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() != 1) {
            syntaxError(sender, "unban <ip>");
            return;
        }

        String ip = args.get(0, String.class);

        if (!XG7Plugins.getInstance().getServer().getBanList(org.bukkit.BanList.Type.IP).isBanned(ip)) {
            Text.formatLang(XG7Plugins.getInstance(), sender, "commands.unban.not-banned").thenAccept(text -> text.send(sender));
            return;
        }

        XG7Plugins.getInstance().getServer().getBanList(org.bukkit.BanList.Type.IP).pardon(ip);

        Text.formatLang(XG7Plugins.getInstance(), sender, "commands.unban.on-unban-ip").thenAccept(text -> text.replace("[IP]", ip).send(sender));

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return ICommand.super.onTabComplete(sender, args);
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.EMERALD, this);
    }
}
