package com.xg7plugins.xg7lobby.commands;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
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
    public void onCommand(CommandSender sender, CommandArgs args) {
        if (args.len() < 1) {
            syntaxError(sender, "/xg7lobby gamemode <gamemode> (player)");
            return;
        }

        OfflinePlayer target = null;

        if (args.len() == 1) {
            if (!(sender instanceof Player)) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.not-a-player").thenAccept(text -> text.send(sender));
                return;
            }
            target = (Player) sender;
        }

        Mode mode = Mode.getMode(args.get(0, String.class));
        boolean isOther = false;

        if (mode == null) {
            Text.formatLang(XG7Plugins.getInstance(), sender, "commands.gamemode.invalid-gamemode").thenAccept(text -> text.send(sender));
            return;
        }

        if (args.len() > 1) {
            if (!sender.hasPermission("xg7lobby.command.gamemode.other")) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.no-permission").thenAccept(text -> text.send(sender));
                return;
            }
            target = args.get(1, OfflinePlayer.class);
            isOther = true;
        }

        if (isOther) {

            if (target == null || !target.hasPlayedBefore()) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.player-not-found").thenAccept(text -> text.send(sender));
                return;
            }

            if (!target.isOnline()) {
                Text.formatLang(XG7Lobby.getInstance(), sender, "commands.not-online").thenAccept(text -> text.send(sender));
                return;
            }
        }

        target.getPlayer().setGameMode(mode.getGameMode());

        OfflinePlayer finalTarget = target;
        Text.formatLang(XG7Lobby.getInstance(), target.getPlayer(), "commands.gamemode.set")
                .thenAccept(text -> text.replace("[GAMEMODE]", mode.name().toLowerCase()).send(finalTarget.getPlayer()));
        if (isOther) Text.formatLang(XG7Lobby.getInstance(), sender, "commands.gamemode.set-other")
                .thenAccept(text -> text.replace("[GAMEMODE]", mode.name().toLowerCase()).replace("[PLAYER]", finalTarget.getName()).send(sender));
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
                if (mode.name().equalsIgnoreCase(name.toUpperCase())) return mode;
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
