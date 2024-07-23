package com.xg7plugins.xg7lobby.events.playerevents;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.Event;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

import java.io.File;
import java.util.stream.Collectors;

public class MOTDEvent implements Event {
    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "motd.enabled");
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        String motd = Config.getList(ConfigType.CONFIG, "motd.text").stream().map(txt -> txt.startsWith("[CENTER] ") ? Text.getCentralizedText(Text.PixelsSize.MOTD.getPixels(), txt.substring(9)) : txt).collect(Collectors.joining("\n"));
        event.setMotd(Text.translateColorCodes(motd));
        event.setMaxPlayers(Config.getInt(ConfigType.CONFIG, "motd.max-players"));
        try {
            event.setServerIcon(Bukkit.loadServerIcon(new File("icon.png")));
        } catch (Exception ignored) {}
    }
}
