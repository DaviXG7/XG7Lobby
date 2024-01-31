package com.xg7network.xg7lobby.Module.Events.Jumps;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class LaunchPad implements Listener {

    private Material block;
    private Material blockUnder;
    private byte launchpadtype;
    private final boolean enabled;

    public LaunchPad() {
        this.enabled = configManager.getConfig(ConfigType.CONFIG).getBoolean("launch-pad.enabled");

        if (enabled) {
            if (configManager.getConfig(ConfigType.CONFIG).getBoolean("launch-pad.one-block"))
                if (configManager.getConfig(ConfigType.CONFIG).getBoolean("launch-pad.pressure-plate")) {
                    this.block = Material.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("launch-pad.block"));
                    this.launchpadtype = 0;
                } else {
                    this.blockUnder = Material.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("launch-pad.block"));
                    this.launchpadtype = 1;
                }
            else {
                this.block = Material.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("launch-pad.top-block"));
                this.blockUnder = Material.valueOf(configManager.getConfig(ConfigType.CONFIG).getString("launch-pad.bottom-block"));

                this.launchpadtype = 2;

            }
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (enabled) {
            Player player = event.getPlayer();

            if (!player.getGameMode().equals(GameMode.SPECTATOR))
                switch (launchpadtype) {

                    case 0:

                        if (player.getLocation().getBlock().getType().equals(block)) {
                            PluginUtil.playSound(player, configManager.getConfig(ConfigType.CONFIG).getString("launch-pad.sound"));
                            player.setVelocity(player.getEyeLocation().getDirection().multiply(configManager.getConfig(ConfigType.CONFIG).getDouble("launch-pad.strength")).setY(configManager.getConfig(ConfigType.CONFIG).getDouble("launch-pad.jump")));
                        }

                        return;
                    case 1:

                        if (player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0)).getType().equals(blockUnder)) {
                            PluginUtil.playSound(player, configManager.getConfig(ConfigType.CONFIG).getString("launch-pad.sound"));
                            player.setVelocity(player.getEyeLocation().getDirection().multiply(configManager.getConfig(ConfigType.CONFIG).getDouble("launch-pad.strength")).setY(configManager.getConfig(ConfigType.CONFIG).getDouble("launch-pad.jump")));
                        }

                        return;
                    case 2:

                        if (player.getLocation().getBlock().getType().equals(block) && player.getWorld().getBlockAt(player.getLocation().subtract(0, 1, 0)).getType().equals(blockUnder)) {
                            PluginUtil.playSound(player, configManager.getConfig(ConfigType.CONFIG).getString("launch-pad.sound"));
                            player.setVelocity(player.getEyeLocation().getDirection().multiply(configManager.getConfig(ConfigType.CONFIG).getDouble("launch-pad.strength")).setY(configManager.getConfig(ConfigType.CONFIG).getDouble("launch-pad.jump")));
                        }



            }
        }
    }


}
