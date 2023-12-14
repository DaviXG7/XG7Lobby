package com.xg7network.xg7lobby.DefautCommands.Moderation;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Warn implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (!PluginUtil.hasPermission(commandSender, PermissionType.BAN_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
            return true;
        if (strings.length <= 1) {
            commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bwarn §2§i<Player> <Reason>");
            return true;
        }


        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);


        String str = "";

        for (int i = 1; i < strings.length; i++) str += strings[i] + " ";


        PlayerData playerData = PlayersManager.getData(target.getUniqueId().toString());
        playerData.addInfraction(str.toString().trim().replace("&", "§"), new Date());

        PlayersManager.update(target.getUniqueId().toString(), playerData);

        Infractions.verify(target.getPlayer(), playerData.getInfractions().size());





        return true;
    }
}
