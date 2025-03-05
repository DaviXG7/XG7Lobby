package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

public class MOTDEvent implements Listener {

    @EventHandler
    public void onMOTDRequest(ServerListPingEvent event) {
        Config config = XG7Lobby.getInstance().getConfig("config");
        if (!config.get("motd.enabled", Boolean.class).orElse(false)) return;

        String motd = config.getList("motd.text", String.class)
                .orElse(Collections.singletonList("Powered by XG7Plugins"))
                .stream()
                .map(
                        (txt) -> Text.format(txt).getPlainText()
                ).collect(Collectors.joining("\n"));
        event.setMotd(motd);
        event.setMaxPlayers(config.get("motd.max-players", Integer.class).orElse(100));

        try {
            event.setServerIcon(Bukkit.loadServerIcon(new File("icon.png")));
        } catch (Exception var4) {
        }

    }
}
