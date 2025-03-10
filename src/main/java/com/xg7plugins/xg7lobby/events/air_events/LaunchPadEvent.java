package com.xg7plugins.xg7lobby.events.air_events;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.xg7lobby.XG7Lobby;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class LaunchPadEvent implements Listener {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("launchpad.enabled", Boolean.class).orElse(false);
    }

    @EventHandler(isOnlyInWorld = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Config config = XG7Lobby.getInstance().getConfig("config");

        if (XG7Lobby.getInstance().getGlobalPVPManager().isPlayerInPVP(event.getPlayer()) && config.get("global-pvp.disable-launchpad", Boolean.class).orElse(true)) return;

        String topBlock = config.get("launchpad.top-block", String.class).orElse(null);
        String bottomBlock = config.get("launchpad.bottom-block", String.class).orElse(null);

        XMaterial topBlockMaterial = null;
        XMaterial bottomBlockMaterial = null;

        if (topBlock != null && !topBlock.isEmpty()) {
            topBlockMaterial = XMaterial.matchXMaterial(topBlock).orElse(null);
        }
        if (bottomBlock != null && !bottomBlock.isEmpty()) {
            bottomBlockMaterial = XMaterial.matchXMaterial(bottomBlock).orElse(null);
        }

        Player player = event.getPlayer();

        if ((topBlockMaterial == null|| player.getLocation().getBlock().getType() == topBlockMaterial.get()) && (bottomBlockMaterial == null || player.getLocation().subtract(0, 1, 0).getBlock().getType() == bottomBlockMaterial.get())) {
            double power = config.get("launchpad.power", Double.class).orElse(1.0);
            double height = config.get("launchpad.height", Double.class).orElse(1.0);

            String[] soundString = config.get("launchpad.sound", String.class).orElse("ENTITY_BAT_TAKEOFF, 1, 1").split(", ");

            Sound sound = XSound.matchXSound(soundString[0]).orElse(XSound.ENTITY_SLIME_JUMP).get();

            float volume = Float.parseFloat(soundString[1]);
            float pitch = Float.parseFloat(soundString[2]);
            player.setVelocity(player.getLocation().getDirection().multiply(power).setY(height));

            player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
        }
    }
}
