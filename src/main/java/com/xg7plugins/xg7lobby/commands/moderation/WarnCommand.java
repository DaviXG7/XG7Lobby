package com.xg7plugins.xg7lobby.commands.moderation;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import com.xg7plugins.xg7lobby.lobby.player.Warn;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Command(
        name = "warn",
        description = "Warn a player",
        syntax = "/warn <player> <level> <reason>",
        permission = "xg7lobby.command.moderation.warn",
        isAsync = true
)
public class WarnCommand implements ICommand {
    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.BOOK, this);
    }


    public void onCommand(CommandSender sender, CommandArgs args) {

        if (args.len() != 3) {
            syntaxError(sender, "warn <player> <level> <reason>");
            return;
        }

        OfflinePlayer target = args.get(0, OfflinePlayer.class);
        int level = args.get(1, int.class);
        String reason = args.get(2, String.class);

        Config config = XG7Lobby.getInstance().getConfig("config");

        if (target == null || !target.hasPlayedBefore()) {
            Text.formatLang(XG7Plugins.getInstance(), sender, "commands.player-not-found").thenAccept(text -> text.send(sender));
            return;
        }

        if (level < 1) {
            Text.formatLang(XG7Lobby.getInstance(), sender, "warn-level-invalid").thenAccept(text -> text.send(sender));
            return;
        }

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(target.getUniqueId(), false).join();

        if (!config.get("warn-admin",Boolean.class).orElse(false) && !target.isOp()) {
            Text.formatLang(XG7Lobby.getInstance(), sender, "commands.no-permission").thenAccept(text -> text.send(sender));
            return;
        }

        lobbyPlayer.addInfraction(new Warn(lobbyPlayer.getPlayerUUID(), level, reason));

        Text.formatLang(XG7Lobby.getInstance(), sender, "commands.warn.on-warn-sender").thenAccept(text -> text.replace("[REASON]", reason).send(sender));
        if (target.isOnline()) Text.formatLang(XG7Lobby.getInstance(), lobbyPlayer.getPlayer(), "commands.warn.on-warn").thenAccept(text -> text.replace("[PLAYER]", target.getName()).replace("[REASON]", reason).send(target.getPlayer()));


    }

    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {

        switch (args.len()) {
            case 1:
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            case 2:
                return XG7Lobby.getInstance().getConfig("config").getList("warn-levels", Map.class).orElse(new ArrayList<>()).stream().map(map -> map.get("level").toString()).collect(Collectors.toList());
            case 3:
                return Collections.singletonList("Reason");
        }

        return Collections.emptyList();
    }
}
