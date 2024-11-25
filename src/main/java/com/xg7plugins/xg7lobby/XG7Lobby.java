package com.xg7plugins.xg7lobby;

import com.xg7plugins.Plugin;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.config.Config;
import com.xg7plugins.data.database.Entity;
import com.xg7plugins.events.Event;
import com.xg7plugins.xg7lobby.actions.ActionsProcessor;
import com.xg7plugins.xg7lobby.commands.lobby.Lobby;
import com.xg7plugins.xg7lobby.commands.lobby.SetLobby;
import com.xg7plugins.xg7lobby.events.JoinAndQuitEvents;
import com.xg7plugins.xg7lobby.model.PlayerData;
import com.xg7plugins.xg7lobby.model.Warn;
import com.xg7plugins.xg7lobby.utils.PlayerDAO;
import lombok.Getter;

@Getter
public final class XG7Lobby extends Plugin {

    private final ActionsProcessor actionsProcessor;
    private PlayerDAO playerDAO;


    public XG7Lobby() {
        super("§1X§9G§37§bLobby§r", new String[]{}, new String[]{
                "§1 __   §9_______ §3______ §b_           _     _           ",
                "§1 \\ \\ §9/ / ____§3|____  §b| |         | |   | |          ",
                "§1  \\ V §9/ |  __ §3   / /§b| |     ___ | |__ | |__  _   _ ",
                "§1   > <§9| | |_ |  §3/ / §b| |    / _ \\| '_ \\| '_ \\| | | |",
                "§1  / . §9\\ |__| | §3/ /  §b| |___| (_) | |_) | |_) | |_| |",
                "§1 /_/ \\_§9\\_____|§3/_/   §b|______\\___/|_.__/|_.__/ \\__, |",
                "§b                                              __/ |",
                "§b                                             |___/ "

        });
        actionsProcessor = new ActionsProcessor();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        loadActions();
        // Plugin startup logic

    }

    public void loadActions() {
        getLog().loading("Loading action events...");
        Config config = getConfigsManager().getConfig("config");
        actionsProcessor.registerActions("on-join", config.getList("on-join.events"));
        if (config.get("on-first-join.enabled")) actionsProcessor.registerActions("on-first-join", config.getList("on-first-join.events"));
        playerDAO = new PlayerDAO();
    }

    @Override
    public Class<? extends Entity>[] loadEntites() {
        return new Class[]{PlayerData.class};
    }

    @Override
    public ICommand[] loadCommands() {
        return new ICommand[]{new SetLobby(), new Lobby()};
    }

    @Override
    public Event[] loadEvents() {
        return new Event[]{new JoinAndQuitEvents()};
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static XG7Lobby getInstance() {
        return XG7Plugins.getPlugin(XG7Lobby.class);
    }
}
