package br.com.xg7network.xg7lobby;

import br.com.xg7network.xg7lobby.Comandos.HelpCommand;
import br.com.xg7network.xg7lobby.Comandos.Lobby.Fly;
import br.com.xg7network.xg7lobby.Comandos.Lobby.Lobby;
import br.com.xg7network.xg7lobby.Comandos.Lobby.VanishComand;
import br.com.xg7network.xg7lobby.Comandos.Lobby.setLobby;
import br.com.xg7network.xg7lobby.Comandos.Moderação.*;
import br.com.xg7network.xg7lobby.Comandos.Player.AdventureCommand;
import br.com.xg7network.xg7lobby.Comandos.Player.CreativeCommand;
import br.com.xg7network.xg7lobby.Comandos.Player.SpectatorCommand;
import br.com.xg7network.xg7lobby.Comandos.Player.SurvivalCommand;
import br.com.xg7network.xg7lobby.Comandos.Reload;
import br.com.xg7network.xg7lobby.Configs.DataManager;
import br.com.xg7network.xg7lobby.Configs.MensagemManager;
import br.com.xg7network.xg7lobby.Configs.ScoreManager;
import br.com.xg7network.xg7lobby.Configs.SeletorManager;
import br.com.xg7network.xg7lobby.Eventos.BlockEvent.BlockBreakEvent;
import br.com.xg7network.xg7lobby.Eventos.BlockEvent.BlockInteractEvent;
import br.com.xg7network.xg7lobby.Eventos.BlockEvent.BlockPlaceEvent;
import br.com.xg7network.xg7lobby.Eventos.Chat.ModeracaoChat;
import br.com.xg7network.xg7lobby.Eventos.JoinEvent.Mensagem;
import br.com.xg7network.xg7lobby.Eventos.JoinEvent.TpJoinLobby;
import br.com.xg7network.xg7lobby.Eventos.PlayerConfigs.*;
import br.com.xg7network.xg7lobby.Eventos.WorldConfigs.*;
import br.com.xg7network.xg7lobby.Launchpad.UnderBlockEvent;
import br.com.xg7network.xg7lobby.MOTD.PingEvent;
import br.com.xg7network.xg7lobby.Modulo.ModuleManager;
import br.com.xg7network.xg7lobby.Modulo.Seletores.Hotbar.HidePlayers;
import br.com.xg7network.xg7lobby.Modulo.Seletores.InventoryManager;

