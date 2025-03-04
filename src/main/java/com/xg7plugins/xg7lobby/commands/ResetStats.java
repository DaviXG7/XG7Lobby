package com.xg7plugins.xg7lobby.commands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "resetstats",
        permission = "xg7lobby.command.resetstats",
        syntax = "xg7lobby resetstats <player> (kills|deaths)",
        description = "Resets the stats of a player",
        isAsync = true
)
public class ResetStats implements ICommand {
    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        if (args.len() < 1) {
            syntaxError(sender, "xg7lobby resetstats <player> (kills|deaths)");
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args.get(0, String.class));

        if (!player.hasPlayedBefore() && !player.isOnline()) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "player-not-found").thenAccept(text -> text.replace("target", args.get(0, String.class)).send(sender));
            return;
        }

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(player.getUniqueId(), false).join();

        if (args.len() != 2) {
            lobbyPlayer.setGlobalPVPKills(0);
            lobbyPlayer.setGlobalPVPDeaths(0);

            lobbyPlayer.update().join();

            Text.fromLang(sender, XG7Lobby.getInstance(), "commands.reset-stats.on-reset").thenAccept(text -> text.replace("target", player.getName()).send(sender));
            return;
        }

        String type = args.get(1, String.class);

        switch (type.toLowerCase()) {
            case "kills":
                lobbyPlayer.setGlobalPVPKills(0);
                lobbyPlayer.update().join();

                Text.fromLang(sender, XG7Lobby.getInstance(), "commands.reset-stats.kills-reset").thenAccept(text -> text.replace("target", player.getName()).send(sender));

                break;
            case "deaths":
                lobbyPlayer.setGlobalPVPDeaths(0);
                lobbyPlayer.update().join();

                Text.fromLang(sender, XG7Lobby.getInstance(), "commands.reset-stats.deaths-reset").thenAccept(text -> text.replace("target", player.getName()).send(sender));

                break;
            default:
                syntaxError(sender, "xg7lobby resetstats <player> (kills|deaths)");
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {

        if (args.len() == 1) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        }

        if (args.len() == 2) {
            return Arrays.asList("kills", "deaths");
        }

        return Collections.emptyList();
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.LAVA_BUCKET, this);
    }
}
