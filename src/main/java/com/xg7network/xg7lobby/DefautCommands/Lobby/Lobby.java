package com.xg7network.xg7lobby.DefautCommands.Lobby;

import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.Message;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Lobby implements CommandExecutor {

    private XG7Lobby pl;

    public Lobby(XG7Lobby pl) {
        this.pl = pl;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            LobbyLocation location = new LobbyLocation(pl);

            if (location.getLocation() == null) {
                if (PluginUtil.hasPermission(commandSender, PermissionType.SETLOBBY_COMMAND, configManager.getConfig(ConfigType.MESSAGES).getString("commands.lobby-warn")))
                    new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.adm-lobby-warn"), player).sendMessage();
                return true;
            }

            player.teleport(new LobbyLocation(pl).getLocation());
        } else {
            commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());
        }
        return true;
    }
}
