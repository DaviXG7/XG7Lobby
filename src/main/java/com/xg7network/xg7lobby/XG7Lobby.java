package com.xg7network.xg7lobby;

import com.xg7network.xg7lobby.DefautCommands.Lobby.Build;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Lobby;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Setlobby;
import com.xg7network.xg7lobby.DefautCommands.Moderation.Ban;
import com.xg7network.xg7lobby.DefautCommands.Moderation.Kick;
import com.xg7network.xg7lobby.DefautCommands.Moderation.Mute;
import com.xg7network.xg7lobby.DefautCommands.Others.GUI;
import com.xg7network.xg7lobby.DefautCommands.Others.Gamemode;
import com.xg7network.xg7lobby.DefautCommands.Others.LockChatCommand;
import com.xg7network.xg7lobby.DefautCommands.Others.ReloadConfig;
import com.xg7network.xg7lobby.DefautCommands.TabCompleter;
import com.xg7network.xg7lobby.Configs.ConfigManager;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Module.Chat.AntiSpam;
import com.xg7network.xg7lobby.Module.Chat.Chat;
import com.xg7network.xg7lobby.Module.Events.JoinAndQuit;
import com.xg7network.xg7lobby.Module.Events.Jumps.DoubleJump;
import com.xg7network.xg7lobby.Module.Events.Jumps.Fly;
import com.xg7network.xg7lobby.Module.Events.Jumps.FlyManager;
import com.xg7network.xg7lobby.Module.Events.Jumps.LaunchPad;
import com.xg7network.xg7lobby.Module.Events.Ping;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction.DropPickup;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction.OnBuild;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Other.DamageEvent;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Other.Hunger;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Other.Void;
import com.xg7network.xg7lobby.Module.Events.WorldEvents.Blocks;
import com.xg7network.xg7lobby.Module.Events.WorldEvents.Cycles;
import com.xg7network.xg7lobby.Module.ModuleManager;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Module.Scores.ScoresManager;
import com.xg7network.xg7lobby.Module.Selectors.SelectorListener;
import com.xg7network.xg7lobby.Module.Selectors.SelectorManager;
import com.xg7network.xg7lobby.Player.PlayersManager;
import com.xg7network.xg7lobby.Utils.CustomInventories.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;

public final class XG7Lobby extends JavaPlugin {

    /*


     * Sistema simples de cooldown:

     private Cache<UUID, Long> cooldown = CacheBuilder.newBuilder().expireAfterWrite(configManager.getConfig(ConfigType.SELECTORS).getInt("selectors.cooldown"), TimeUnit.SECONDS).build();

     if (this.cooldown.asMap().containsKey(player.getUniqueId())) {
        new Message(configManager.getConfig(ConfigType.MESSAGES).getString("events.on-cooldown").replace("SECONDS", String.valueOf(TimeUnit.MILLISECONDS.toSeconds(cooldown.asMap().get(p.getUniqueId()) - System.currentTimeMillis()))), player).sendMessage();
     } else {
        this.cooldown.put(p.getUniqueId(), System.currentTimeMillis() + configManager.getConfig(ConfigType.SELECTORS).getLong("selectors.cooldown") * 1000L);
     }

     */




    public static ConfigManager configManager;

    public static boolean placeholderapi = false;

    private static XG7Lobby plugin;
    
    private ModuleManager moduleManager;


    public static String prefix = ChatColor.BLUE + "[XG7 " + ChatColor.DARK_AQUA + "Lob" + ChatColor.AQUA + "by] " + ChatColor.RESET;

    @Override
    public void onEnable() {

        this.reloadConfig();

        /////////////////////////////////////////////////////////////////////////////////////////////

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading...");
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
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {

            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "THIS PLUGIN NEEDS PROTOCOLLIB TO WORK FINE");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "DOWNLOAD HERE: https://www.spigotmc.org/resources/protocollib.1997/");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "THE PLUGIN WILL DISABLE!");

            this.getPluginLoader().disablePlugin(this);
            return;

        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {

            this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "It's recommended to install PlaceholderAPI");
            this.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "to get more resourses!");

        }

        placeholderapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        plugin = this;

        ///////////////////////////////////////////////////////////////////////////////////

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading files:");

        configManager = new ConfigManager();
        configManager.loadConfig(ConfigType.CONFIG);
        configManager.loadConfig(ConfigType.MESSAGES);
        configManager.loadConfig(ConfigType.DATA);
        configManager.loadConfig(ConfigType.SELECTORS);
        
        moduleManager = new ModuleManager(this);
        moduleManager.loadModules();


        /////////////////////////////////////////////////////////////////////////////////////////////////////////

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading player data:");

            PlayersManager.load();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (PlayersManager.players.isEmpty()) PlayersManager.createData(player);
                else
                    if (PlayersManager.getData(player.getUniqueId().toString()) == null) {
                        PlayersManager.createData(player).setFirstJoin(new Date(System.currentTimeMillis()));
                    }
            }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////


        this.getServer().getConsoleSender().sendMessage(prefix + "Loading events:");

        this.getServer().getPluginManager().registerEvents(new PlayersManager(), this);
        this.getServer().getPluginManager().registerEvents(new Players(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new JoinAndQuit(), this);
        this.getServer().getPluginManager().registerEvents(new ScoresManager(this), this);
        this.getServer().getPluginManager().registerEvents(new FlyManager(this), this);
        this.getServer().getPluginManager().registerEvents(new DoubleJump(), this);
        this.getServer().getPluginManager().registerEvents(new LaunchPad(), this);
        this.getServer().getPluginManager().registerEvents(new Ping(), this);
        this.getServer().getPluginManager().registerEvents(new OnBuild(), this);
        this.getServer().getPluginManager().registerEvents(new SelectorManager(this), this);
        this.getServer().getPluginManager().registerEvents(new SelectorListener(), this);
        this.getServer().getPluginManager().registerEvents(new Mute(), this);
        this.getServer().getPluginManager().registerEvents(new DropPickup(), this);
        this.getServer().getPluginManager().registerEvents(new DamageEvent(), this);
        this.getServer().getPluginManager().registerEvents(new Hunger(), this);
        this.getServer().getPluginManager().registerEvents(new Void(), this);
        this.getServer().getPluginManager().registerEvents(new AntiSpam(this), this);
        this.getServer().getPluginManager().registerEvents(new Chat(), this);
        this.getServer().getPluginManager().registerEvents(new Blocks(), this);
        new Cycles();





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
        this.getCommand("xg7lobbyunban").setExecutor(new Ban());
        this.getCommand("xg7lobbyreloadconfig").setExecutor(new ReloadConfig());
        this.getCommand("xg7lobbygui").setExecutor(new GUI());
        this.getCommand("xg7lobbylockchat").setExecutor(new LockChatCommand());
        this.getCommand("xg7lobbygma").setExecutor(new Gamemode());
        this.getCommand("xg7lobbygmc").setExecutor(new Gamemode());
        this.getCommand("xg7lobbygms").setExecutor(new Gamemode());
        this.getCommand("xg7lobbygmsp").setExecutor(new Gamemode());
        this.getCommand("xg7lobbywarns").setExecutor(new Gamemode());

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


        this.getServer().getConsoleSender().sendMessage(prefix + "Loaded!");
    }

    @Override
    public void onDisable() {
        moduleManager.unloadModules();
        // Plugin shutdown logic
    }

    public static XG7Lobby getPlugin() {
        return plugin;
    }
}
