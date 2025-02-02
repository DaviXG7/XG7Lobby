package com.xg7plugins.xg7lobby;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.boot.PluginConfigurations;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.PacketListener;
import com.xg7plugins.libs.xg7menus.menus.BaseMenu;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.tasks.Task;
import com.xg7plugins.xg7lobby.actions.ActionsProcessor;
import com.xg7plugins.xg7lobby.commands.*;
import com.xg7plugins.xg7lobby.commands.lobby.Lobby;
import com.xg7plugins.xg7lobby.commands.lobby.SetLobby;
import com.xg7plugins.xg7lobby.commands.moderation.KickCommand;
import com.xg7plugins.xg7lobby.commands.moderation.WarnCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.BanCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.BanIPCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.UnbanCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.UnbanIPCommand;
import com.xg7plugins.xg7lobby.commands.moderation.mute.MuteCommand;
import com.xg7plugins.xg7lobby.commands.moderation.mute.UnmuteCommand;
import com.xg7plugins.xg7lobby.events.*;
import com.xg7plugins.xg7lobby.events.air_events.FlyEvent;
import com.xg7plugins.xg7lobby.events.air_events.LaunchPadEvent;
import com.xg7plugins.xg7lobby.events.air_events.MultiJumpEvent;
import com.xg7plugins.xg7lobby.events.chat_events.*;
import com.xg7plugins.xg7lobby.events.defaults.DefaultPlayerEvents;
import com.xg7plugins.xg7lobby.events.defaults.DefaultWorldEvents;
import com.xg7plugins.xg7lobby.events.defaults.LoginAndLogoutEvents;
import com.xg7plugins.xg7lobby.inventories.InventoryManager;
import com.xg7plugins.xg7lobby.inventories.menu.LobbySelector;
import com.xg7plugins.xg7lobby.lobby.ServerInfo;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import com.xg7plugins.xg7lobby.lobby.location.LobbyManager;
import com.xg7plugins.xg7lobby.lobby.player.*;
import com.xg7plugins.xg7lobby.lobby.scores.*;
import com.xg7plugins.xg7lobby.repeating_tasks.AutoBroadcast;
import com.xg7plugins.xg7lobby.repeating_tasks.Effects;
import com.xg7plugins.xg7lobby.repeating_tasks.WorldCycles;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;

@Getter
@PluginConfigurations(
    prefix = "§1X§9G§37§bLobby§r",
        onEnableDraw = {
                "§1 __   §9_______ §3______ §b_           _     _           ",
                "§1 \\ \\ §9/ / ____§3|____  §b| |         | |   | |          ",
                "§1  \\ V §9/ |  __ §3   / /§b| |     ___ | |__ | |__  _   _ ",
                "§1   > <§9| | |_ |  §3/ / §b| |    / _ \\| '_ \\| '_ \\| | | |",
                "§1  / . §9\\ |__| | §3/ /  §b| |___| (_) | |_) | |_) | |_| |",
                "§1 /_/ \\_§9\\_____|§3/_/   §b|______\\___/|_.__/|_.__/ \\__, |",
                "§b                                              __/ |",
                "§b                                             |___/ "

        },
        mainCommandName = "xg7lobby",
        mainCommandAliases = {"7l", "xg7l"}

)
public final class XG7Lobby extends Plugin {

    private final ActionsProcessor actionsProcessor;
    private LobbyManager lobbyManager;
    private final PlayerDAO playerDAO;
    private final ServerInfo serverInfo;
    private InventoryManager inventoryManager;


