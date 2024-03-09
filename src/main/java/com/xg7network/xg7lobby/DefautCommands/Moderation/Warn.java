package com.xg7network.xg7lobby.DefautCommands.Moderation;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Player.PlayerData;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class Warn implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (strings.length <= 1) {
            commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage().replace("[COMMAND]", "§e/§bwarn §2§i<Player> <Reason> or §e/§bwarn §2§i<remove> <Player> <warnId>"));
            return true;
        }

        if (strings[0].equals("remove")) {

            if (strings.length == 2) {
                commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage().replace("[COMMAND]", "§e/§bwarn §2§i<Player> <Reason> or §e/§bwarn §2§i<remove> <Player> <warnId>"));
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(strings[1]);

            PlayerData playerData = PlayersManager.getData(target.getUniqueId().toString());
            if (playerData == null)
            {
                commandSender.sendMessage(prefix + "§cThis player has never joined the server or doesn't exists!");
                return true;
            }
            com.xg7network.xg7lobby.Player.Warn warn = playerData.getInfractions()
                    .stream()
                    .filter(warn1 -> warn1.getId().equals(strings[2]))
                    .findFirst()
                    .orElse(null);

            if (!playerData.removeInfraction(strings[2])) {
                commandSender.sendMessage(prefix + ChatColor.RED + "Either the player or the warn id was not entered correctly, check if the player id or name is correct.");
            } else {
                commandSender.sendMessage(prefix + ChatColor.GREEN + "You have successfully removed the warn from " + ChatColor.AQUA + target.getName());
                if (playerData.getPlayer().isOnline()) TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.on-warn-remove").replace("[WARN]", warn.getWarn()), playerData.getPlayer());

            }


            return true;
        } else {

            if (!PluginUtil.hasPermission(commandSender, PermissionType.WARN_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
                return true;

            OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);
            if (target.isOnline()) {
                if (target.getPlayer().hasPermission(PermissionType.WARN_COMMAND.getPerm())) {
                    commandSender.sendMessage(prefix + "§cYou cannot warn a player with admin perms.");
                    return true;
                }
            }


            StringBuilder str = new StringBuilder();

            for (int i = 1; i < strings.length; i++) str.append(strings[i]).append(" ");


            PlayerData playerData = PlayersManager.getData(target.getUniqueId().toString());
            if (playerData == null)
            {
                commandSender.sendMessage(prefix + "§cThis player has never joined the server or doesn't exists!");
                return true;
            }
            playerData.addInfraction(str.toString().trim().replace("&", "§"), System.currentTimeMillis());

            commandSender.sendMessage(prefix + ChatColor.GREEN + "You warned " + target.getName() + " for " + str.toString().toString().trim().replace("&", "§"));
            if (playerData.getPlayer().isOnline()) TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.on-warn").replace("[WARN]", str.toString().toString().trim().replace("&", "§")), playerData.getPlayer());

            PlayersManager.update(target.getUniqueId().toString(), playerData);

            Infractions.verify(target.getPlayer(), playerData.getInfractions().size());
        }
        return true;
    }
}
