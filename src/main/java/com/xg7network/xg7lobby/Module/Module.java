package com.xg7network.xg7lobby.Module;

import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.event.Listener;

public abstract class Module implements Listener {

    private XG7Lobby plugin;


    public Module(XG7Lobby plugin) {
        this.plugin = plugin;
    }

    public XG7Lobby getPlugin() {
        return this.plugin;
    }

    public abstract void onEnable();

    public abstract void onDisable();
}