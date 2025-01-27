package com.xg7plugins.xg7lobby.events;

import com.xg7plugins.data.config.Config;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.bukkitevents.EventHandler;
import com.xg7plugins.utils.text.Text;
import com.xg7plugins.utils.text.TextCentralizer;
import com.xg7plugins.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;
import java.util.Collections;
import java.util.stream.Collectors;

public class MOTDEvent implements Listener {
    @Override
    public boolean isEnabled() {
        return XG7Lobby.getInstance().getConfig("config").get("motd.enabled", Boolean.class).orElse(false);
    }

    @EventHandler
    public void onMOTDRequest(ServerListPingEvent event) {
        System.out.println("MOTD Requested");
        Config config = XG7Lobby.getInstance().getConfig("config");

        String motd = config.getList("motd.text", String.class)
                .orElse(Collections.singletonList("Powred by XG7Plugins"))
                .stream()
                .map(
                        (txt) -> Text.format(txt).getTextCentralized(TextCentralizer.PixelsSize.MOTD)
                ).collect(Collectors.joining("\n"));
        event.setMotd(motd);
        event.setMaxPlayers(config.get("motd.max-players", int.class).orElse(100));

        try {
            event.setServerIcon(Bukkit.loadServerIcon(new File("icon.png")));
        } catch (Exception var4) {
        }

    }
}
