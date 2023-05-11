package br.com.xg7network.xg7lobby.Utilidades;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import static br.com.xg7network.xg7lobby.XG7Lobby.*;

public class ConfigDefaults {

    private XG7Lobby pl;

    public ConfigDefaults(XG7Lobby pl) {
        this.pl = pl;
    }

    public void configDefaults() {
        if (pl.getConfig().getBoolean("ConfigDefaults")) {
            if (Bukkit.getServer().getVersion().contains("1.10")) {
                mensagem.getMessage().set("AvisoEmActionBars", true);
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Sua versão não tem suporte a ActionBars neste plugin");
                mensagem.getMessage().set("AvisoEmActionBars", false);
            }
            if (Bukkit.getServer().getVersion().contains("1.9")) {
                score.getScore().set("bossbar.ativado", true);
            } else {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Sua versão não tem suporte a Bossbars neste plugin");
                score.getScore().set("bossbar.ativado", false);
            }
            if (!Bukkit.getServer().getVersion().contains("1.13")) {
                seletor.getSelector().set("EsconderJogadores.item_ativado", "EMERALD");
                seletor.getSelector().set("EsconderJogadores.item_ativado", "STONE");

            }
            pl.getConfig().set("ConfigDefaults", false);
        }
    }
}
