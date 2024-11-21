package com.xg7plugins.xg7lobby;

import com.xg7plugins.Plugin;
import com.xg7plugins.XG7Plugins;
import com.xg7plugins.xg7lobby.commands.lobby.Lobby;
import com.xg7plugins.xg7lobby.commands.lobby.SetLobby;

public final class XG7Lobby extends Plugin {


    public XG7Lobby() {
        super("XG7Lobby", new String[]{});
    }

    @Override
    public void onEnable() {

        super.onEnable();

        XG7Plugins.register(this);
        XG7Plugins.getInstance().registerCommands(this, SetLobby.class, Lobby.class);
        XG7Plugins.getInstance().connectPlugin(this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public void onLoad() {

    }

    public static XG7Lobby getInstance() {
        return XG7Plugins.getPlugin(XG7Lobby.class);
    }
}
