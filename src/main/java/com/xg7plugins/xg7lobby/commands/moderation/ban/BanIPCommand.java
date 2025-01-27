package com.xg7plugins.xg7lobby.commands.moderation.ban;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import com.xg7plugins.xg7lobby.lobby.player.Warn;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.List;

@Command(
        name = "banip",
        description = "Ban a player for ip",
        syntax = "/banip <player> <time> [reason]",
        permission = "xg7lobby.command.moderation.ban"
)
public class BanIPCommand implements ICommand {

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        if (args.len() != 2) {
            syntaxError(sender, "banip <player> <level> [reason]");
            return;
        }

        OfflinePlayer target = args.get(0, OfflinePlayer.class);
        long time = args.get(1, String.class).equals("forever") ? 0 : Text.convertToMilliseconds(XG7Lobby.getInstance(), args.get(1, String.class));
        String reason = args.len() > 2 ? args.get(2, String.class) : null;

        if (target == null || !target.hasPlayedBefore()) {
            Text.formatLang(XG7Plugins.getInstance(), sender, "commands.player-not-found").thenAccept(text -> text.send(sender));
            return;
        }

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(target.getUniqueId(), false).join();

        if (target.isBanned()) {
            Text.formatLang(XG7Lobby.getInstance(), sender, "commands.ban.already-banned").thenAccept(text -> text.send(sender));
            return;
        }

        if (target.isOp() && !XG7Lobby.getInstance().getConfig("config").get("ban-admin",Boolean.class).orElse(false)) {
            Text.formatLang(XG7Lobby.getInstance(), sender, "commands.ban.ban-admin").thenAccept(text -> text.send(sender));
            return;
        }

        if (!target.isOnline()) {
            Text.formatLang(XG7Lobby.getInstance(), sender, "commands.not-online").thenAccept(text -> text.send(sender));
            return;
        }

        Date date = time == 0 ? null : new Date(System.currentTimeMillis() + time);

        Bukkit.getBanList(BanList.Type.IP).addBan(target.getPlayer().getAddress().getAddress().getHostAddress(), reason, date, sender.getName());

        long seconds = time / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        long finalHours = hours;
        long finalMinutes = minutes;
        long finalSeconds = seconds;

        target.getPlayer().kickPlayer(Text.formatLang(XG7Lobby.getInstance(), lobbyPlayer.getPlayer(), "commands.ban.on-ban").join().replace("[REASON]", reason).replace("[TIME]", String.format("%dd, %02dh %02dm %02ds", days, finalHours, finalMinutes, finalSeconds)).getText());


        Text.formatLang(XG7Lobby.getInstance(), sender, "commands.ban.on-ban-sender").thenAccept(text -> text.replace("[PLAYER]", target.getName()).replace("[REASON]", reason).replace("[TIME]", String.format("%dd, %02dh %02dm %02ds", days, finalHours, finalMinutes, finalSeconds)).send(sender));

        if (reason != null) {
            lobbyPlayer.addInfraction(new Warn(lobbyPlayer.getPlayerUUID(), XG7Lobby.getInstance().getConfig("config").get("ban-warn-level", Integer.class).orElse(0), reason));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return ICommand.super.onTabComplete(sender, args);
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.matchXMaterial("BARRIER").orElse(XMaterial.OAK_DOOR), this);
    }

}
