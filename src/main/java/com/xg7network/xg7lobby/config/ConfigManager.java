package com.xg7network.xg7lobby.config;

import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ConfigManager {
    private static final HashMap<ConfigType, FileConfiguration> configs = new HashMap<>();
    public static void reload(ConfigType type) {
        configs.put(type, YamlConfiguration.loadConfiguration(new File(XG7Lobby.getPlugin().getDataFolder(), type.getFilePath())));
    }
    public static void load() throws IOException {

        XG7Lobby.getPlugin().saveResource("config.yml", false);
        XG7Lobby.getPlugin().saveResource("selector.yml", false);
        XG7Lobby.getPlugin().saveResource("data.yml", false);

        File config = new File(XG7Lobby.getPlugin().getDataFolder(), "config.yml");
        File selectors = new File(XG7Lobby.getPlugin().getDataFolder(), "selector.yml");
        File data = new File(XG7Lobby.getPlugin().getDataFolder(), "data.yml");

        XG7Lobby.getPlugin().saveResource("langs/en_us.yml", false);
        XG7Lobby.getPlugin().saveResource("langs/pt_br.yml", false);

        configs.put(ConfigType.CONFIG, YamlConfiguration.loadConfiguration(config));
        configs.put(ConfigType.DATA, YamlConfiguration.loadConfiguration(data));
        configs.put(ConfigType.SELECTORS, YamlConfiguration.loadConfiguration(selectors));

        ConfigType.MESSAGES.setMessageFilePath("langs/" + configs.get(ConfigType.CONFIG).getString("messages-lang"));

        File messages = new File(XG7Lobby.getPlugin().getDataFolder(), "langs/" + ConfigType.MESSAGES.getFilePath());
        configs.put(ConfigType.MESSAGES, YamlConfiguration.loadConfiguration(messages));

    }
    public static void save(ConfigType type) throws IOException {
        configs.get(type).save(new File(XG7Lobby.getPlugin().getDataFolder(), type.getFilePath()));
    }
    public static FileConfiguration getConfig(ConfigType type) {
        return configs.get(type);
    }

}
