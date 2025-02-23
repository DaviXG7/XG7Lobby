package com.xg7plugins.xg7lobby.events.chat_events;

import com.google.common.base.Strings;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.tasks.TaskManager;
import com.xg7plugins.utils.Time;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import com.xg7plugins.xg7lobby.lobby.player.Warn;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AntiSwear implements Listener {

    private final HashMap<String, Integer> tolerance = new HashMap<>();
    private final Config config = XG7Lobby.getInstance().getConfig("config");
    private final List<String> swearWords = config.getList("anti-swearing.blocked-words", String.class).orElse(new ArrayList<>()).stream().map(String::toLowerCase).collect(Collectors.toList());

    @Override
    public boolean isEnabled() {
        return config.get("anti-swearing.enabled", Boolean.class).orElse(false);
    }

    @EventHandler(
            isOnlyInWorld = true
    )
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("xg7lobby.chat.swear")) return;

        Player player = event.getPlayer();

        boolean dontSend = config.get("anti-swearing.dont-send-the-message", Boolean.class).orElse(false);

        String message = event.getMessage().toLowerCase();

        for (String word : swearWords) {
            if (message.contains(word)) {
                if (dontSend) {
                    event.setCancelled(true);
                    Text.fromLang(player,XG7Lobby.getInstance(), "chat.swear").thenAccept(text -> text.send(player));
                    return;
                }

                if (config.get("anti-swearing.words-tolerance", Integer.class).orElse(0) > 0) {
                    tolerance.putIfAbsent(player.getName(), 0);
                    tolerance.put(player.getName(), tolerance.get(player.getName()) + 1);

                    if (tolerance.get(player.getName()) >= config.get("anti-swearing.words-tolerance", Integer.class).orElse(0)) {
                        event.setCancelled(true);
                        LobbyPlayer.cast(player.getUniqueId(), true).thenAccept(lobbyPlayer -> lobbyPlayer.addInfraction(new Warn(player.getUniqueId(), config.get("anti-swearing.tolerance-warn-level", Integer.class).orElse(1), "Swearing")));
                        return;
                    }

                    Bukkit.getScheduler().runTaskLater(XG7Lobby.getInstance(), () -> {
                        tolerance.put(player.getName(), tolerance.get(player.getName()) - 1);
                        if (tolerance.get(player.getName()) == 0) tolerance.remove(player.getName());

                    }, Time.convertMillisToTicks(config.getTime("anti-swearing.time-for-decrement-tolerance").orElse(5000L)));
                }
                break;
            }
        }

        for (String word : swearWords) {
            message = message.replace(word, Strings.repeat(config.get("anti-swearing.replacement", String.class).orElse("*"), word.length()));
        }
        event.setMessage(message);

    }
}
