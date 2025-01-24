package com.xg7plugins.xg7lobby.repeating_tasks;

import com.cryptomorin.xseries.XPotion;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.tasks.Task;
import com.xg7plugins.tasks.TaskState;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class Effects extends Task {
    private final List<PotionEffect> effects = new ArrayList<>();

    public Effects() {
        super(XG7Lobby.getInstance(), "effects", false, true, XG7Lobby.getInstance().getConfig("config").getTime("effects-task-delay").orElse(10000L), TaskState.RUNNING, null);

        Config config = XG7Lobby.getInstance().getConfig("config");

        List<String> effectsStringList = config.getList("effects", String.class).orElse(null);

        if (effectsStringList == null) return;

        effectsStringList.forEach(effectString -> {

            String[] effect = effectString.split(", ");

            XPotion potion = XPotion.matchXPotion(effect[0]).orElse(null);

            if (potion == null) return;

            PotionEffect potionEffect = new PotionEffect(potion.getPotionEffectType(), 200, Integer.parseInt(effect[1]), false, true);

            effects.add(potionEffect);
        });

    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!XG7Lobby.getInstance().isInWorldEnabled(player)) return;
            effects.forEach(effect -> {
                player.removePotionEffect(effect.getType());
                player.addPotionEffect(effect);
            });
        });
    }
}
