package com.xg7network.xg7lobby.Module.Events.PlayerEvents.Other;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Utils.PluginUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Hunger implements Listener {


    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (PluginUtil.isInWorld(player) && !configManager.getConfig(ConfigType.CONFIG).getBoolean("hunger-loss")) player.setFoodLevel(20);



    }


}
