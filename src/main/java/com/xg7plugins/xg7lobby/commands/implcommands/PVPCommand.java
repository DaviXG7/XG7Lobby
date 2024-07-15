package com.xg7plugins.xg7lobby.commands.implcommands;

import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.actions.Action;
import com.xg7plugins.xg7lobby.tasks.CooldownTask;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PVPCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbypvp";
    }

    @Override
    public String getDescription() {
        return "Enables pvp on lobby";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbypvp (Player)";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return true;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }
    @Override
    public PermissionType getPermission() {
        return PermissionType.PVP;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


        if (args.length == 1) {

            if (!sender.hasPermission(PermissionType.PVP_OTHER.getPerm())) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.no-permission"), sender);
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                Text.send(Config.getString(ConfigType.MESSAGES, "commands.player-doesnt-exist"), sender);
                return true;
            }
            if (!Config.getBoolean(ConfigType.CONFIG, "pvp.enabled")) {
                Text.send(Config.getString(ConfigType.MESSAGES, "pvp.not-enabled"), sender);
                return true;
            }

            PlayerData data = PlayerManager.getPlayerData(target.getUniqueId());

            if (!data.isPVPEnabled() && data.isFlying() && Config.getBoolean(ConfigType.CONFIG, "pvp.disable-fly")) {
                Text.send(Config.getString(ConfigType.MESSAGES, "fly.on-pvp-fly"), sender);
                return true;
            }

            if (!CacheManager.getPvpCache().asMap().containsKey(target.getUniqueId())) {

                CacheManager.put(target.getUniqueId(), CacheType.PVP_COOLDOWN, null);

                TaskManager.addTask(new CooldownTask("cooldown:pvp=" + target.getUniqueId()) {
                    @Override
                    public void run() {

                        if (!CacheManager.getPvpCache().asMap().containsKey(target.getUniqueId())) {
                            data.setPVPEnabled(!data.isPVPEnabled());
                            CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
                            Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
                            SQLHandler.update("UPDATE players SET ispvpenabled = ? WHERE id = ?", data.isPVPEnabled(), data.getId());

                                        if (data.isPVPEnabled()) {
                                            target.setMaxHealth(Config.getDouble(ConfigType.CONFIG, "pvp.max-hearts") * 2);
                                            target.setHealth(target.getMaxHealth());
                                            target.getActivePotionEffects().clear();
                                        }

                            Config.getList(ConfigType.CONFIG, data.isPVPEnabled() ? "pvp.events-enable" : "pvp.events-disable").forEach(action -> Action.execute(action, target));

                            Text.send(data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.on-enable") : Config.getString(ConfigType.MESSAGES, "pvp.on-disable"), target);
                            Text.send(data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.on-enable-other").replace("[PLAYER]", target.getName()) : Config.getString(ConfigType.MESSAGES, "pvp.on-disable-other").replace("[PLAYER]", target.getName()), sender);
                            },5L);
                            TaskManager.cancelTask(this.getName());
                            return;
                        }
                        Text.send(!data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.on-enabling").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getPvpCache().asMap().get(target.getUniqueId()) - System.currentTimeMillis()) + 1) + "") : Config.getString(ConfigType.MESSAGES, "pvp.on-disabling").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getPvpCache().asMap().get(target.getUniqueId()) - System.currentTimeMillis()) + 1) + ""), target);
                        Text.send(!data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.on-enabling-other").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getPvpCache().asMap().get(target.getUniqueId()) - System.currentTimeMillis()) + 1) + "").replace("[PLAYER]", target.getName()) : Config.getString(ConfigType.MESSAGES, "pvp.on-disabling-other").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getPvpCache().asMap().get(target.getUniqueId()) - System.currentTimeMillis()) + 1) + "").replace("[PLAYER]", target.getName()), sender);

                    }
                });
                return true;

            }

            Text.send(data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.pvp-disable-cancelled") : Config.getString(ConfigType.MESSAGES, "pvp.pvp-enable-cancelled"), target);
            Text.send(data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.pvp-disable-cancelled") : Config.getString(ConfigType.MESSAGES, "pvp.pvp-enable-cancelled"), sender);
            TaskManager.cancelTask("cooldown:lobby=" + target.getUniqueId());
            CacheManager.remove(target.getUniqueId(), CacheType.PVP_COOLDOWN);

            return true;
        }
        if (!(sender instanceof Player)) {
            Text.send(Config.getString(ConfigType.MESSAGES, "commands.not-a-player"), sender);
            return true;
        }

        if (!Config.getBoolean(ConfigType.CONFIG, "pvp.enabled")) {
            Text.send(Config.getString(ConfigType.MESSAGES, "pvp.not-enabled"), sender);
            return true;
        }

        Player player = (Player) sender;

        PlayerData data = PlayerManager.getPlayerData(player.getUniqueId());

        if (!data.isPVPEnabled() && data.isFlying() && Config.getBoolean(ConfigType.CONFIG, "pvp.disable-fly")) {
            Text.send(Config.getString(ConfigType.MESSAGES, "fly.on-pvp-fly"), sender);
            return true;
        }

        if (!CacheManager.getPvpCache().asMap().containsKey(player.getUniqueId())) {

            CacheManager.put(player.getUniqueId(), CacheType.PVP_COOLDOWN, null);

            TaskManager.addTask(new CooldownTask("cooldown:pvp=" + player.getUniqueId()) {
                @Override
                public void run() {


                    if (!CacheManager.getPvpCache().asMap().containsKey(player.getUniqueId())) {
                        data.setPVPEnabled(!data.isPVPEnabled());
                        CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
                        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
                        SQLHandler.update("UPDATE players SET ispvpenabled = ? WHERE id = ?", data.isPVPEnabled(), data.getId());

                            if (data.isPVPEnabled()) {
                                player.setMaxHealth(Config.getDouble(ConfigType.CONFIG, "pvp.max-hearts") * 2);
                                player.setHealth(player.getMaxHealth());
                                player.getActivePotionEffects().clear();
                            }


                        Config.getList(ConfigType.CONFIG, data.isPVPEnabled() ? "pvp.events-enable" : "pvp.events-disable").forEach(action -> Action.execute(action, player));

                        Text.send(data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.on-enable") : Config.getString(ConfigType.MESSAGES, "pvp.on-disable"), player);
                        },5L);
                        TaskManager.cancelTask(this.getName());
                        return;
                    }
                    Text.send(!data.isPVPEnabled() ? Config.getString(ConfigType.MESSAGES, "pvp.on-enabling").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getPvpCache().asMap().get(player.getUniqueId()) - System.currentTimeMillis()) + 1) + "") : Config.getString(ConfigType.MESSAGES, "pvp.on-disabling").replace("[SECONDS]", (TimeUnit.MILLISECONDS.toSeconds(CacheManager.getPvpCache().asMap().get(player.getUniqueId()) - System.currentTimeMillis()) + 1) + ""), player);
                }
            });
            return true;

        }

        Text.send(Config.getString(ConfigType.MESSAGES, data.isPVPEnabled() ? "pvp.pvp-disable-cancelled" : "pvp.pvp-enable-cancelled"), player);
        TaskManager.cancelTask("cooldown:pvp=" + player.getUniqueId());
        CacheManager.remove(player.getUniqueId(), CacheType.LOBBY_COOLDOWN);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()) : new ArrayList<>();
    }
}
