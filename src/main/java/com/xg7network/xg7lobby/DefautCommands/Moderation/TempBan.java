package com.xg7network.xg7lobby.DefautCommands.Moderation;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class TempBan implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!PluginUtil.hasPermission(commandSender, PermissionType.BAN_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
            return true;
        if (strings.length == 0) {
            commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§btempban §2§i<Player> <Time> [Reason]");
            return true;
        } else {

            OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);


            if (target == null) {
                commandSender.sendMessage(ErrorMessages.PLAYER_DOESNOT_EXIST.getMessage());
                return true;
            }


            if (target.getPlayer().isOnline()) {
                if (target.getPlayer().hasPermission(PermissionType.ADMIN.getPerm())) {
                    commandSender.sendMessage(prefix + "§cYou cannot ban a player with admin perms.");
                    return true;
                }
            }

            if (strings.length < 2) {
                commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§btempmute §2§i<Player> <Date ->  dd/mm/yyyy h:m>");

                String[] date = strings[1].split("/");
                String[] time = strings[2].split(":");

                Calendar calendar = Calendar.getInstance();

                calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

                Date dateToUnban = calendar.getTime();

                target.getPlayer().kickPlayer("");
                Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), "", dateToUnban, null);
                commandSender.sendMessage(prefix + ChatColor.GREEN + "You successfully banned " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN +  " for " + TimeUnit.MILLISECONDS.toDays((dateToUnban.getTime() - new Date().getTime())) + " days, " + TimeUnit.MILLISECONDS.toHours((dateToUnban.getTime() - new Date().getTime())) % 24 + " hours and " + TimeUnit.MILLISECONDS.toMinutes((dateToUnban.getTime() - new Date().getTime())) % 60 + " minutes!");

                return true;
            }

        }

        return true;
    }
}
