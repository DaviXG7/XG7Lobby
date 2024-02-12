package com.xg7network.xg7lobby.Module.Chat.CustomCommands;

import com.xg7network.xg7lobby.Configs.ConfigType;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class CommandManager {
    public static void registerCommands() {

        for (String s : configManager.getConfig(ConfigType.CONFIG).getConfigurationSection("custom-commands").getKeys(false)) {
            new Command(s, configManager.getConfig(ConfigType.CONFIG).getString("custom-commands." + s + ".description"), configManager.getConfig(ConfigType.CONFIG).getStringList("custom-commands." + s + "aliases"), configManager.getConfig(ConfigType.CONFIG).getString("custom-commands." + s + "permission"), configManager.getConfig(ConfigType.CONFIG).getStringList("custom-commands." + s + ".actions"));
        }

    }
}
