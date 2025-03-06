package com.xg7plugins.xg7lobby.commands.toggleCommands;

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
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@Command(
        name = "fly",
        permission = "xg7lobby.command.fly",
        syntax = "fly (player)",
        description = "Toggle fly mode",
        isInEnabledWorldOnly = true
)
public class FlyCommand implements ICommand {

    @Override
    public Plugin getPlugin() {
        return XG7Lobby.getInstance();
    }

    @Override
    public void onCommand(CommandSender sender, CommandArgs args) {

        OfflinePlayer target = null;
        boolean isOther = false;
        if (args.len() == 0) {
            if (!(sender instanceof Player)) {
                Text.fromLang(sender, XG7Plugins.getInstance(), "commands.not-a-player").thenAccept(text -> text.send(sender));
                return;
            }
            target = (Player) sender;
        }

        if (args.len() > 0) {
            if (!sender.hasPermission("xg7lobby.command.fly-other")) {
                Text.fromLang(sender, XG7Plugins.getInstance(), "commands.no-permission").thenAccept(text -> text.send(sender));
                return;
            }
            target = args.get(0, OfflinePlayer.class);
            isOther = true;
        }
        if (isOther) {
            if (target == null || (!target.hasPlayedBefore()) && !target.isOnline()) {
                Text.fromLang(sender, XG7Plugins.getInstance(), "commands.player-not-found").thenAccept(text -> text.send(sender));
                return;
            }
        }

        boolean finalIsOther = isOther;



        OfflinePlayer finalTarget = target;
        LobbyPlayer.cast(target.getUniqueId(), true).thenAccept(lobbyPlayer -> {
            boolean isFlying = lobbyPlayer.isFlying();
            lobbyPlayer.setFlying(!lobbyPlayer.isFlying());
            lobbyPlayer.update().exceptionally(throwable -> {
                throwable.printStackTrace();
                lobbyPlayer.setFlying(isFlying);
                lobbyPlayer.update();
                return null;
            });
            if (finalTarget.isOnline()) {
                XG7Plugins.taskManager().runSyncTask(XG7Lobby.getInstance(), lobbyPlayer::fly);
                Text.fromLang(lobbyPlayer.getPlayer(),XG7Lobby.getInstance(), "commands.fly." + (lobbyPlayer.isFlying() ? "toggle-on" : "toggle-off")).thenAccept(text -> text.send(lobbyPlayer.getPlayer()));
            }
            if (finalIsOther) Text.fromLang(sender, XG7Lobby.getInstance(), "commands.fly." + (lobbyPlayer.isFlying() ? "toggle-other-on" : "toggle-other-off")).thenAccept(text -> text.replace("target", lobbyPlayer.getPlayer().getDisplayName()).send(sender));
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
        return Item.commandIcon(XMaterial.FEATHER, this);
    }

}
