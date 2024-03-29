package com.xg7network.xg7lobby;

import com.xg7network.xg7lobby.DefautCommands.HelpCommand.HelpCommand;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Build;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Lobby;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Setlobby;
import com.xg7network.xg7lobby.DefautCommands.Moderation.*;
import com.xg7network.xg7lobby.DefautCommands.Others.*;
import com.xg7network.xg7lobby.DefautCommands.Others.Warns;
import com.xg7network.xg7lobby.DefautCommands.TabCompleter;
import com.xg7network.xg7lobby.Configs.ConfigManager;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.DefautCommands.ToCreator;
import com.xg7network.xg7lobby.Module.Chat.AntiSpam;
import com.xg7network.xg7lobby.Module.Chat.Chat;
import com.xg7network.xg7lobby.Module.Chat.CustomCommands.CommandManager;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Others.*;
import com.xg7network.xg7lobby.Module.Events.Jumps.DoubleJump;
import com.xg7network.xg7lobby.Module.Events.Jumps.Fly;
import com.xg7network.xg7lobby.Module.Events.Jumps.FlyManager;
import com.xg7network.xg7lobby.Module.Events.Jumps.LaunchPad;
import com.xg7network.xg7lobby.Module.Events.Ping;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction.DropPickup;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction.OnBuild;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Others.Void;
import com.xg7network.xg7lobby.Module.Events.WorldEvents.Blocks;
import com.xg7network.xg7lobby.Module.Events.WorldEvents.Cycles;
import com.xg7network.xg7lobby.Module.ModuleManager;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Module.Scores.ScoresManager;
import com.xg7network.xg7lobby.Module.Inventories.SelectorManager;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.CustomInventories.InventoryLoader;
import com.xg7network.xg7lobby.Utils.PrivateInforations.Metrics;
import com.xg7network.xg7lobby.Utils.Other.PlaceHolder;
import com.xg7network.xg7lobby.Utils.PrivateInforations.VerfVersion;
import com.xg7network.xg7lobby.Utils.Text.TextUtil;
import com.xg7network.xg7menus.API.Inventory.Manager.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public final class XG7Lobby extends JavaPlugin {




    public static ConfigManager configManager;

    public static boolean placeholderapi = false;

    private static XG7Lobby plugin;

    private ModuleManager moduleManager;


    public static String prefix;

    @Override
    public void onEnable() {

        this.reloadConfig();

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

        }

        placeholderapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        if (placeholderapi) {
            new PlaceHolder().register();
        }


        plugin = this;

        Metrics metrics = Metrics.getMetrics(this);
        try {
            InventoryLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ///////////////////////////////////////////////////////////////////////////////////

        this.getServer().getConsoleSender().sendMessage("Loading files:");

        configManager = new ConfigManager();
        configManager.loadConfig(ConfigType.CONFIG);
        configManager.loadConfig(ConfigType.MESSAGES);
        configManager.loadConfig(ConfigType.DATA);
        configManager.loadConfig(ConfigType.SELECTORS);

        moduleManager = new ModuleManager(this);
        moduleManager.loadModules();

        prefix = TextUtil.get(configManager.getConfig(ConfigType.CONFIG).getString("prefix"));

        this.getServer().getConsoleSender().sendMessage("Plugin prefix: " + prefix);

        VerfVersion.verifyUpdate();


        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading player data:");

        PlayersManager.connect();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PlayersManager.getDatas().isEmpty()) PlayersManager.createData(player);
            else if (PlayersManager.getData(player.getUniqueId().toString()) == null) {
                PlayersManager.createData(player).setFirstJoin(System.currentTimeMillis());
            }
        }

        MenuManager.inicialize(this);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////


        this.getServer().getConsoleSender().sendMessage(prefix + "Loading events:");

        this.getServer().getPluginManager().registerEvents(new Players(this), this);
        this.getServer().getPluginManager().registerEvents(new JoinAndQuit(), this);
        this.getServer().getPluginManager().registerEvents(new ScoresManager(this), this);
        this.getServer().getPluginManager().registerEvents(new FlyManager(this), this);
        this.getServer().getPluginManager().registerEvents(new DoubleJump(), this);
        this.getServer().getPluginManager().registerEvents(new LaunchPad(), this);
        this.getServer().getPluginManager().registerEvents(new Ping(), this);
        this.getServer().getPluginManager().registerEvents(new OnBuild(), this);
        this.getServer().getPluginManager().registerEvents(new SelectorManager(this), this);
        this.getServer().getPluginManager().registerEvents(new Mute(), this);
        this.getServer().getPluginManager().registerEvents(new DropPickup(), this);
        this.getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new Hunger(), this);
        this.getServer().getPluginManager().registerEvents(new Void(), this);
        this.getServer().getPluginManager().registerEvents(new AntiSpam(this), this);
        this.getServer().getPluginManager().registerEvents(new Chat(), this);
        this.getServer().getPluginManager().registerEvents(new Blocks(), this);
        this.getServer().getPluginManager().registerEvents(new Warns(), this);
        this.getServer().getPluginManager().registerEvents(new Cycles(), this);
        this.getServer().getPluginManager().registerEvents(new LockChatCommand(), this);
        this.getServer().getPluginManager().registerEvents(new GeneralEvents(), this);
        this.getServer().getPluginManager().registerEvents(new Lobby(), this);
        this.getServer().getPluginManager().registerEvents(new Vanish(), this);




        this.getServer().getConsoleSender().sendMessage(prefix + "Loading commands:");

        this.getCommand("xg7lobbymute").setExecutor(new Mute());
        this.getCommand("xg7lobbyunmute").setExecutor(new Mute());
        this.getCommand("xg7lobbytempmute").setExecutor(new Mute());
        this.getCommand("xg7lobbysetlobby").setExecutor(new Setlobby());
        this.getCommand("xg7lobbylobby").setExecutor(new Lobby());
        this.getCommand("xg7lobbyfly").setExecutor(new Fly());
        this.getCommand("xg7lobbybuild").setExecutor(new Build());
        this.getCommand("xg7lobbykick").setExecutor(new Kick());
        this.getCommand("xg7lobbyban").setExecutor(new Ban());
        this.getCommand("xg7lobbytempban").setExecutor(new TempBan());
        this.getCommand("xg7lobbyunban").setExecutor(new Ban());
        this.getCommand("xg7lobbygui").setExecutor(new GUI());
        this.getCommand("xg7lobbylockchat").setExecutor(new LockChatCommand());
        this.getCommand("xg7lobbygma").setExecutor(new Gamemode());
        this.getCommand("xg7lobbygmc").setExecutor(new Gamemode());
        this.getCommand("xg7lobbygms").setExecutor(new Gamemode());
        this.getCommand("xg7lobbygmsp").setExecutor(new Gamemode());
        this.getCommand("xg7lobbywarn").setExecutor(new Warn());
        this.getCommand("xg7lobbywarns").setExecutor(new Warns());
        this.getCommand("xg7lobbyhelp").setExecutor(new HelpCommand());
        this.getCommand("xg7lobbyvanish").setExecutor(new Vanish());
        this.getCommand("xg7lobbyreloadconfig").setExecutor(new ReloadConfigCommand());
        this.getCommand("xg7lobbyreportbug").setExecutor(new ToCreator());
        this.getCommand("xg7lobbysuggest").setExecutor(new ToCreator());


        this.getCommand("xg7lobbygma").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbygmc").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbygms").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbygmsp").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbytempmute").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbymute").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbyunmute").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbykick").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbyban").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbyunban").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbyfly").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbysetlobby").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbygui").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbyreportbug").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbysuggest").setTabCompleter(new TabCompleter());

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading custom commands:");
        CommandManager.registerCommands();

        this.getServer().getConsoleSender().sendMessage(prefix + "Loaded!");
    }


    @Override
    public void onDisable() {
        moduleManager.unloadModules();
        try {
            PlayersManager.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Plugin shutdown logic
    }



    public static XG7Lobby getPlugin() {
        return plugin;
    }
}