import br.com.xg7network.xg7lobby.Modulo.Seletores.Seletores.SPListener;
import br.com.xg7network.xg7lobby.Utilidades.ActionBar;
import br.com.xg7network.xg7lobby.Utilidades.CentralizarTexto;
import br.com.xg7network.xg7lobby.Utilidades.ConfigDefaults;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class XG7Lobby extends JavaPlugin {

    public static MensagemManager mensagem = new MensagemManager();
    public static ScoreManager score = new ScoreManager();

    public static DataManager data = new DataManager();
    public static SeletorManager seletor = new SeletorManager();

    public static ActionBar action;
    public static CentralizarTexto centralizar;
    public static ConfigDefaults configDef;

    public ModuleManager moduleManager;
    String prefix = ChatColor.BLUE + "[XG7 " + ChatColor.DARK_AQUA + "Lob" + ChatColor.AQUA + "by] " + ChatColor.RESET;


    @Override
    public void onEnable() {

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando...");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "__   __  ___   ______     "   + ChatColor.DARK_AQUA + "_       ____    ____ "     + ChatColor.AQUA + "  ____ __   __");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "\\ \\ / / / __| |___   /   "  + ChatColor.DARK_AQUA + "| |     / __ \\  | __ ) "  + ChatColor.AQUA + "| __ )\\ \\ / /");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " \\ v / | |  _     / /    "   + ChatColor.DARK_AQUA + "| |    | | | |  | \\ \\\\" + ChatColor.AQUA + " | \\ \\\\ \\ V /");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + " / . \\ | |_| |   / /     "   + ChatColor.DARK_AQUA + "| |___ | |_| |  | |_) |"   + ChatColor.AQUA + "| |_) | | |  ");
        this.getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "/_/ \\_\\ \\____|  /_/      " + ChatColor.DARK_AQUA + "|_____| \\____/  |____/ "  + ChatColor.AQUA + "|____/  |_|");

        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (ClassNotFoundException var4) {
            this.getServer().getConsoleSender().sendMessage("                       SPIGOT NÃO DETECTADO                    ");
            this.getServer().getConsoleSender().sendMessage("ESTE PLUGIN PRECISA DO SPIGOT PARA FUNCIONAR!                  ");
            this.getServer().getConsoleSender().sendMessage("BAIXE AQUI: https://www.spigotmc.org/wiki/spigot-installation/.");
            this.getServer().getConsoleSender().sendMessage("O PLUGIN IRÁ DESLIGAR!                                         ");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando configs...");

        action = new ActionBar();
        this.centralizar = new CentralizarTexto();
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        reloadConfig();

        if (mensagem.messageFile == null) {
            mensagem.messageFile = new File(this.getDataFolder(), "mensagens.yml");
        }
        if (!mensagem.messageFile.exists()) {
            this.saveResource("mensagens.yml", false);
        }
        if (score.scoreFile == null) {
            score.scoreFile = new File(this.getDataFolder(), "scores.yml");
        }
        if (!score.scoreFile.exists()) {
            this.saveResource("scores.yml", false);
        }
        if (data.dataFile == null) {
            data.dataFile = new File(this.getDataFolder(), "data.yml");
        }
        if (!data.dataFile.exists()) {
            this.saveResource("data.yml", false);
        }

        if (seletor.seletorFile == null) {
            seletor.seletorFile = new File(this.getDataFolder(), "seletores.yml");
        }
        if (!seletor.seletorFile.exists()) {
            this.saveResource("seletores.yml", false);
        }

        this.getServer().getConsoleSender().sendMessage(prefix + "Configurando de acordo com a versão...");
        this.getServer().getConsoleSender().sendMessage(prefix + "Sua versão é: " + Bukkit.getServer().getVersion());
        this.configDef = new ConfigDefaults(this);



        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "[XG7 Lobby] O PlaceholderAPI não foi encontrado! " +
                    "Se quiser mêcanicas como colocar nome de players," +
                    " baixe ele aqui: " + ChatColor.GOLD + "https://www.spigotmc.org/resources/placeholderapi.6245/");
        }

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Mensagens de entrar e de saída...");
        this.getServer().getPluginManager().registerEvents(new Mensagem(), this);
        this.getServer().getPluginManager().registerEvents(new br.com.xg7network.xg7lobby.Eventos.QuitEvent.Mensagem(), this);


        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando tp para o lobby...");
        this.getServer().getPluginManager().registerEvents(new TpJoinLobby(this), this);

        this.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[XG7 Lobby] Carregando Módulos...");
        this.moduleManager = new ModuleManager(this);
        this.moduleManager.loadModules();

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Eventos de blocos...");
        this.getServer().getPluginManager().registerEvents(new BlockBreakEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockInteractEvent(this), this);

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Eventos de Chat...");
        this.getServer().getPluginManager().registerEvents(new ModeracaoChat(this), this);
        this.getServer().getPluginManager().registerEvents(new Mute(), this);

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Configurações de mundos...");
        this.getServer().getPluginManager().registerEvents(new SpawnMobs(this), this);
        this.getServer().getPluginManager().registerEvents(new CancelWeather(this), this);
        this.getServer().getPluginManager().registerEvents(new Time(this), this);
        this.getServer().getPluginManager().registerEvents(new CancelBlockBurn(this), this);
        this.getServer().getPluginManager().registerEvents(new CancelLeavesDecay(this), this);

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Configurações de jogadores...");
        this.getServer().getPluginManager().registerEvents(new PlayerHungerEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayeronVoid(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDropPickupEvent(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerAttackEvent(this), this);


        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Launchpad...");
        this.getServer().getPluginManager().registerEvents(new UnderBlockEvent(this), this);

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando MOTD...");
        this.getServer().getPluginManager().registerEvents(new PingEvent(this), this);

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Comandos...");
        this.getCommand("xg7lsetlobby").setExecutor(new setLobby(this));
        this.getCommand("xg7llobby").setExecutor(new Lobby());
        this.getCommand("xg7lfly").setExecutor(new Fly());
        this.getCommand("xg7lkick").setExecutor(new Kick());
        this.getCommand("xg7lban").setExecutor(new Ban());
        this.getCommand("xg7lunban").setExecutor(new Unban());
        this.getCommand("xg7ltempban").setExecutor(new Tempban());
        this.getCommand("xg7lmute").setExecutor(new Mute());
        this.getCommand("xg7lunmute").setExecutor(new Unmute());
        this.getCommand("xg7lreload").setExecutor(new Reload(this));
        this.getCommand("xg7lgmcreative").setExecutor(new CreativeCommand());
        this.getCommand("xg7lgmsurvival").setExecutor(new SurvivalCommand());
        this.getCommand("xg7lgmadventure").setExecutor(new AdventureCommand());
        this.getCommand("xg7lgmspectator").setExecutor(new SpectatorCommand());
        this.getCommand("xg7lvanish").setExecutor(new VanishComand(this));
        this.getCommand("xg7lajuda").setExecutor(new HelpCommand());

        this.getServer().getConsoleSender().sendMessage(prefix + "Carregando Seletores...");
        this.getServer().getPluginManager().registerEvents(new InventoryManager(this), this);
        this.getServer().getPluginManager().registerEvents(new HidePlayers(this), this);
        this.getServer().getPluginManager().registerEvents(new SPListener(this), this);

    }

    @Override
    public void onDisable() {

        this.moduleManager.unloadModules();

        Bukkit.getConsoleSender().sendMessage(prefix + "O Plugin foi desligado!");
    }

}
