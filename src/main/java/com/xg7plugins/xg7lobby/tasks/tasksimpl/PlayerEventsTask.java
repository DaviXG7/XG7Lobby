package com.xg7plugins.xg7lobby.tasks.tasksimpl;

import com.xg7plugins.xg7lobby.utils.XSeries.XPotion;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.player.PlayerManager;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.tasks.Task;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;

public class PlayerEventsTask extends Task {

    public PlayerEventsTask() {
        super("player-events", Config.getLong(ConfigType.CONFIG, "player-task-delay"));
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(player -> EventManager.getWorlds().contains(player.getWorld().getName())).forEach(player -> {
            if (!PlayerManager.getPlayerData(player.getUniqueId()).isPVPEnabled()) {
                if (!Config.getBoolean(ConfigType.CONFIG, "hunger-loss")) player.setFoodLevel(20);
                player.setMaxHealth(Config.getDouble(ConfigType.CONFIG, "max-hearths") * 2);
                for (String effect : Config.getList(ConfigType.CONFIG, "effects")) {
                    String[] effectSplit = effect.split(", ");
                    player.addPotionEffect(new PotionEffect(XPotion.valueOf(effectSplit[0].toUpperCase()).getPotionEffectType(), 999999999, Integer.parseInt(effectSplit[1]) - 1));
                }
            }
        });
    }
}
