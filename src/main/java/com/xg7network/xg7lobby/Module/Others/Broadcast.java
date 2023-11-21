package com.xg7network.xg7lobby.Module.Others;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.Module.Module;
import com.xg7network.xg7lobby.Utils.Text.Message;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class Broadcast extends Module {


    public Broadcast(XG7Lobby plugin) {
        super(plugin);
    }

    int posicao = 0;

    @Override
    public void onEnable() {


        if (configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.enabled")) {
            Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {

                for (Player p : Bukkit.getOnlinePlayers()) {
                    List<List<String>> anuncios = new ArrayList<>();
                    for (String anuncio : configManager.getConfig(ConfigType.CONFIG).getConfigurationSection("announcements.announcements").getKeys(false)) anuncios.add(configManager.getConfig(ConfigType.CONFIG).getStringList("announcements.announcements." + anuncio));
                    if (configManager.getConfig(ConfigType.CONFIG).getConfigurationSection("announcements") != null) {
                        List<String> anuncio = anuncios.get(posicao);


                        for (String linhas : anuncio) {
                            Message message = new Message(linhas, p);
                            if (configManager.getConfig(ConfigType.CONFIG).getBoolean("announcements.AOOL")) {
                                if (configManager.getConfig(ConfigType.CONFIG).getStringList("enabled-worlds").contains(p.getWorld().getName())) {
                                    String s = configManager.getConfig(ConfigType.CONFIG).getString("announcements.sound");
                                    String[] s2 = s.split(", ");
                                    p.playSound(p.getLocation(), Sound.valueOf(s2[0]), Float.parseFloat(s2[1]), Float.parseFloat(s2[2]));
                                    message.sendMessage();
                                }
                            } else {
                                String s = configManager.getConfig(ConfigType.CONFIG).getString("announcements.sound");
                                String[] s2 = s.split(", ");
                                p.playSound(p.getLocation(), Sound.valueOf(s2[0]), Float.parseFloat(s2[1]), Float.parseFloat(s2[2]));
                                message.sendMessage();
                            }

                        }

                        posicao++;

                        if (posicao == anuncios.size()) posicao = 0;
                    }
                }

            }, 0, configManager.getConfig(ConfigType.CONFIG).getInt("announcements.cooldown") * 20);
        }

    }

    @Override
    public void onDisable() {

    }
}
