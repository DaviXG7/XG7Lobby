package com.xg7plugins.xg7lobby.events.playerevents;

import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.cache.CacheType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.data.player.model.PlayerData;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.events.JoinQuitEvent;
import com.xg7plugins.xg7lobby.events.actions.Action;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.utils.PacketEvents;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvent implements JoinQuitEvent {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event) {
        PlayerManager.createPlayerData(event.getUniqueId());
    }

    @Override
    public void onJoin(PlayerJoinEvent event) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) <= 13) PacketEvents.create(event.getPlayer());
        event.setJoinMessage(Text.getFormatedText(event.getPlayer(), Config.getString(ConfigType.MESSAGES, "lobby.on-join")));
        if (Config.getBoolean(ConfigType.CONFIG, "on-join.tp-to-lobby")) {
            if (Config.getString(ConfigType.DATA, "spawn-location.world") != null) {
                World world = Bukkit.getWorld(Config.getString(ConfigType.DATA, "spawn-location.world"));
                double x = Config.getDouble(ConfigType.DATA, "spawn-location.x");
                double y = Config.getDouble(ConfigType.DATA, "spawn-location.y");
                double z = Config.getDouble(ConfigType.DATA, "spawn-location.z");
                float yaw = (float) Config.getDouble(ConfigType.DATA, "spawn-location.yaw");
                float pitch = (float) Config.getDouble(ConfigType.DATA, "spawn-location.pitch");

                event.getPlayer().teleport(new Location(world,x,y,z,yaw,pitch));
            }
        }

        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        PlayerData data = PlayerManager.getPlayerData(event.getPlayer().getUniqueId());
        if (!data.isPVPEnabled() && !data.isBuildEnabled()) {
            if (Config.getBoolean(ConfigType.CONFIG, "on-join.clear-inventory")) event.getPlayer().getInventory().clear();
            if (Config.getBoolean(ConfigType.CONFIG, "on-join.heal")) event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
        }

        if (Config.getBoolean(ConfigType.CONFIG, "on-first-join.enabled")) {
            if (data.getFirstJoin() == 0) {
                data.setFirstJoin(System.currentTimeMillis());
                CacheManager.put(data.getId(), CacheType.SQL_QUERY, data);
                SQLHandler.update("UPDATE players SET firstJoin = ? WHERE id = ?", data.getFirstJoin(), data.getId());
                event.setJoinMessage(Text.getFormatedText(event.getPlayer(), Config.getString(ConfigType.MESSAGES, "lobby.on-first-join")));
                Config.getList(ConfigType.CONFIG, "on-first-join.events").forEach(action -> Action.execute(action, event.getPlayer()));
                return;
            }
        }

        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
            for (String action : Config.getList(ConfigType.CONFIG, "on-join.events")) {
                if (action.startsWith("[SWAP]")) {
                    action += ", selectorclickevent";
                }
                Action.execute(action, event.getPlayer());
            }
        },1L);

    }
    @Override
    public void onWorldJoin(Player player) {
        Bukkit.getScheduler().runTaskLater(XG7Lobby.getPlugin(), () -> {
            if (Config.getBoolean(ConfigType.CONFIG, "on-join.run-events-when-return-to-the-world")) {
                for (String action : Config.getList(ConfigType.CONFIG, "on-join.events")) {
                    if (action.startsWith("[SWAP]")) {
                        action += ", selectorclickevent";
                    }
                    Action.execute(action, player);
                }
            }
        },1L);

    }

    @Override
    public void onQuit(PlayerQuitEvent event) {
        PacketEvents.stopEvent(event.getPlayer());
        CacheManager.remove(event.getPlayer().getUniqueId(), CacheType.SQL_QUERY);

        TaskManager.cancelTask("cooldown:lobby=" + event.getPlayer().getUniqueId());
        TaskManager.cancelTask("cooldown:pvp=" + event.getPlayer().getUniqueId());

        event.setQuitMessage(Text.getFormatedText(event.getPlayer(), Config.getString(ConfigType.MESSAGES, "lobby.on-quit")));
    }
}
