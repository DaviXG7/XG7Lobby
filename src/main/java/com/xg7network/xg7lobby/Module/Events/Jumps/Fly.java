package com.xg7network.xg7lobby.Module.Events.Jumps;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Configs.PermissionType;
import com.xg7network.xg7lobby.DefautCommands.ErrorMessages;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class Fly implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {

            Player player = (Player) commandSender;

            if (strings.length == 1 && !strings[0].equals(player.getName())) {

                    if (PluginUtil.hasPermission(player, PermissionType.FLY_OTHER, ErrorMessages.NO_PEMISSION.getMessage())) {
                        Player target = Bukkit.getPlayer(strings[0]);
                        if (target != null) {

                            if (PluginUtil.isInWorld(target)) {

                                if (DoubleJump.isJumping.containsKey(target.getUniqueId()))
                                    DoubleJump.isJumping.remove(target.getUniqueId());
                                if (!FlyManager.canfly.containsKey(target.getUniqueId()))
                                    FlyManager.canfly.put(target.getUniqueId(), true);

                                if (!FlyManager.canfly.get(target.getUniqueId())) {
                                    if (target.getGameMode().equals(GameMode.CREATIVE) || target.getGameMode().equals(GameMode.SPECTATOR)) {
                                        player.sendMessage(ChatColor.RED + "You cannot change fly of §b" + target.getName() + " §cif the player's gamemode is creative or spectator!");
                                        return true;
                                    }
                                    new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.fly-enabled-other").replace("PLAYERT", target.getName()), player).sendMessage();
                                    new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.fly-enabled"), target).sendMessage();
                                    FlyManager.canfly.put(target.getUniqueId(), true);
                                } else {
                                    new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.fly-disabled-other").replace("PLAYERT", target.getName()), target).sendMessage();
                                    new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.fly-disabled"), target).sendMessage();
                                    target.setFlying(target.getGameMode().equals(GameMode.CREATIVE) || target.getGameMode().equals(GameMode.SPECTATOR));
                                    FlyManager.canfly.put(target.getUniqueId(), false);
                                }
                            } else commandSender.sendMessage(prefix + ChatColor.RED + "You cannot execute this command because this player is not in an enabled world!");

                        } else commandSender.sendMessage(ErrorMessages.PLAYER_DOESNOT_EXIST.getMessage());
                    }

            } else {
                if (PluginUtil.isInWorld(player)) {
                    if (PluginUtil.hasPermission(player, PermissionType.FLY_COMMAND, ErrorMessages.NO_PEMISSION.getMessage())) {
                        if (DoubleJump.isJumping.containsKey(player.getUniqueId())) {
                            DoubleJump.isJumping.remove(player.getUniqueId());
                            player.setAllowFlight(true);
                        }
                        if (!FlyManager.canfly.containsKey(player.getUniqueId())) FlyManager.canfly.put(player.getUniqueId(), false);
                        if (!FlyManager.canfly.get(player.getUniqueId())) {
                            if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
                                player.sendMessage(ChatColor.RED + "You cannot change to fly in creative or spectator modes!");
                                return true;
                            }
                            new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.fly-enabled"), player).sendMessage();
                            FlyManager.canfly.put(player.getUniqueId(), true);
                        } else {
                            new Message(configManager.getConfig(ConfigType.MESSAGES).getString("commands.fly-disabled"), player).sendMessage();
                            player.setFlying(player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR));
                            FlyManager.canfly.put(player.getUniqueId(), false);
                        }
                    }
                } else commandSender.sendMessage(ErrorMessages.NOT_IN_WORLD.getMessage());

            }

        } else commandSender.sendMessage(ErrorMessages.NOT_PLAYER.getMessage());


        return true;
    }
}
