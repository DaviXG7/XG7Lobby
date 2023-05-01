package br.com.xg7network.xg7lobby.Configs;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


public class MensagemManager {
    public  FileConfiguration messageConfig = null;
    public File messageFile = null;

    public void reloadMessage() {
        if (this.messageFile == null) {
            this.messageFile = new File(new XG7Lobby().getDataFolder(), "mensagens.yml");
        }
        this.messageConfig = YamlConfiguration.loadConfiguration(this.messageFile);
    }

    public FileConfiguration getMessage() {
        if (messageConfig == null) {
            reloadMessage();
        }
        return this.messageConfig;
    }

    public void saveMessage() {
        if (this.messageConfig == null || this.messageFile == null)
            return;
        try {
            this.getMessage().save("mensagens.yml");
        } catch (IOException e) {
            new XG7Lobby().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + this.messageFile, e);
        }
    }
}
