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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class Mute implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (!PluginUtil.hasPermission(commandSender, PermissionType.MUTE_COMMAND, ErrorMessages.NO_PEMISSION.getMessage()))
            return true;

        if (strings.length == 0) {
            commandSender.sendMessage(
                    command.getName().equalsIgnoreCase("xg7lobbymute") ?
                            ErrorMessages.MISSING_ARGS.getMessage().replace("[COMMAND]", "§e/§bmute §2§i<Player>") :

                            command.getName().equalsIgnoreCase("xg7lobbyunmute") ?
                                    ErrorMessages.MISSING_ARGS.getMessage().replace("[COMMAND]", "§e/§bunmute §2§i<Player>") :

                                    command.getName().equalsIgnoreCase("xg7lobbytempmute") ?
                                            ErrorMessages.MISSING_ARGS.getMessage().replace("[COMMAND]", "§e/§btempmute §2§i<Player> <d/h/min, Date>") : ""
            );
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(strings[0]);

        if (target == null) {
            commandSender.sendMessage(ErrorMessages.PLAYER_DOESNOT_EXIST.getMessage());
            return true;
        }

        PlayerData data = PlayersManager.getData(target.getUniqueId().toString());

        switch (command.getName()) {
            case "xg7lobbymute":
                if (data.isMuted()) {
                    commandSender.sendMessage(prefix + "§cThis player is already muted!");
                    return true;
                }

                data.setMuted(!target.getPlayer().hasPermission(PermissionType.WARN_COMMAND.getPerm()));
                data.setLastDayToUnmute(0);
                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute")) data.addInfraction(ChatColor.RED + "Muted by adm!", System.currentTimeMillis());
                PlayersManager.update(target.getUniqueId().toString(), data);
                Infractions.verify(target.getPlayer(), data.getInfractions().size());

                commandSender.sendMessage(data.isMuted() ? prefix + "§aYou have successfully muted §b" + target.getName() : prefix + "§cYou cannot mute a player with admin perms.");

                return true;

            case "xg7lobbyunmute":

                if (!data.isMuted()) {
                    commandSender.sendMessage(prefix + "§cThis player is already unmuted!");
                    return true;
                }

                data.setMuted(false);

                if (target.isOnline()) TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.when-muted"), target.getPlayer());

                data.setLastDayToUnmute(0);
                PlayersManager.update(target.getUniqueId().toString(), data);

                commandSender.sendMessage(prefix + "§aYou have successfully unmuted §b" + target.getName());

                return true;

            case "xg7lobbytempmute":

                if (data.isMuted()) {
                    commandSender.sendMessage(prefix + "§cThis player is already muted! Unmute and temp mute again!");
                    return true;
                }

                if (strings.length < 2) {
                    commandSender.sendMessage(ErrorMessages.MISSING_ARGS.getMessage() + "§e/§btempmute §2§i<Player> <d/h/min or Date>");
                    return true;
                }

                if (strings.length == 2) {

                    if (!target.getPlayer().hasPermission(PermissionType.WARN_COMMAND.getPerm())) {

                        if (strings[1].contains("min")) {
                            String time = strings[1].replace("min", "");
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.MINUTE, Integer.parseInt(time));
                            data.setMuted(true);
                            data.setLastDayToUnmute(calendar.getTime().getTime());
                            if (target.isOnline())
                                TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.when-muted").replace("[DAYS]", TimeUnit.MILLISECONDS.toDays((data.getLastDayToUnmute() - new Date().getTime())) + " days, " + TimeUnit.MILLISECONDS.toHours((data.getLastDayToUnmute() - new Date().getTime())) % 24 + " hours and " + TimeUnit.MILLISECONDS.toMinutes((data.getLastDayToUnmute() - new Date().getTime())) % 60 + " minutes!"), target.getPlayer());
                        } else if (strings[1].contains("h")) {
                            String time = strings[1].replace("h", "");
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.HOUR, Integer.parseInt(time));
                            data.setMuted(true);
                            data.setLastDayToUnmute(calendar.getTime().getTime());
                            if (target.isOnline())
                                TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.when-muted").replace("[DAYS]", TimeUnit.MILLISECONDS.toDays((data.getLastDayToUnmute() - new Date().getTime())) + " days, " + TimeUnit.MILLISECONDS.toHours((data.getLastDayToUnmute() - new Date().getTime())) % 24 + " hours and " + TimeUnit.MILLISECONDS.toMinutes((data.getLastDayToUnmute() - new Date().getTime())) % 60 + " minutes!"), target.getPlayer());
                        } else if (strings[1].contains("d")) {
                            String time = strings[1].replace("d", "");
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.HOUR, Integer.parseInt(time) * 24);
                            data.setMuted(true);
                            data.setLastDayToUnmute(calendar.getTime().getTime());
                            if (target.isOnline())
                                TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.when-muted").replace("[DAYS]", TimeUnit.MILLISECONDS.toDays((data.getLastDayToUnmute() - new Date().getTime())) + " days, " + TimeUnit.MILLISECONDS.toHours((data.getLastDayToUnmute() - new Date().getTime())) % 24 + " hours and " + TimeUnit.MILLISECONDS.toMinutes((data.getLastDayToUnmute() - new Date().getTime())) % 60 + " minutes!"), target.getPlayer());
                        } else {
                            commandSender.sendMessage(ErrorMessages.SYNTAX_ERROR.getMessage());
                        }

                        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute")) data.addInfraction(ChatColor.RED + "Muted by adm!", System.currentTimeMillis());
                        PlayersManager.update(target.getUniqueId().toString(), data);
                        Infractions.verify(target.getPlayer(), data.getInfractions().size());

                        commandSender.sendMessage(prefix + "§aYou have successfully muted §b" + target.getName() + " §afor §e" + TimeUnit.MILLISECONDS.toDays((data.getLastDayToUnmute() - new Date().getTime())) + " days, " + TimeUnit.MILLISECONDS.toHours((data.getLastDayToUnmute() - new Date().getTime())) % 24 + " hours and " + TimeUnit.MILLISECONDS.toMinutes((data.getLastDayToUnmute() - new Date().getTime())) % 60 + " minutes!");
                    } else {
                        commandSender.sendMessage(prefix + "§cYou cannot mute a player with admin perms.");
                    }

                } else if (strings.length == 3) {

                    try {

                        String[] date = strings[1].split("/");
                        String[] time = strings[2].split(":");

                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]));

                        Date daytounmute = calendar.getTime();
                        data.setMuted(true);
                        data.setLastDayToUnmute(daytounmute.getTime());

                        if (target.isOnline()) TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.when-muted").replace("[DAYS]", TimeUnit.MILLISECONDS.toDays(data.getLastDayToUnmute() - new Date().getTime()) + " days, " + TimeUnit.MILLISECONDS.toHours((data.getLastDayToUnmute() - new Date().getTime())) % 24 + " hours and " + TimeUnit.MILLISECONDS.toMinutes((data.getLastDayToUnmute() - new Date().getTime())) % 60 + " minutes!"), target.getPlayer());
                        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("infraction-on-mute")) data.addInfraction(ChatColor.RED + "Muted by adm!", System.currentTimeMillis());
                        PlayersManager.update(target.getUniqueId().toString(), data);
                        Infractions.verify(target.getPlayer(), data.getInfractions().size());

                    } catch (Exception e) {
                        commandSender.sendMessage(ErrorMessages.SYNTAX_ERROR.getMessage());
                    }

                } else {
                    commandSender.sendMessage(ErrorMessages.SYNTAX_ERROR.getMessage());
                }

                return true;
        }


        return true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        PlayerData data = PlayersManager.getData(player.getUniqueId().toString());

        if (data.getLastDayToUnmute() <= System.currentTimeMillis() && data.getLastDayToUnmute() != 0) {
            data.setLastDayToUnmute(0);
            data.setMuted(false);
            PlayersManager.update(player.getUniqueId().toString(), data);
            return;
        }

        event.setCancelled(configManager.getConfig(ConfigType.CONFIG).getBoolean("mute-only-in-lobby") ? PluginUtil.isInWorld(player) && data.isMuted() : data.isMuted());

        if (event.isCancelled()) {
            if (data.getLastDayToUnmute() >= 0)
                TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.on-mute"), player);
            else

                TextUtil.send(configManager.getConfig(ConfigType.MESSAGES).getString("commands.on-mute-with-time").replace("[DAYS]", TimeUnit.MILLISECONDS.toDays((data.getLastDayToUnmute() - new Date().getTime())) + " days, " + TimeUnit.MILLISECONDS.toHours((data.getLastDayToUnmute() - new Date().getTime())) % 24 + " hours and " + TimeUnit.MILLISECONDS.toMinutes((data.getLastDayToUnmute() - new Date().getTime())) % 60 + " minutes!"), player);

        }

    }


}

