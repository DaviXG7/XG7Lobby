package com.xg7network.xg7lobby.Configs;

import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {

    private static FileConfiguration config = null;
    private static  FileConfiguration seletor = null;
    private static FileConfiguration data = null;
    private static  FileConfiguration mensagem = null;

    private  static File Cfile = null;
    private  static File Dfile = null;
    private static  File Sfile = null;
    private static  File Mfile = null;


    public void reloadConfig(ConfigType type) {

        switch (type) {
            case CONFIG:
                if (Cfile == null) loadConfig(type);

                config = null;

                config = YamlConfiguration.loadConfiguration(Cfile);

                return;

            case DATA:
                if (Dfile == null) loadConfig(type);

                data = null;

                data = YamlConfiguration.loadConfiguration(Dfile);

                return;

            case MESSAGES:
                if (Mfile == null) loadConfig(type);

                mensagem = null;

                mensagem = YamlConfiguration.loadConfiguration(Mfile);


                return;

            case SELECTORS:
                if (Sfile == null) loadConfig(type);

                seletor = null;

                seletor = YamlConfiguration.loadConfiguration(Sfile);

        }
    }

    public FileConfiguration getConfig(ConfigType type) {
        switch (type) {
            case CONFIG:
                if (config == null) {
                    reloadConfig(type);
                }
                return config;
            case DATA:
                if (data == null) {
                    reloadConfig(type);
                }
                return data;
            case MESSAGES:
                if (mensagem == null) {
                    reloadConfig(type);
                }
                return mensagem;
            case SELECTORS:
                if (seletor == null) {
                    reloadConfig(type);
                }
                return seletor;
        }
        return null;
    }

    public void saveConfig(ConfigType type) {
        switch (type) {
            case CONFIG:
                if (config == null || Cfile == null)
                    return;
                try {
                    this.getConfig(type).save(Cfile);
                } catch (IOException e) {
                    XG7Lobby.getPlugin().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Cfile, e);
                }

                return;

            case DATA:
                if (data == null || Dfile == null)
                    return;
                try {
                    this.getConfig(type).save(Dfile);
                } catch (IOException e) {
                    XG7Lobby.getPlugin().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Dfile, e);
                }


                return;

            case MESSAGES:
                if (mensagem == null || Mfile == null)
                    return;
                try {
                    this.getConfig(type).save(Mfile);
                } catch (IOException e) {
                    XG7Lobby.getPlugin().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Mfile, e);
                }

                return;

            case SELECTORS:
                if (seletor == null || Sfile == null)
                    return;
                try {
                    this.getConfig(type).save(Sfile);
                } catch (IOException e) {
                    XG7Lobby.getPlugin().getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Sfile, e);
                }

        }
    }

    public void loadConfig(ConfigType type) {
        switch (type) {
            case CONFIG:
                if (Cfile == null) {
                    Cfile = new File(XG7Lobby.getPlugin().getDataFolder(), type.getConfig());
                }
                if (!Cfile.exists()) {
                    XG7Lobby.getPlugin().saveResource(type.getConfig(), false);
                }

                return;

            case DATA:
                if (Dfile == null) {
                    Dfile = new File(XG7Lobby.getPlugin().getDataFolder(), type.getConfig());
                }
                if (!Dfile.exists()) {
                    XG7Lobby.getPlugin().saveResource(type.getConfig(), false);
                }

                return;

            case MESSAGES:
                if (Mfile == null) {
                    Mfile = new File(XG7Lobby.getPlugin().getDataFolder(), type.getConfig());
                }
                if (!Mfile.exists()) {
                    XG7Lobby.getPlugin().saveResource(type.getConfig(), false);
                }

                return;

            case SELECTORS:
                if (Sfile == null) {
                    Sfile = new File(XG7Lobby.getPlugin().getDataFolder(), type.getConfig());
                }
                if (!Sfile.exists()) {
                    XG7Lobby.getPlugin().saveResource(type.getConfig(), false);
                }
        }
    }
}
