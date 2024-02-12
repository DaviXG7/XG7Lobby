package com.xg7network.xg7lobby.Module;

import com.xg7network.xg7lobby.Module.Events.Jumps.FlyManager;
import com.xg7network.xg7lobby.Module.Others.Broadcast;
import com.xg7network.xg7lobby.Module.Others.Effects;
import com.xg7network.xg7lobby.Module.Scores.ScoresManager;
import com.xg7network.xg7lobby.Module.Selectors.SelectorManager;
import com.xg7network.xg7lobby.XG7Lobby;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private XG7Lobby plugin;
    private List<Module> modules = new ArrayList<>();

    public ModuleManager(XG7Lobby plugin) {
        this.plugin = plugin;
    }

    public void loadModules() {

        modules.add(new Players(plugin));
        modules.add(new ScoresManager(plugin));
        modules.add(new Effects(plugin));
        modules.add(new Broadcast(plugin));
        modules.add(new FlyManager(plugin));
        modules.add(new SelectorManager(plugin));
        modules.add(new WarnVersion(plugin));

        for (Module module : modules) {
            module.onEnable();
        }
    }

    public void unloadModules() {
        for (Module module : modules) {
            module.onDisable();
        }
    }
}   
