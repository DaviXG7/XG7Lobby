package com.xg7network.xg7lobby.Module.Selectors;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.Other.PluginUtil;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class SelectorListener implements Listener {

    private final Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown"), TimeUnit.SECONDS).build();


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (PluginUtil.isInWorld(player))

            if (event.getItem() != null) {

                if (SelectorManager.containsItemInHand(player)) {

                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                            event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
                        if (this.cooldown.asMap().containsKey(player.getUniqueId())) {
                            TextUtil.send(TextUtil.get(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-cooldown").replace("SECONDS", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown.asMap().get(player.getUniqueId()) - System.currentTimeMillis())))), player);
                        } else {
                            this.cooldown.put(player.getUniqueId(), System.currentTimeMillis() + configManager.getConfig(ConfigType.SELECTORS).getLong("selectors.cooldown") * 1000L);
                            SelectorManager.execute(player, player.getItemInHand());
                        }
                }
            }


    }
}