    public XG7Lobby() {
        actionsProcessor = new ActionsProcessor();
        playerDAO = new PlayerDAO();
        serverInfo = new ServerInfo(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        lobbyManager = new LobbyManager(this);

        if (XG7Plugins.isPlaceholderAPI()) new XG7LobbyPlaceholderExpansion().register();

        getLog().loading("Loading custom menus...");
        inventoryManager = new InventoryManager(this, "games", "profile", "selector");

        getLog().loading("Loading action events...");
        loadActions();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getOnlinePlayers().forEach(player -> {

            LobbyPlayer.cast(player.getUniqueId(), false);

            LobbySelector menu = XG7Lobby.getInstance().getInventoryManager().getInventories().stream().filter(m -> m instanceof LobbySelector).map(m -> (LobbySelector) m).findFirst().orElse(null);

            if (menu != null) menu.open(player);
        });

    }


    public void loadActions() {
        Config config = getConfigsManager().getConfig("config");
        actionsProcessor.registerActions("on-join", config.getList("on-join.events", String.class).orElse(new ArrayList<>()));
        if (config.get("on-first-join.enabled", Boolean.class).orElse(false)){
            actionsProcessor.registerActions("on-first-join", config.getList("on-first-join.events", String.class).orElse(new ArrayList<>()));
        }
    }

    @Override
    public Class<? extends Entity>[] loadEntites() {
        return new Class[]{LobbyPlayer.class, LobbyLocation.class};
    }

    @Override
    public ICommand[] loadCommands() {
        return new ICommand[]{new SetLobby(), new Lobby(), new FlyCommand(), new GamemodeCommand(), new BuildCommand(), new LockChatCommand(),new WarnCommand(), new KickCommand(), new BanCommand(), new BanIPCommand(), new UnbanIPCommand(), new UnbanCommand(), new MuteCommand(), new UnmuteCommand(), new OpenInventoryCommand(), new VanishCommand(), new ExecuteActionCommand()};
    }

    @Override
    public Listener[] loadEvents() {
        return new Listener[]{new LoginAndLogoutEvents(), new LobbyCooldownEvent(), new FlyEvent(), new MultiJumpEvent(), new DefaultPlayerEvents(), new LaunchPadEvent(), new MOTDEvent(), new DefaultWorldEvents(), new AntiSpam(), new AntiSwear(), new MutedChat(), new ChatLockedEvent(), new CommandProcess(), XG7Plugins.getMinecraftVersion() > 14 ? new CommandAntiTab() : null};
    }

    @Override
    public PacketListener[] loadPacketEvents() {
        return new PacketListener[]{XG7Plugins.getMinecraftVersion() < 14 ? new CommandAntiTabOlder() : null};
    }

    @Override
    public Task[] loadRepeatingTasks() {
        return new Task[]{getConfig("config").get("auto-broadcast.enabled", Boolean.class).orElse(false) ? new AutoBroadcast() : null, getConfig("config").contains("effects") ? new Effects() : null, new WorldCycles()};
    }

    @Override
    public BaseMenu[] loadMenus() {
        return inventoryManager.getInventories().toArray(new BaseMenu[0]);
    }

    @Override
    public void loadHelp() {

    }

    @Override
    public Score[] loadScores() {

        Config config = getConfig("config");

        ScoreboardLoader scoreboardLoader = new ScoreboardLoader(config);
        TablistLoader tablistLoader = new TablistLoader(config);
        BossBarLoader bossBarLoader = new BossBarLoader(config);
        ActionBarLoader actionBarLoader = new ActionBarLoader(config);
        XPBarLoader xpBarLoader = new XPBarLoader(config);


        if (scoreboardLoader.isEnabled() || tablistLoader.isEnabled() || bossBarLoader.isEnabled() || actionBarLoader.isEnabled() || xpBarLoader.isEnabled()) {
            XG7Plugins.taskManager().runTask(XG7Plugins.taskManager().getRegisteredTask(XG7Plugins.getInstance(), "score-task"));
        }

        return new Score[]{scoreboardLoader.load(), tablistLoader.load(), bossBarLoader.load(), actionBarLoader.load(), xpBarLoader.load()};
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (XG7Plugins.getInstance().getMenuManager().hasPlayerMenu(player.getUniqueId())) {
                player.getInventory().clear();
                XG7Plugins.getInstance().getMenuManager().removePlayerMenu(player.getUniqueId());
            }
        });
    }


    public static XG7Lobby getInstance() {
        return getPlugin(XG7Lobby.class);
    }
}
