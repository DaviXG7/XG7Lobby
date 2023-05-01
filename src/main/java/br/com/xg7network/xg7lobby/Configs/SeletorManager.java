package br.com.xg7network.xg7lobby.Configs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class SeletorManager {

    public FileConfiguration seletorConfig = null;
    public File seletorFile = null;

    public void reloadSelector() {
        if (this.seletorFile == null) {
            this.seletorFile = new File(new XG7Lobby().getDataFolder(), "seletores.yml");
        }
        this.seletorConfig = YamlConfiguration.loadConfiguration(this.seletorFile);
    }

    public FileConfiguration getSelector() {
        if (seletorConfig == null) {
            reloadSelector();
        }
        return this.seletorConfig;
    }

    public void saveSelector() {
        if (this.seletorConfig == null || this.seletorFile == null)
            return;
        try {
            this.getSelector().save("seletores.yml");
        } catch (IOException e) {
            new XG7Lobby().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + this.seletorFile, e);
        }
    }
}
