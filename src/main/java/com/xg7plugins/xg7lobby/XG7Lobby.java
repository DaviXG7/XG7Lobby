package com.xg7plugins.xg7lobby;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.boot.Plugin;
import com.xg7plugins.boot.PluginConfigurations;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.data.database.entity.Entity;
import com.xg7plugins.events.Listener;
import com.xg7plugins.libs.xg7scores.Score;
import com.xg7plugins.tasks.Task;
import com.xg7plugins.xg7lobby.actions.ActionsProcessor;
import com.xg7plugins.xg7lobby.commands.BuildCommand;
import com.xg7plugins.xg7lobby.commands.FlyCommand;
import com.xg7plugins.xg7lobby.commands.GamemodeCommand;
import com.xg7plugins.xg7lobby.commands.lobby.Lobby;
import com.xg7plugins.xg7lobby.commands.lobby.SetLobby;
import com.xg7plugins.xg7lobby.events.*;
import com.xg7plugins.xg7lobby.lobby.ServerInfo;
import com.xg7plugins.xg7lobby.lobby.location.LobbyLocation;
import com.xg7plugins.xg7lobby.lobby.location.LobbyManager;
import com.xg7plugins.xg7lobby.lobby.player.LobbyPlayer;
import com.xg7plugins.xg7lobby.lobby.player.PlayerDAO;
import com.xg7plugins.xg7lobby.lobby.scores.loaders.ScoreboardLoader;
import com.xg7plugins.xg7lobby.repeating_tasks.AutoBroadcast;
import com.xg7plugins.xg7lobby.repeating_tasks.Effects;
import com.xg7plugins.xg7lobby.repeating_tasks.WorldCycles;
import lombok.Getter;

import java.util.ArrayList;
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

    private final ActionsProcessor actionsProcessor;
    private LobbyManager lobbyManager;
    private final PlayerDAO playerDAO;
    private final ServerInfo serverInfo;


    public XG7Lobby() {
        actionsProcessor = new ActionsProcessor();
        playerDAO = new PlayerDAO();
        serverInfo = new ServerInfo(this);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        lobbyManager = new LobbyManager(this);

        getLog().loading("Loading lobbies");
        this.lobbyManager.load();

        getLog().loading("Loading action events...");
        loadActions();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        // Plugin startup logic

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
        return new ICommand[]{new SetLobby(), new Lobby(), new FlyCommand(), new GamemodeCommand(), new BuildCommand()};
    }

    @Override
    public Listener[] loadEvents() {
        return new Listener[]{new LoginAndLogoutEvents(), new LobbyCooldownEvent(), new FlyEvent(), new MultiJumpEvent(), new DefaultPlayerEvents(), new LaunchPadEvent(), new MOTDEvent(), new DefaultWorldEvents()};
    }

    @Override
    public Task[] loadRepeatingTasks() {
        return new Task[]{getConfig("config").get("auto-broadcast.enabled", Boolean.class).orElse(false) ? new AutoBroadcast() : null, getConfig("config").contains("effects") ? new Effects() : null, new WorldCycles()};
    }

    @Override
    public void loadHelp() {

    }

    @Override
    public Score[] loadScores() {

        Config config = getConfig("config");

        ScoreboardLoader scoreboardLoader = new ScoreboardLoader(config);

        if (scoreboardLoader.isEnabled()) {
            XG7Plugins.taskManager().runTask(XG7Plugins.taskManager().getRegisteredTask(XG7Plugins.getInstance(), "score-task"));
        }

        return new Score[]{new ScoreboardLoader(config).load()};
    }

    @Override
    public void onDisable() {
        lobbyManager.save();
    }


    public static XG7Lobby getInstance() {
        return getPlugin(XG7Lobby.class);
    }
}
