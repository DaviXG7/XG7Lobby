package com.xg7plugins.xg7lobby.commands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.Command;
import com.xg7plugins.commands.setup.CommandArgs;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.libs.xg7menus.item.Item;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "build",
        permission = "xg7lobby.command.build",
        syntax = "build (player)",
        description = "Toggle build mode",
        isInEnabledWorldOnly = true
)
public class BuildCommand implements ICommand {
    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {
        OfflinePlayer target = null;
        boolean isOther = false;
        if (args.len() == 0) {
            if (!(sender instanceof Player)) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.not-a-player").thenAccept(text -> text.send(sender));
                return;
            }
            target = (Player) sender;
        }

        if (args.len() > 0) {
            if (!sender.hasPermission("xg7lobby.command.build.other")) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.no-permission").thenAccept(text -> text.send(sender));
                return;
            }
            target = args.get(0, OfflinePlayer.class);
            isOther = true;
        }
        if (isOther) {
            if (target == null || !target.hasPlayedBefore()) {
                Text.formatLang(XG7Plugins.getInstance(), sender, "commands.player-not-found").thenAccept(text -> text.send(sender));
                return;
            }
        }

        boolean finalIsOther = isOther;

        OfflinePlayer finalTarget = target;
        LobbyPlayer.cast(target.getUniqueId(), true).thenAccept(lobbyPlayer -> {
            lobbyPlayer.setBuildEnabled(!lobbyPlayer.isBuildEnabled());
            if (finalTarget.isOnline()) {
                Text.formatLang(XG7Lobby.getInstance(), lobbyPlayer.getPlayer(), "commands.build." + (lobbyPlayer.isBuildEnabled() ? "toggle-on" : "toggle-off")).thenAccept(text -> text.send(lobbyPlayer.getPlayer()));
            }
            XG7Lobby.getInstance().getPlayerDAO().update(lobbyPlayer).join();
            if (finalIsOther) Text.formatLang(XG7Lobby.getInstance(), sender, "commands.build." + (lobbyPlayer.isBuildEnabled() ? "toggle-other-on" : "toggle-other-off")).thenAccept(text -> text.replace("[PLAYER]", lobbyPlayer.getPlayer().getDisplayName()).send(sender));
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, CommandArgs args) {
        return Bukkit.getOnlinePlayers().stream().filter(player -> XG7Lobby.getInstance().isInWorldEnabled(player)).map(Player::getName).collect(Collectors.toList());
    }

    @Override
    public Item getIcon() {
        return Item.commandIcon(XMaterial.IRON_AXE, this);
    }
}
