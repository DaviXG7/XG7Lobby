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
        name = "unban",
        description = "Unban a player",
        syntax = "/unban <player>",
        permission = "xg7lobby.command.moderation.unban"
)
public class UnbanCommand implements ICommand {
    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() != 1) {
            syntaxError(sender, "unban <player>");
            return;
        }

        OfflinePlayer target = args.get(0, OfflinePlayer.class);

        if (target == null || !target.hasPlayedBefore()) {
            Text.formatLang(XG7Plugins.getInstance(), sender, "commands.player-not-found").thenAccept(text -> text.send(sender));
            return;
        }

        if (!target.isBanned()) {
            Text.formatLang(XG7Plugins.getInstance(), sender, "commands.unban.not-banned").thenAccept(text -> text.send(sender));
            return;
        }

        XG7Plugins.getInstance().getServer().getBanList(org.bukkit.BanList.Type.NAME).pardon(target.getName());

        Text.formatLang(XG7Plugins.getInstance(), sender, "commands.unban.on-unban").thenAccept(text -> text.replace("[PLAYER]", target.getName()).send(sender));

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
