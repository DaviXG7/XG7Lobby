package com.xg7network.xg7lobby.Configs;

import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {

    private XG7Lobby pl;

    public ConfigManager(XG7Lobby pl) {
        this.pl = pl;
    }

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
                if (Cfile == null) {
                    Cfile = new File(pl.getDataFolder(), type.getConfig());
                }
                config = YamlConfiguration.loadConfiguration(Cfile);

                return;

            case DATA:
                if (Dfile == null) {
                    Dfile = new File(pl.getDataFolder(), type.getConfig());
                }
                data = YamlConfiguration.loadConfiguration(Dfile);

                return;

            case MESSAGES:
                if (Mfile == null) {
                    Mfile = new File(pl.getDataFolder(), type.getConfig());
                }
                mensagem = YamlConfiguration.loadConfiguration(Mfile);


                return;

            case SELECTORS:
                if (Sfile == null) {
                    Sfile = new File(pl.getDataFolder(), type.getConfig());
                }
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
                    this.getConfig(type).save(type.getConfig());
                } catch (IOException e) {
                    pl.getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Cfile, e);
                }

                return;

            case DATA:
                if (data == null || Dfile == null)
                    return;
                try {
                    System.out.println("aaaa");
                    this.getConfig(type).save("data.yml");
                } catch (IOException e) {
                    pl.getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Dfile, e);
                }

                return;

            case MESSAGES:
                if (mensagem == null || Mfile == null)
                    return;
                try {
                    this.getConfig(type).save(type.getConfig());
                } catch (IOException e) {
                    pl.getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Mfile, e);
                }

                return;

            case SELECTORS:
                if (seletor == null || Sfile == null)
                    return;
                try {
                    this.getConfig(type).save(type.getConfig());
                } catch (IOException e) {
                    pl.getLogger().log(Level.SEVERE, "Não foi possível carregar o arquivo: " + Sfile, e);
                }

        }
    }

    public void loadConfig(ConfigType type) {
        switch (type) {
            case CONFIG:
                if (Cfile == null) {
                    Cfile = new File(pl.getDataFolder(), type.getConfig());
                }
                if (!Cfile.exists()) {
                    pl.saveResource(type.getConfig(), false);
                }

                return;

            case DATA:
                if (Dfile == null) {
                    Dfile = new File(pl.getDataFolder(), type.getConfig());
                }
                if (!Dfile.exists()) {
                    pl.saveResource(type.getConfig(), false);
                }

                return;

            case MESSAGES:
                if (Mfile == null) {
                    Mfile = new File(pl.getDataFolder(), type.getConfig());
                }
                if (!Mfile.exists()) {
                    pl.saveResource(type.getConfig(), false);
                }

                return;

            case SELECTORS:
                if (Sfile == null) {
                    Sfile = new File(pl.getDataFolder(), type.getConfig());
                }
                if (!Sfile.exists()) {
                    pl.saveResource(type.getConfig(), false);
                }
        }
    }
}
