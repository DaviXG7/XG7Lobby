package br.com.xg7network.xg7lobby.Utilidades;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class ActionBar {

    public void mandarAction(Player player, String mesage) {
        if (Bukkit.getServer().getVersion().contains("1.10")) {
            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mesage.replace("&", "§")));
            } else {
                player.sendMessage(mesage.replace("&", "§"));
            }
        } else {
            player.sendMessage(mesage.replace("&", "§"));
        }
    }
}
