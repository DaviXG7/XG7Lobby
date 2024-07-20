package com.xg7plugins.xg7lobby.events.commandevents.commandtabevents;

import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.PacketPlayEvent;
import com.xg7plugins.xg7lobby.utils.NMSUtil;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class TabCompleteEvent implements PacketPlayEvent.PacketPlayOutEvent {

    @Override
    public String[] getPacketsNames() {
        return new String[]{"PacketPlayOutTabComplete"};
    }

    @SneakyThrows
    @Override
    public void out(Player player, Object packet) {
        Class<?> packetPlayOutTabCompleteClass = NMSUtil.getNMSClass("PacketPlayOutTabComplete");
        Field suggestionsField = packetPlayOutTabCompleteClass.getDeclaredField("a");
        suggestionsField.setAccessible(true);

        Object suggestions = suggestionsField.get(packet);

        if (suggestions instanceof String[]) {
            String[] suggestionsArray = (String[]) suggestions;
            if (player.hasPermission(PermissionType.ANTITAB_BYPASS.getPerm())) return;

            for (String commands : Config.getList(ConfigType.CONFIG, "block-commands.commands-blocked")) {
                suggestionsArray = java.util.Arrays.stream(suggestionsArray)
                        .filter(suggestion -> !suggestion.equals(commands))
                        .toArray(String[]::new);

            }
            suggestionsField.set(packet, suggestionsArray);
        }
    }


    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.CONFIG, "anti-tab");
    }
}
