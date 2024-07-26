package com.xg7plugins.xg7lobby;

import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.commands.CommandManager;
import com.xg7plugins.xg7lobby.commands.implcommands.HelpCommand;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.menus.SelectorManager;
import com.xg7plugins.xg7lobby.scores.Bossbar;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.utils.Log;
import com.xg7plugins.xg7lobby.utils.Metrics;
import com.xg7plugins.xg7lobby.utils.PacketEvents;
import com.xg7plugins.xg7lobby.utils.Placeholders;
import com.xg7plugins.xg7menus.api.XG7Menus;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class XG7Lobby extends JavaPlugin {

    @Getter
    private static XG7Lobby plugin;


    public void onEnable() {
        plugin = this;
        Metrics metrics = Metrics.getMetrics(this);

        this.getServer().getConsoleSender().sendMessage("Loading...");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "__   __  ___   ______     " + ChatColor.DARK_AQUA + "_       ____    ____ " + ChatColor.AQUA + "  ____ __   __");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "\\ \\ / / / __| |___   /   " + ChatColor.DARK_AQUA + "| |     / __ \\  | __ ) " + ChatColor.AQUA + "| __ )\\ \\ / /");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " \\ v / | |  _     / /    " + ChatColor.DARK_AQUA + "| |    | | | |  | \\ \\\\" + ChatColor.AQUA + " | \\ \\\\ \\ V /");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " / . \\ | |_| |   / /     " + ChatColor.DARK_AQUA + "| |___ | |_| |  | |_) |" + ChatColor.AQUA + "| |_) | | |  ");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "/_/ \\_\\ \\____|  /_/      " + ChatColor.DARK_AQUA + "|_____| \\____/  |____/ " + ChatColor.AQUA + "|____/  |_|");

        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (ClassNotFoundException var4) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "                       SPIGOT NOT DETECTED                     ");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "THIS PLUGIN NEEDS SPIGOT TO WORK!                              ");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DOWNLOAD HERE: https://www.spigotmc.org/wiki/spigot-installation/.");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "THE PLUGIN WILL DISABLE!                                         ");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "It's recommended to install PlaceholderAPI");
            this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "to get more resourses!");
        } else {
            new Placeholders().register();
        }
        Log.setEnabled(XG7Lobby.getPlugin().getConfig().getBoolean("debug"));

        if (Log.isEnabled) Log.warn("DEBUG is enabled, to disable go on config.yml");

        Log.loading("Loading the plugin..");

        XG7Menus.inicialize(this);

        Log.loading("Loading configuration and data...");
        Config.load();
        SQLHandler.connect();

        Log.loading("Loading cache...");
        CacheManager.init();

        Log.loading("Loading events...");
        new EventManager().init();
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) <= 13) EventManager.initPacketEvents();

        Log.loading("Loading commands...");
        new CommandManager().init();
        HelpCommand.init();
        CommandManager.initCustomCommands();

        Log.loading("Loading Tasks...");
        TaskManager.initTimerTasks();

        Log.loading("Loaded!");

    }


    @Override
    public void onDisable() {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) <= 13)Bukkit.getOnlinePlayers().forEach(PacketEvents::stopEvent);
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 9) Bukkit.getOnlinePlayers().forEach(Bossbar::removePlayer);
        if (Config.getBoolean(ConfigType.SELECTOR, "enabled")) Bukkit.getOnlinePlayers().forEach(SelectorManager.getMenu()::close);
    }


}
