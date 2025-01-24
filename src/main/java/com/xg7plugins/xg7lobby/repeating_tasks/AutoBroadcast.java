package com.xg7plugins.xg7lobby.repeating_tasks;

import com.cryptomorin.xseries.XSound;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.tasks.Task;
import com.xg7plugins.tasks.TaskState;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class AutoBroadcast extends Task {

    private final AtomicInteger index = new AtomicInteger(0);

    public AutoBroadcast() {
        super(XG7Lobby.getInstance(), "auto-broadcast", true, true, XG7Lobby.getInstance().getConfig("config").getTime("auto-broadcast.cooldown").orElse(3 * 60 * 1000L), TaskState.RUNNING, null);
    }

    @Override
    public void run() {
        Config config = XG7Lobby.getInstance().getConfig("config");

        String[] soundString = config.get("auto-broadcast.sound", String.class).orElse("ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1").split(", ");

        Sound sound = XSound.matchXSound(soundString[0]).orElse(XSound.ENTITY_SLIME_JUMP).get();

        float volume = Float.parseFloat(soundString[1]);
        float pitch = Float.parseFloat(soundString[2]);

        boolean broadcastOnlyInLobby = config.get("auto-broadcast.broadcast-only-in-the-lobby", Boolean.class).orElse(false);

        boolean random = config.get("auto-broadcast.random", Boolean.class).orElse(false);

        List<Map> ads = config.getList("auto-broadcast.advertisements", Map.class).orElse(null);

        if (ads == null) return;


        int index = random ? new Random().nextInt(ads.size()) : this.index.getAndIncrement();

        if (index >= ads.size()) {
            this.index.set(1);
            index = 0;
        }

        int finalIndex = index;
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (broadcastOnlyInLobby && !XG7Lobby.getInstance().isInWorldEnabled(player)) return;

            ((List<String>)ads.get(finalIndex).get("ad")).forEach(message -> Text.detectLangOrText(
                    XG7Lobby.getInstance(),
                    player,
                    message
            ).join().send(player));

            player.playSound(player.getLocation(), sound, volume, pitch);
        });


    }



}
