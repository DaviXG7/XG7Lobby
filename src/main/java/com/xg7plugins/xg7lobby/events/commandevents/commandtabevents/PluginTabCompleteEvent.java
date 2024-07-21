package com.xg7plugins.xg7lobby.events.commandevents.commandtabevents;

import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.CommandManager;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.events.PacketPlayEvent;
import com.xg7plugins.xg7lobby.utils.NMSUtil;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PluginTabCompleteEvent implements PacketPlayEvent.PacketPlayOutEvent {
    @Override
    public String[] getPacketsNames() {
        return new String[]{"PacketPlayOutTabComplete"};
    }

    @SneakyThrows
    @Override
    public Object out(Player player, Object packet) {
        Class<?> packetPlayOutTabCompleteClass = NMSUtil.getNMSClass("PacketPlayOutTabComplete");
        Field suggestionsField = packetPlayOutTabCompleteClass.getDeclaredField("a");
        suggestionsField.setAccessible(true);

        Object suggestions = suggestionsField.get(packet);

        if (suggestions instanceof String[]) {
            String[] suggestionsArray = (String[]) suggestions;
            if (player.hasPermission(PermissionType.ANTITAB_PLUGIN_BYPASS.getPerm())) return packet;

            for (String commands : CommandManager.getCommands().stream().flatMap(cmd -> {
                        List<String> combined = new ArrayList<>();
                        combined.add("/" + cmd.getName());
                        combined.addAll(cmd.getAliasses().stream().map(alias -> "/" + alias).collect(Collectors.toList()));
                        return combined.stream();
                    })
                    .collect(Collectors.toList()))
            {
                suggestionsArray = java.util.Arrays.stream(suggestionsArray)
                        .filter(suggestion -> !suggestion.startsWith(commands))
                        .toArray(String[]::new);

            }
            suggestionsField.set(packet, suggestionsArray);
        }
        return packet;
    }


    @Override
    public boolean isEnabled() {
        return Config.getBoolean(ConfigType.COMMANDS, "anti-tab");
    }
}
