package com.xg7plugins.xg7lobby.events.jumpevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.events.EventManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class LaunchpadEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "launchpad.enabled");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!EventManager.getWorlds().contains(event.getPlayer().getWorld().getName())) return;

        Player player = event.getPlayer();

        if (Config.getBoolean(ConfigType.CONFIG, "pvp.disable-launchpad") && PlayerManager.getPlayerData(player.getUniqueId()).isPVPEnabled()) return;
        if ((Config.getString(ConfigType.CONFIG, "launchpad.top-block").equals("AIR") || player.getLocation().getBlock().getType().name().equals(Config.getString(ConfigType.CONFIG, "launchpad.top-block"))) && (Config.getString(ConfigType.CONFIG, "launchpad.bottom-block").equals("AIR") || player.getLocation().subtract(0,1,0).getBlock().getType().name().equals(Config.getString(ConfigType.CONFIG, "launchpad.bottom-block")))) {
            player.setVelocity(player.getEyeLocation().getDirection().multiply(Config.getDouble(ConfigType.CONFIG, "launchpad.strength")).setY(Config.getDouble(ConfigType.CONFIG, "launchpad.jump")));
            String[] sound = Config.getString(ConfigType.CONFIG, "launchpad.sound").split(", ");
            player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
        }
    }
}
