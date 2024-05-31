package com.xg7network.xg7lobby;

import com.xg7network.xg7lobby.config.ConfigManager;
import com.xg7network.xg7lobby.config.ConfigType;
import com.xg7network.xg7lobby.data.PlayersManager;
import com.xg7network.xg7lobby.events.playerevents.Handler.PlayerManager;
import com.xg7network.xg7lobby.inventories.managers.InventoryManager;
import com.xg7network.xg7lobby.utils.PrivateInforations.Metrics;
import com.xg7network.xg7lobby.utils.Other.PlaceHolder;
import com.xg7network.xg7lobby.utils.PrivateInforations.VerfVersion;
import com.xg7network.xg7lobby.utils.Text.TextUtil;
import com.xg7network.xg7menus.API.Inventory.Manager.Managers.MenuManager;
import com.xg7network.xg7menus.API.Inventory.Manager.MenuManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class XG7Lobby extends JavaPlugin {


    @Getter
    private static XG7Lobby plugin;
    public static String prefix;

    @Override
    public void onEnable() {

        /////////////////////////////////////////////////////////////////////////////////////////////

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
            new PlaceHolder().register();
        }

        plugin = this;

        Metrics metrics = Metrics.getMetrics(this);

        ///////////////////////////////////////////////////////////////////////////////////

        this.getServer().getConsoleSender().sendMessage("Loading files:");

        try {
            ConfigManager.load();
            InventoryManager.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        prefix = TextUtil.get(ConfigManager.getConfig(ConfigType.CONFIG).getString("prefix"));

        this.getServer().getConsoleSender().sendMessage("Plugin prefix: " + prefix);

        VerfVersion.verifyUpdate();

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading player data:");

        PlayersManager.connect();

        MenuManager.inicialize(this);

        PlayerManager.init();



        this.getServer().getConsoleSender().sendMessage(prefix + "Loading events:");


        this.getServer().getConsoleSender().sendMessage(prefix + "Loading commands:");


        this.getServer().getConsoleSender().sendMessage(prefix + "Loaded!");
    }


    @Override
    public void onDisable() {
        try {
            PlayersManager.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Plugin shutdown logic
    }


}
