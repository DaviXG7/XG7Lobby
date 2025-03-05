package com.xg7plugins.xg7lobby.commands.moderation.mute;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "unmute",
        description = "Unmute a player",
        syntax = "/unmute <player>",
        permission = "xg7lobby.command.moderation.unmute",
        isAsync = true
)
public class UnmuteCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        if (args.len() != 1) {
            syntaxError(sender, "unmute <player>");
            return;
        }

        OfflinePlayer target = args.get(0, OfflinePlayer.class);

        if (target == null || (!target.hasPlayedBefore()) && !target.isOnline()) {
            Text.fromLang(sender,XG7Plugins.getInstance(), "commands.player-not-found").thenAccept(text -> text.send(sender));
            return;
        }

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(target.getUniqueId(), false).join();

        if (!lobbyPlayer.isMuted()) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "commands.unmute.not-muted").thenAccept(text -> text.send(sender));
            return;
        }

        lobbyPlayer.setMuted(false);
        lobbyPlayer.setTimeForUnmute(0);
        lobbyPlayer.update().join();

        if (target.isOnline()) {
            Text.fromLang(target.getPlayer(),XG7Lobby.getInstance(), "commands.unmute.on-unmute-sender").thenAccept(text -> text.send(target.getPlayer()));
        }

        Text.fromLang(sender, XG7Lobby.getInstance(), "commands.unmute.on-unmute-sender").thenAccept(text -> text.replace("target", target.getName()).send(sender));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return args.len() == 1 ? Bukkit.getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.PAPER, this);
    }
}
