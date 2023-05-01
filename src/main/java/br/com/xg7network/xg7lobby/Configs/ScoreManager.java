package br.com.xg7network.xg7lobby.Configs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ScoreManager {
    public FileConfiguration scoreConfig = null;
    public File scoreFile = null;

    public void reloadScore() {
        if (this.scoreFile == null) {
            this.scoreFile = new File(new XG7Lobby().getDataFolder(), "scores.yml");
        }
        this.scoreConfig = YamlConfiguration.loadConfiguration(this.scoreFile);
    }

    public FileConfiguration getScore() {
        if (scoreConfig == null) {
            reloadScore();
        }
        return this.scoreConfig;
    }

    public void saveScore() {
        if (this.scoreConfig == null || this.scoreFile == null)
            return;
        try {
            this.getScore().save("scores.yml");
        } catch (IOException e) {
            new XG7Lobby().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + this.scoreFile, e);
        }
    }
}
