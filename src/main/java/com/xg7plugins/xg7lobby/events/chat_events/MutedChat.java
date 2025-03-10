package com.xg7plugins.xg7lobby.events.chat_events;

import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MutedChat implements Listener {
    @Override
    public boolean isEnabled() {
        return true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        LobbyPlayer lobbyPlayer = LobbyPlayer.cast(event.getPlayer().getUniqueId(), false).join();

        if (lobbyPlayer.isMuted()) {

            if (System.currentTimeMillis() >= lobbyPlayer.getTimeForUnmute() && lobbyPlayer.getTimeForUnmute() != 0) {
                lobbyPlayer.setMuted(false);
                lobbyPlayer.setTimeForUnmute(0);
                lobbyPlayer.update().join();
                return;
            }

            long seconds = (lobbyPlayer.getTimeForUnmute() - System.currentTimeMillis()) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            seconds %= 60;
            minutes %= 60;
            hours %= 24;

            long finalHours = hours;
            long finalMinutes = minutes;
            long finalSeconds = seconds;

            Text.fromLang(event.getPlayer(),XG7Lobby.getInstance(), "chat.muted").thenAccept(text -> text.replace("time", String.format("%dd, %02dh %02dm %02ds", days, finalHours, finalMinutes, finalSeconds)).send(event.getPlayer()));
            event.setCancelled(true);
            return;
        }

    }
}
