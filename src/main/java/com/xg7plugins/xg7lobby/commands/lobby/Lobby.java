package com.xg7plugins.xg7lobby.commands.lobby;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.commands.setup.ISubCommand;
import com.xg7plugins.commands.setup.SubCommand;
import com.xg7plugins.commands.setup.SubCommandType;
import com.xg7plugins.libs.xg7menus.XSeries.XMaterial;
import com.xg7plugins.libs.xg7menus.builders.item.ItemBuilder;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.model.LobbyLocation;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@com.xg7plugins.commands.setup.Command(
        name = "xg7lobbylobby",
        aliasesPath = "lobby",
        syntax = "lobby",
        description = "Teleport to the lobby",
        isOnlyPlayer = true
)
public class Lobby implements ICommand {
    @Override
    public ISubCommand[] getSubCommands() {
        return new ISubCommand[]{new LobbyOther()};
    }

    @Override
    public void onCommand(Command command, Player player, String label) {
        try {
            LobbyLocation location = XG7Plugins.getInstance().getJsonManager().load(XG7Lobby.getInstance(), "lobby.json", LobbyLocation.class);
            if (location.getLocation() == null) {
                Text.format("lang:[lobby.on-teleport." + (player.hasPermission("xg7lobby.command.setlobby") ? "on-error-doesnt-exist-adm" : "on-error-doesnt-exist") + "]", XG7Lobby.getInstance()).send(player);

                return;
            }
            Bukkit.getScheduler().runTask(XG7Lobby.getInstance(), () -> player.teleport(location.getLocation().getBukkitLocation()));
            Text.format("lang:[lobby.on-teleport.on-success]", XG7Lobby.getInstance()).send(player);
        } catch (IOException e) {
            Text.format("lang:[lobby.on-teleport.on-error]", XG7Lobby.getInstance()).send(player);
            throw new RuntimeException(e);
        }

    }

    @SubCommand(
            description = "Teleport other to lobby",
            perm = "xg7lobby.command.lobby.other",
            type = SubCommandType.PLAYER,
            syntax = "lobby [PLAYER]"
    )
    static class LobbyOther implements ISubCommand {

        @Override
        public void onSubCommand(CommandSender sender, OfflinePlayer target, String label) {

            if (!target.isOnline()) {
                Text.format("lang:[commands.not-online]", XG7Lobby.getInstance()).send(sender);
                return;
            }

            try {
                LobbyLocation location = XG7Plugins.getInstance().getJsonManager().load(XG7Lobby.getInstance(), "lobby.json", LobbyLocation.class);

                if (location.getLocation() == null) {
                    Text.format("lang:[lobby.on-teleport." + (sender.hasPermission("xg7lobby.command.setlobby") ? "on-error-doesnt-exist-adm" : "on-error-doesnt-exist") + "]", XG7Lobby.getInstance())
                            .send(sender);
                    return;
                }

                XG7Plugins.getInstance().getTaskManager().runTaskSync(XG7Lobby.getInstance(), () -> target.getPlayer().teleport(location.getLocation().getBukkitLocation()));
                Text.format("lang:[lobby.on-teleport.on-success]", XG7Lobby.getInstance())
                        .send(target.getPlayer());
                Text.format("lang:[lobby.on-teleport.on-success-other]", XG7Lobby.getInstance())
                        .replace("[PLAYER]", target.getName())
                        .send(sender);
            } catch (IOException e) {
                Text.format("lang:[lobby.on-teleport.on-error]", XG7Lobby.getInstance())
                        .replace("[PLAYER]", target.getName())
                        .send(sender);
                throw new RuntimeException(e);
            }
        }


        @Override
        public ItemBuilder getIcon() {
            return ItemBuilder.subCommandIcon(XMaterial.BLAZE_ROD, this, XG7Lobby.getInstance());
        }
    }

    @Override
    public List<String> onTabComplete(Command command, CommandSender sender, String label, String[] args) {
        if (args.length == 1 && sender.hasPermission("xg7lobby.command.lobby.other")) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        }
        return ICommand.super.onTabComplete(command, sender, label, args);
    }

    @Override
    public ItemBuilder getIcon() {
        return ItemBuilder.commandIcon(XMaterial.ENDER_PEARL, this, XG7Lobby.getInstance());
    }
}
