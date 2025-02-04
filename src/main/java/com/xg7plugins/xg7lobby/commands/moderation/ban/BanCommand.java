package com.xg7plugins.xg7lobby.commands.moderation.ban;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import com.xg7plugins.xg7lobby.lobby.player.Warn;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "ban",
        description = "Ban a player",
        syntax = "/ban <player> <time> [reason]",
        permission = "xg7lobby.command.moderation.ban"
)
public class BanCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        if (args.len() < 2) {
            syntaxError(sender, "ban <player> <time> [reason]");
            return;
        }

        OfflinePlayer target = args.get(0, OfflinePlayer.class);
        long time = args.get(1, String.class).equals("forever") ? 0 : Text.convertToMilliseconds(XG7Lobby.getInstance(), args.get(1, String.class));
        String reason = args.len() > 2 ? Strings.join(Arrays.asList(Arrays.copyOfRange(args.getArgs(), 2, args.len())), ' ') : null;

        if (target == null || (!target.hasPlayedBefore()) && !target.isOnline()) {
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

        Date date = time == 0 ? null : new Date(System.currentTimeMillis() + time);

        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, date, sender.getName());

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
        if (target.isOnline()) {
            target.getPlayer().kickPlayer(Text.formatLang(XG7Lobby.getInstance(), lobbyPlayer.getPlayer(), "commands.ban.on-ban").join().replace("[REASON]", reason).replace("[TIME]", String.format("%dd, %02dh %02dm %02ds", days, finalHours, finalMinutes, finalSeconds)).getText());
        }

        Text.formatLang(XG7Lobby.getInstance(), sender, "commands.ban.on-ban-sender").thenAccept(text -> text.replace("[PLAYER]", target.getName()).replace("[REASON]", reason).replace("[TIME]", String.format("%dd, %02dh %02dm %02ds", days, finalHours, finalMinutes, finalSeconds)).send(sender));

        if (reason != null) {
            lobbyPlayer.addInfraction(new Warn(lobbyPlayer.getPlayerUUID(), XG7Lobby.getInstance().getConfig("config").get("ban-warn-level", Integer.class).orElse(0), reason));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        switch (args.len()) {
            case 1:
                return Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
            case 2:
                return Collections.singletonList("reason");
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.matchXMaterial("BARRIER").orElse(XMaterial.OAK_DOOR), this);
    }
}
