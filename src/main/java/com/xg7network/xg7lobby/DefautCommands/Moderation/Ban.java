package com.xg7network.xg7lobby.DefautCommands.Moderation;

import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class Ban implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!PluginUtil.hasPermission(commandSender, PermissionType.BAN_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
            return true;
        if (strings.length == 0) {
            commandSender.sendMessage(command.getName().equalsIgnoreCase("xg7lobbyban") ?
                    ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bban §2§i<Player> [Reason]" :
                    ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bunban §2§i<Player>");
            return true;
        }

        if (command.getName().equalsIgnoreCase("xg7lobbyban")) {

            OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);


            if (target == null) {
                commandSender.sendMessage(ErrorMessages.PLAYER_DOESNOT_EXIST.getMessage());
                return true;
            }

            if (target.getPlayer().hasPermission(PermissionType.ADMIN.getPerm())) {
                commandSender.sendMessage(prefix + "§cYou cannot ban a player with admin perms.");
                return true;
            }

            if (strings.length == 1) {

                if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(target.getName())) {

                    target.getPlayer().kickPlayer("");
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), "", null, null);
                    commandSender.sendMessage(prefix + ChatColor.GREEN + "You successfully banned " + ChatColor.YELLOW + target.getName());

                } else
                    commandSender.sendMessage(ChatColor.RED + "This player is already in the banlist!");

            } else if (strings.length == 2) {

                String str = "";

                for (int i = 1; i < strings.length; i++) str += strings[i] + " ";

                if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(target.getName())) {

                    String replace = str.toString().trim().replace("&", "§");
                    target.getPlayer().kickPlayer(replace);
                    Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), replace, null, null);
                    commandSender.sendMessage(prefix + ChatColor.GREEN + "You successfully banned " + ChatColor.YELLOW + target.getName() + ChatColor.GREEN + " for" + ChatColor.RESET + str.trim().replace("&", "§"));

                } else
                    commandSender.sendMessage(ChatColor.RED + "This player is already in the banlist!");

            } else {
                commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§bban §2§i<Player> [Reason]");
            }
        } else {

            OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);


            if (target == null) {
                commandSender.sendMessage(ErrorMessages.PLAYER_DOESNOT_EXIST.getMessage());
                return true;
            }

            if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(target.getName())) {
                commandSender.sendMessage(prefix + ChatColor.RED + "This player is already unbanned!");
                return true;
            }

            Bukkit.getBanList(BanList.Type.NAME).pardon(target.getName());
            commandSender.sendMessage(prefix + ChatColor.GREEN + "You pardon " + ChatColor.YELLOW + target.getName());
        }
        return true;
    }
}
