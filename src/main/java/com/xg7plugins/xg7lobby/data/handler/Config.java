package com.xg7plugins.xg7lobby.data.handler;

import com.xg7plugins.xg7lobby.Enums.ConfigType;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.utils.Log;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Config {

    private static final HashMap<ConfigType, FileConfiguration> configs = new HashMap<>();
    private static final HashMap<String, FileConfiguration> menus = new HashMap<>();

    public static void load() {

        Log.info("Loading config files...");
        File config = new File(XG7Lobby.getPlugin().getDataFolder(), "config.yml");
        File commands = new File(XG7Lobby.getPlugin().getDataFolder(), "commands.yml");
        File selectors = new File(XG7Lobby.getPlugin().getDataFolder(), "selectors.yml");
        File data = new File(XG7Lobby.getPlugin().getDataFolder(), "data.yml");
        File messages = new File(XG7Lobby.getPlugin().getDataFolder(), "messages.yml");

        if (!config.exists()) XG7Lobby.getPlugin().saveResource("config.yml", false);
        if (!commands.exists()) XG7Lobby.getPlugin().saveResource("commands.yml", false);
        if (!selectors.exists()) XG7Lobby.getPlugin().saveResource("selectors.yml", false);
        if (!data.exists()) XG7Lobby.getPlugin().saveResource("data.yml", false);
        if (!messages.exists()) XG7Lobby.getPlugin().saveResource("messages.yml", false);

        configs.put(ConfigType.CONFIG, YamlConfiguration.loadConfiguration(config));
        configs.put(ConfigType.COMMANDS, YamlConfiguration.loadConfiguration(commands));
        configs.put(ConfigType.SELECTORS, YamlConfiguration.loadConfiguration(selectors));
        configs.put(ConfigType.DATA, YamlConfiguration.loadConfiguration(data));
        configs.put(ConfigType.MESSAGES, YamlConfiguration.loadConfiguration(messages));

        Log.fine("Config loaded!");

        loadMenu();
    }

    public static void loadMenu() {
        Log.info("Loading menu files...");
        File menuFolder = new File(XG7Lobby.getPlugin().getDataFolder(), "menus");

        if (!menuFolder.exists()) {
            menuFolder.mkdirs();
        }
        for (File file : Objects.requireNonNull(menuFolder.getAbsoluteFile().listFiles())) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            menus.put(configuration.getString("id"), configuration);
        }
        Log.fine("Menus loaded!");
    }

    public static void load(ConfigType type) {
        Log.info("Loading config " + type.getConfig() + "...");
        File file = new File(XG7Lobby.getPlugin().getDataFolder(), type.getConfig());
        if (!file.exists()) XG7Lobby.getPlugin().saveResource(type.getConfig(), false);
        configs.put(type, YamlConfiguration.loadConfiguration(file));
        Log.fine("Loaded!");

    }

    public static Object get(ConfigType type, String path) {
        return configs.get(type).get(path);
    }

    public static int getInt(ConfigType type, String path) {
        return configs.get(type).getInt(path);
    }

    public static String getString(ConfigType type, String path) {
        return configs.get(type).getString(path);
    }

    public static boolean getBoolean(ConfigType type, String path) {
        return configs.get(type).getBoolean(path);
    }

    public static long getLong(ConfigType type, String path) {
        return configs.get(type).getLong(path);
    }

    public static double getDouble(ConfigType type, String path) {
        return configs.get(type).getDouble(path);
    }

    public static List<String> getList(ConfigType type, String path) {
        return configs.get(type).getStringList(path);
    }

    public static ConfigurationSection getConfigurationSections(ConfigType type, String path) {
        return configs.get(type).getConfigurationSection(path);
    }

    public static void set(ConfigType type, String path, Object value) {
        configs.get(type).set(path, value);
    }


    public static void reload() {
        Log.info("Reloading configs...");
        save();
        load();
        Log.fine("Successful reloaded!");
    }

    public static void reload(ConfigType type) {
        Log.info("Reloading config " + type.getConfig() + "...");
        save(type);
        load(type);
        Log.fine("Loaded!");
    }

    public static void reloadMenus() {
        loadMenu();
    }

    @SneakyThrows
    public static void save() {
        Log.info("Saving config files....");
        for (Map.Entry<ConfigType, FileConfiguration> config : configs.entrySet()) {
            config.getValue().save(new File(XG7Lobby.getPlugin().getDataFolder(), config.getKey().getConfig()));
        }
        Log.fine("Successful saved!");
    }

    @SneakyThrows
    public static void save(ConfigType configType) {
        Log.info("Saving config " + configType.getConfig() + "...");
        configs.get(configType).save(new File(XG7Lobby.getPlugin().getDataFolder(), configType.getConfig()));
        Log.fine("Loaded!");
    }


}
