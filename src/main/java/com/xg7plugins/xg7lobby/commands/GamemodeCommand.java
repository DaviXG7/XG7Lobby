package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.modules.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "gamemode",
        permission = "xg7lobby.command.gamemode",
        syntax = "gamemode <gamemode> (player)",
        description = "Change player's gamemode"
)
public class GamemodeCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        if (args.len() < 1) {
            syntaxError(sender, "/xg7lobby gamemode <gamemode> (player)");
            return;
        }

        OfflinePlayer target = null;

        if (args.len() == 1) {
            if (!(sender instanceof Player)) {
                Text.fromLang(sender,XG7Plugins.getInstance(), "commands.not-a-player").thenAccept(text -> text.send(sender));
                return;
            }
            target = (Player) sender;
        }

        Mode mode = Mode.getMode(args.get(0, String.class));
        boolean isOther = false;

        if (mode == null) {
            Text.fromLang(sender, XG7Lobby.getInstance(), "commands.gamemode.invalid-gamemode").thenAccept(text -> text.send(sender));
            return;
        }

        if (args.len() > 1) {
            if (!sender.hasPermission("xg7lobby.command.gamemode.other")) {
                Text.fromLang(sender, XG7Plugins.getInstance(), "commands.no-permission").thenAccept(text -> text.send(sender));
                return;
            }
            target = args.get(1, OfflinePlayer.class);
            isOther = true;
        }

        if (isOther) {

            if (target == null || (!target.hasPlayedBefore()) && !target.isOnline()) {
                Text.fromLang(sender, XG7Plugins.getInstance(), "commands.player-not-found").thenAccept(text -> text.send(sender));
                return;
            }

            if (!target.isOnline()) {
                Text.fromLang(sender, XG7Lobby.getInstance(), "commands.not-online").thenAccept(text -> text.send(sender));
                return;
            }
        }

        target.getPlayer().setGameMode(mode.getGameMode());

        if (mode == Mode.SURVIVAL || mode == Mode.ADVENTURE) LobbyPlayer.cast(target.getUniqueId(), false).thenAccept(LobbyPlayer::fly);

        OfflinePlayer finalTarget = target;
        Text.fromLang(target.getPlayer(),XG7Lobby.getInstance(), "commands.gamemode.set")
                .thenAccept(text -> text.replace("gamemode", mode.name().toLowerCase()).send(finalTarget.getPlayer()));
        if (isOther) Text.fromLang(sender, XG7Lobby.getInstance(), "commands.gamemode.set-other")
                .thenAccept(text -> text.replace("gamemode", mode.name().toLowerCase()).replace("player", finalTarget.getName()).send(sender));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        switch (args.len()) {
            case 1:
                return Arrays.asList("s", "c", "a", "sp");
            case 2:
                if (!sender.hasPermission("xg7lobby.command.gamemode.other")) return Collections.emptyList();
                return XG7Plugins.getInstance().getServer().getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public Item getIcon() {
        return null;
    }

    public enum Mode {
        SURVIVAL("s", "0"),
        CREATIVE("c", "1"),
        ADVENTURE("a", "2"),
        SPECTATOR("sp", "3");

        private final String[] aliases;

        Mode(String... aliases) {
            this.aliases = aliases;
        }

        public static Mode getMode(String name) {
            for (Mode mode : values()) {
                if (mode.name().equalsIgnoreCase(name)) return mode;
                for (String alias : mode.aliases) {
                    if (alias.equalsIgnoreCase(name)) return mode;
                }
            }
            return null;
        }

        public GameMode getGameMode() {
            switch (this) {
                case SURVIVAL:
                    return GameMode.SURVIVAL;
                case CREATIVE:
                    return GameMode.CREATIVE;
                case ADVENTURE:
                    return GameMode.ADVENTURE;
                case SPECTATOR:
                    return GameMode.SPECTATOR;
                default:
                    return null;
            }
        }
    }
}
