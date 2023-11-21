package com.xg7network.xg7lobby.DefautCommands.Moderation;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class Kick implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!PluginUtil.hasPermission(commandSender, PermissionType.KICK_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
            return true;
        if (strings.length == 0) {
            commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bkick §2§i<Player> [Reason]");
            return true;
        } else {

            Player target = Bukkit.getPlayer(strings[0]);

            if (!target.isOnline()) {
                commandSender.sendMessage(ErrorMessages.PLAYER_IS_NOT_ONLINE.getMessage());
                return true;
            }

            if (target == null) {
                commandSender.sendMessage(ErrorMessages.PLAYER_DOESNOT_EXIST.getMessage());
                return true;
            }


            if (strings.length == 1) {


                target.kickPlayer("");
                commandSender.sendMessage(ChatColor.GREEN + "You kicked out " + ChatColor.BLUE + target.getName());

            } else if (strings.length == 2) {

                String str = "";

                for (int i = 0; i < strings.length; i++) str += strings[i] + " ";


                target.kickPlayer(str.toString().trim().replace("&", "§"));
                commandSender.sendMessage(prefix + ChatColor.GREEN + "You kicked out " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " for" + ChatColor.RESET + str.trim().replace("&", "§"));

            }
        }
        return true;
    }


}
