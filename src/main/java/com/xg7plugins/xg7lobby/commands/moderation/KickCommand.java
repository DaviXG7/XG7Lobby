package com.xg7plugins.xg7lobby.commands.moderation;

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
import com.xg7plugins.xg7lobby.lobby.player.Warn;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "kick",
        description = "Kick a player",
        syntax = "/kick <player> %reason%",
        permission = "xg7lobby.command.moderation.kick"
)
public class KickCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        OfflinePlayer target = args.get(0, OfflinePlayer.class);
        String reason = args.len() > 1 ? Strings.join(Arrays.asList(Arrays.copyOfRange(args.getArgs(), 1, args.len())), ' ') : null;

        if (target == null || (!target.hasPlayedBefore()) && !target.isOnline()) {
            Text.fromLang(sender, XG7Plugins.getInstance(), "commands.player-not-found").thenAccept(text -> text.send(sender));
            return;
        }

        if (!target.isOnline()) {
            Text.fromLang(sender, XG7Plugins.getInstance(), "commands.not-online").thenAccept(text -> text.send(sender));
            return;
        }

        if (target.isOp() && !XG7Lobby.getInstance().getConfig("config").get("kick-admin",Boolean.class).orElse(false)) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "commands.kick.kick-admin").thenAccept(text -> text.send(sender));
            return;
        }

        target.getPlayer().kickPlayer(Text.fromLang(target.getPlayer(), XG7Lobby.getInstance(), "commands.kick.on-kick").join().replace("reason", reason != null ? reason : "").getText());

        if (reason != null) {
            LobbyPlayer.cast(target.getUniqueId(), true).thenAccept(lobbyPlayer -> {
                lobbyPlayer.getInfractions().add(new Warn(target.getUniqueId(), XG7Lobby.getInstance().getConfig("config").get("kick-warn-level", Integer.class).orElse(0),  reason != null ? reason : ""));
            });
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
        return Item.commandIcon(XMaterial.GOLDEN_BOOTS, this);
    }
}
