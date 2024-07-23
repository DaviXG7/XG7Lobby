package com.xg7plugins.xg7lobby.tasks.tasksimpl;

import com.cryptomorin.xseries.XSound;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.tasks.Task;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AutoBroadcast extends Task {
    public AutoBroadcast() {
        super("xg7lautobroadcast", Config.getLong(ConfigType.CONFIG, "auto-broadcast.cooldown") * 1200);
    }

    private int index = 0;

    @Override
    public void run() {

        Bukkit.getOnlinePlayers().forEach(p -> {
            List<String> keys = new ArrayList<>(Config.getConfigurationSections(ConfigType.CONFIG, "auto-broadcast.announcements"));

            List<String> announcements = Config.getList(ConfigType.CONFIG, "auto-broadcast.announcements." + keys.get(index));

            if (Config.getBoolean(ConfigType.CONFIG,"auto-broadcast.random")) {
                int random = new Random().nextInt(keys.size());
                announcements = Config.getList(ConfigType.CONFIG, "auto-broadcast.announcements." + keys.get(random));
            }

            for (String announce : announcements) {
                Text.send(announce, p);
            }
            String[] sound = Config.getString(ConfigType.CONFIG, "auto-broadcast.sound").split(", ");
            if (sound.length != 1) p.playSound(p.getLocation(), Objects.requireNonNull(XSound.valueOf(sound[0].toUpperCase()).parseSound()), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));

            index++;
            if (index == keys.size()) index = 0;
        });
    }
}
