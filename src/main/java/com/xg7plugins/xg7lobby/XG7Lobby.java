package com.xg7plugins.xg7lobby;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.boot.PluginConfigurations;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.events.Listener;
import com.xg7plugins.events.PacketListener;

import com.xg7plugins.modules.xg7menus.XG7Menus;
import com.xg7plugins.modules.xg7menus.menus.BaseMenu;
import com.xg7plugins.modules.xg7scores.XG7Scores;
import com.xg7plugins.tasks.Task;
import com.xg7plugins.utils.Debug;
import com.xg7plugins.utils.Metrics;
import com.xg7plugins.xg7lobby.actions.ActionsProcessor;
import com.xg7plugins.xg7lobby.commands.*;
import com.xg7plugins.xg7lobby.commands.customcommand.CustomCommandManager;
import com.xg7plugins.xg7lobby.commands.lobby.Lobby;
import com.xg7plugins.xg7lobby.commands.lobby.SetLobby;
import com.xg7plugins.xg7lobby.commands.moderation.KickCommand;
import com.xg7plugins.xg7lobby.commands.moderation.WarnCommand;
import com.xg7plugins.xg7lobby.commands.moderation.WarnsCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.BanCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.BanIPCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.UnbanCommand;
import com.xg7plugins.xg7lobby.commands.moderation.ban.UnbanIPCommand;
import com.xg7plugins.xg7lobby.commands.moderation.mute.MuteCommand;
import com.xg7plugins.xg7lobby.commands.moderation.mute.UnmuteCommand;
import com.xg7plugins.xg7lobby.commands.toggleCommands.BuildCommand;
import com.xg7plugins.xg7lobby.commands.toggleCommands.FlyCommand;
import com.xg7plugins.xg7lobby.commands.toggleCommands.LockChatCommand;
import com.xg7plugins.xg7lobby.commands.toggleCommands.VanishCommand;
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
import com.xg7plugins.xg7lobby.inventories.warn_menu.WarnMenu;
import com.xg7plugins.xg7lobby.lobby.ServerInfo;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import com.xg7plugins.xg7lobby.lobby.location.LobbyManager;
import com.xg7plugins.xg7lobby.lobby.player.*;
import com.xg7plugins.xg7lobby.lobby.scores.*;
import com.xg7plugins.xg7lobby.pvp.GlobalPVPManager;
import com.xg7plugins.xg7lobby.pvp.PVPListener;
import com.xg7plugins.xg7lobby.repeating_tasks.AutoBroadcast;
import com.xg7plugins.xg7lobby.repeating_tasks.Effects;
import com.xg7plugins.xg7lobby.repeating_tasks.WorldCycles;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private ActionsProcessor actionsProcessor;
    private LobbyManager lobbyManager;
    private PlayerDAO playerDAO;
    private ServerInfo serverInfo;
    private InventoryManager inventoryManager;
    private CustomCommandManager customCommandManager;
    @Getter
    private GlobalPVPManager globalPVPManager;

    @Override
    public void onLoad() {
        super.onLoad();
        actionsProcessor = new ActionsProcessor();
        playerDAO = new PlayerDAO();
        serverInfo = new ServerInfo(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        Metrics.getMetrics(this, 24625);

        lobbyManager = new LobbyManager();

        if (XG7Plugins.isPlaceholderAPI()) new XG7LobbyPlaceholderExpansion().register();

        Config config = getConfigsManager().getConfig("config");

        if (config.get("menus-enabled",Boolean.class).orElse(false)) {

            Debug.of(XG7Lobby.getInstance()).loading("Loading custom menus...");
            inventoryManager = new InventoryManager(this, "games", "profile", "selector", "pvp_selector");

        }

        if (config.get("global-pvp.enabled", Boolean.class).orElse(false)) {
            Debug.of(XG7Lobby.getInstance()).loading("Loading global pvp manager...");
            globalPVPManager = new GlobalPVPManager(config);
        }

        if (config.get("custom-commands-enabled", Boolean.class).orElse(false)) {
            Debug.of(XG7Lobby.getInstance()).loading("Loading custom commands...");
            customCommandManager = new CustomCommandManager(config);
        }

        Debug.of(XG7Lobby.getInstance()).loading("Loading action events...");
        loadActions();

        if (XG7Plugins.isBungeecord()) getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Debug.of(XG7Lobby.getInstance()).loading("Loading menus...");


        List<BaseMenu> menus = new ArrayList<>();

        menus.add(new WarnMenu());

        menus.addAll(inventoryManager.getInventories());

        XG7Menus.getInstance().registerMenus(menus.toArray(new BaseMenu[0]));

        Debug.of(XG7Lobby.getInstance()).loading("Loading scores...");

        ScoreboardLoader scoreboardLoader = new ScoreboardLoader(config);
        TablistLoader tablistLoader = new TablistLoader(config);
        BossBarLoader bossBarLoader = new BossBarLoader(config);
        ActionBarLoader actionBarLoader = new ActionBarLoader(config);
        XPBarLoader xpBarLoader = new XPBarLoader(config);


        if (scoreboardLoader.isEnabled() || tablistLoader.isEnabled() || bossBarLoader.isEnabled() || actionBarLoader.isEnabled() || xpBarLoader.isEnabled()) {
            XG7Plugins.taskManager().runTask(XG7Plugins.taskManager().getRegisteredTask(XG7Plugins.getInstance(), "score-task"));
        }

        XG7Scores.getInstance().registerScores(scoreboardLoader.load(), tablistLoader.load(), bossBarLoader.load(), actionBarLoader.load(), xpBarLoader.load());


        Bukkit.getOnlinePlayers().forEach(player -> {

            LobbyPlayer.cast(player.getUniqueId(), false);

            LobbySelector menu = XG7Lobby.getInstance().getInventoryManager().getInventories().stream().filter(m -> m instanceof LobbySelector).map(m -> (LobbySelector) m).findFirst().orElse(null);

            if (menu != null) menu.open(player);
        });

    }


    public void loadActions() {
        Config config = getConfigsManager().getConfig("config");
        actionsProcessor.registerActions("on-join", config.getList("on-join.events", String.class).orElse(new ArrayList<>()));
        if (config.get("on-first-join.enabled", Boolean.class).orElse(false)) actionsProcessor.registerActions("on-first-join", config.getList("on-first-join.events", String.class).orElse(new ArrayList<>()));
        if (config.get("global-pvp.enabled", Boolean.class).orElse(false)) {
            actionsProcessor.registerActions("on-pvp-enter", config.getList("global-pvp.on-enter-pvp.actions", String.class).orElse(new ArrayList<>()));
            actionsProcessor.registerActions("on-pvp-leave", config.getList("global-pvp.on-leave-pvp.actions", String.class).orElse(new ArrayList<>()));
            actionsProcessor.registerActions("on-pvp-die", config.getList("global-pvp.on-death-actions", String.class).orElse(new ArrayList<>()));
        }
    }

    @Override
    public Class<? extends Entity>[] loadEntites() {
        return new Class[]{LobbyPlayer.class, LobbyLocation.class};
    }

    @Override
    public ICommand[] loadCommands() {
        return new ICommand[]{new SetLobby(), new Lobby(), new FlyCommand(), new GamemodeCommand(), new BuildCommand(), new LockChatCommand(),new WarnCommand(), new KickCommand(), new BanCommand(), new BanIPCommand(), new UnbanIPCommand(), new UnbanCommand(), new MuteCommand(), new UnmuteCommand(), new OpenInventoryCommand(), new VanishCommand(), new ExecuteActionCommand(), new PVPCommand(), new WarnsCommand()};
    }

    @Override
    public Listener[] loadEvents() {
        return new Listener[]{new LoginAndLogoutEvents(), new LobbyCooldownEvent(), new FlyEvent(), new MultiJumpEvent(), new DefaultPlayerEvents(), new LaunchPadEvent(), new MOTDEvent(), new DefaultWorldEvents(), new AntiSpam(), new AntiSwear(), new MutedChat(), new ChatLockedEvent(), new CommandProcess(), XG7Plugins.getMinecraftVersion() > 14 ? new CommandAntiTab() : null, new PVPListener(globalPVPManager)};
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
    public void loadHelp() {

    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {

            if (globalPVPManager.isPlayerInPVP(player)) globalPVPManager.removePlayerFromPVP(player);

            LobbySelector menu = XG7Lobby.getInstance().getInventoryManager().getInventories().stream().filter(m -> m.getId().equals(getConfig("config").get("main-selector-id", String.class).orElse(null))).map(m -> (LobbySelector) m).findFirst().orElse(null);

            if (menu != null) {
                menu.close(player);
            }
        });
    }


    public static XG7Lobby getInstance() {
        return getPlugin(XG7Lobby.class);
    }
}
