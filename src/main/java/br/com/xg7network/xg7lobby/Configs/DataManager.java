package br.com.xg7network.xg7lobby.Configs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataManager {

    public FileConfiguration dataConfig = null;
    public File dataFile = null;

    public void reloadData() {
        if (this.dataFile == null) {
            this.dataFile = new File(new XG7Lobby().getDataFolder(), "data.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.dataFile);
    }

    public FileConfiguration getData() {
        if (dataConfig == null) {
            reloadData();
        }
        return this.dataConfig;
    }

    public void saveData() {
        if (this.dataConfig == null || this.dataFile == null)
            return;
        try {
            this.getData().save("data.yml");
        } catch (IOException e) {
            new XG7Lobby().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + this.dataFile, e);
        }
    }
}
