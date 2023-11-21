package com.xg7network.xg7lobby;

import com.xg7network.xg7lobby.DefautCommands.Lobby.Build;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Lobby;
import com.xg7network.xg7lobby.DefautCommands.Lobby.Setlobby;
import com.xg7network.xg7lobby.DefautCommands.Moderation.Kick;
import com.xg7network.xg7lobby.DefautCommands.TabCompleter;
import com.xg7network.xg7lobby.DefautCommands.test;
import com.xg7network.xg7lobby.Configs.ConfigManager;
import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Module.Events.JoinAndQuit;
import com.xg7network.xg7lobby.Module.Events.Jumps.DoubleJump;
import com.xg7network.xg7lobby.Module.Events.Jumps.Fly;
import com.xg7network.xg7lobby.Module.Events.Jumps.FlyManager;
import com.xg7network.xg7lobby.Module.Events.Jumps.LaunchPad;
import com.xg7network.xg7lobby.Module.Events.Ping;
import com.xg7network.xg7lobby.Module.Events.PlayerEvents.Interaction.OnBuild;
import com.xg7network.xg7lobby.Module.ModuleManager;
import com.xg7network.xg7lobby.Module.Players;
import com.xg7network.xg7lobby.Module.Scores.ScoresManager;
import com.xg7network.xg7lobby.Module.Selectors.SelectorListener;
import com.xg7network.xg7lobby.Module.Selectors.SelectorManager;
import com.xg7network.xg7lobby.Utils.Inventory.InventoryListener;
import com.xg7network.xg7lobby.Utils.Sql.SqlConnect;
import com.xg7network.xg7lobby.Utils.Sql.SqlCreate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class XG7Lobby extends JavaPlugin {

    /*


     * Básico SQL:

     * Pegar informações:

     PreparedStatement ps = sqlConnect.getConnection().prepareStatement("");
     ResultSet rs = ps.executeQuery();

     while (rs.next()) {
     String result = rs.getString("coluna");
     }


     *Executar:

     PreparedStatement ps = sqlConnect.getConnection().prepareStatement("");
     ps.executeUpdate();



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
    public static boolean xg7chat = false;

    private SqlConnect sqlConnect;


    public static boolean connected = false;
    
    private ModuleManager moduleManager;

    private SqlCreate sqlCreate;

    public static String prefix = ChatColor.BLUE + "[XG7 " + ChatColor.DARK_AQUA + "Lob" + ChatColor.AQUA + "by] " + ChatColor.RESET;

    @Override
    public void onEnable() {

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

        if (Bukkit.getPluginManager().getPlugin("XG7Chat") == null) {
            this.getServer().getConsoleSender().sendMessage(prefix + "To get more features in XG7Lobby it's recommended to install XG7Chat!");
        }

        xg7chat = Bukkit.getPluginManager().getPlugin("XG7Chat") != null;

        placeholderapi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        ///////////////////////////////////////////////////////////////////////////////////

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading files:");

        configManager = new ConfigManager(this);
        configManager.loadConfig(ConfigType.CONFIG);
        configManager.loadConfig(ConfigType.MESSAGES);
        configManager.loadConfig(ConfigType.DATA);
        configManager.loadConfig(ConfigType.SELECTORS);
        
        moduleManager = new ModuleManager(this);
        moduleManager.loadModules();

        this.getServer().getConsoleSender().sendMessage(prefix + "Connecting to database:");

        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("sql.enabled")) {

            sqlConnect = new SqlConnect();

            try {
                sqlConnect.connect();
                this.getServer().getConsoleSender().sendMessage(prefix + "§6SQL: §rConnected successfully!");
                sqlCreate = new SqlCreate(sqlConnect);
                sqlCreate.createLobby();
                connected = true;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            this.getServer().getConsoleSender().sendMessage(prefix + "§6SQL: §rDatabase is disabled, continuing to load...");
        }


        this.getServer().getConsoleSender().sendMessage(prefix + "Loading events:");

        this.getServer().getPluginManager().registerEvents(new Players(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new JoinAndQuit(this), this);
        this.getServer().getPluginManager().registerEvents(new ScoresManager(this), this);
        this.getServer().getPluginManager().registerEvents(new FlyManager(this), this);
        this.getServer().getPluginManager().registerEvents(new DoubleJump(), this);
        this.getServer().getPluginManager().registerEvents(new LaunchPad(), this);
        this.getServer().getPluginManager().registerEvents(new Ping(), this);
        this.getServer().getPluginManager().registerEvents(new OnBuild(), this);
        this.getServer().getPluginManager().registerEvents(new SelectorManager(this), this);
        this.getServer().getPluginManager().registerEvents(new SelectorListener(), this);

        this.getServer().getConsoleSender().sendMessage(prefix + "Loading commands:");

        this.getCommand("execute").setExecutor(new test());
        this.getCommand("xg7lobbysetlobby").setExecutor(new Setlobby(this));
        this.getCommand("xg7lobbylobby").setExecutor(new Lobby(this));
        this.getCommand("xg7lobbyfly").setExecutor(new Fly());
        this.getCommand("xg7lobbybuild").setExecutor(new Build());
        this.getCommand("xg7lobbykick").setExecutor(new Kick());

        this.getCommand("xg7lobbykick").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbyfly").setTabCompleter(new TabCompleter());
        this.getCommand("xg7lobbysetlobby").setTabCompleter(new TabCompleter());


        this.getServer().getConsoleSender().sendMessage(prefix + "Loaded!");
    }

    @Override
    public void onDisable() {
        moduleManager.unloadModules();
        if (connected) {
            sqlConnect.disconnect();
        }
        // Plugin shutdown logic
    }

    public SqlConnect getSqlConnect() {
        return this.sqlConnect;
    }
}
