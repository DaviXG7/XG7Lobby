package br.com.xg7network.xg7lobby.Modulo;

import br.com.xg7network.xg7lobby.Modulo.Scores.ActionBars;
import br.com.xg7network.xg7lobby.Modulo.Scores.BossBar;
import br.com.xg7network.xg7lobby.Modulo.Scores.ScoreBoard;
import br.com.xg7network.xg7lobby.Modulo.Scores.TabList;
import br.com.xg7network.xg7lobby.Modulo.Seletores.InventoryManager;
import br.com.xg7network.xg7lobby.XG7Lobby;

import java.util.ArrayList;
import java.util.List;


public class ModuleManager {

    private XG7Lobby plugin;
    private List<Module> modules = new ArrayList<>();

    public ModuleManager(XG7Lobby plugin) {
        this.plugin = plugin;
    }

    public void loadModules() {
        modules.add(new ActionBars(plugin));
        modules.add(new ScoreBoard(plugin));
        modules.add(new TabList(plugin));
        modules.add(new Anúncios(plugin));
        modules.add(new BossBar(plugin));
        modules.add(new InventoryManager(plugin));

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
