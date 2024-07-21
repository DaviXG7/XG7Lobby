package com.xg7plugins.xg7lobby.tasks.tasksimpl;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.chatevents.AntiSpamEvent;
import com.xg7plugins.xg7lobby.tasks.Task;
import org.bukkit.Bukkit;

public class ChatTask extends Task {

    public ChatTask() {
        super("xg7lantispam", Config.getLong(ConfigType.CONFIG, "anti-spam.spam-cooldown") * 20);
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            if (AntiSpamEvent.getSpamRange().containsKey(p.getUniqueId()) && !AntiSpamEvent.getSpamRange().get(p.getUniqueId()).isEmpty()) AntiSpamEvent.getSpamRange().get(p.getUniqueId()).remove(0);
        });
    }
}
