package com.xg7plugins.xg7lobby;

import com.xg7plugins.Plugin;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.commands.setup.ICommand;
import com.xg7plugins.data.database.Entity;
import com.xg7plugins.xg7lobby.commands.lobby.Lobby;
import com.xg7plugins.xg7lobby.commands.lobby.SetLobby;
import com.xg7plugins.xg7lobby.model.PlayerData;
import com.xg7plugins.xg7lobby.model.Warn;

public final class XG7Lobby extends Plugin {


    public XG7Lobby() {
        super("&1X&9G&37&bLobby&r", new String[]{}, new String[]{
                "§v __   §9_______ §3______ §b_           _     _           ",
                "§1 \\ \\ §9/ / ____§3|____  §b| |         | |   | |          ",
                "§1  \\ V §9/ |  __ §3   / /§b| |     ___ | |__ | |__  _   _ ",
                "§1   > <§9| | |_ |  §3/ / §b| |    / _ \\| '_ \\| '_ \\| | | |",
                "§1  / . §9\\ |__| | §3/ /  §b| |___| (_) | |_) | |_) | |_| |",
                "§1 /_/ \\_§9\\_____|§3/_/   §b|______\\___/|_.__/|_.__/ \\__, |",
                "§b                                              __/ |",
                "§b                                             |___/ "

        });
    }

    @Override
    public void onEnable() {

        super.onEnable();
        
        // Plugin startup logic

    }

    @Override
    public Class<? extends Entity>[] loadEntites() {
        return new Class[]{PlayerData.class, Warn.class};
    }

    @Override
    public ICommand[] loadCommands() {
        return new ICommand[]{new SetLobby(), new Lobby()};
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static XG7Lobby getInstance() {
        return XG7Plugins.getPlugin(XG7Lobby.class);
    }
}
