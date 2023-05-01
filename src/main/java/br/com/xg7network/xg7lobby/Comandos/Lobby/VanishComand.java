package br.com.xg7network.xg7lobby.Comandos.Lobby;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class VanishComand implements CommandExecutor {

    private XG7Lobby pl;

    public VanishComand(XG7Lobby pl) {
        this.pl = pl;
    }

    private List<UUID> vanished = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (!pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                if (!vanished.contains(p.getUniqueId())) {
                    vanished.add(p.getUniqueId());
                    for (Player target : Bukkit.getOnlinePlayers()) {
                            target.hidePlayer(p);
                    }
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("comando_esconder").replace("&", "§")));
                    } else {
                        p.sendMessage(mensagem.getMessage().getString("comando_esconder").replace("&", "§"));
                    }
                } else {
                    for (Player target : Bukkit.getOnlinePlayers()) {
                        target.showPlayer(p);
                    }
                    vanished.remove(p.getUniqueId());
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("comando_revelar").replace("&", "§")));
                    } else {
                        p.sendMessage(mensagem.getMessage().getString("comando_revelar").replace("&", "§"));
                    }
                }
            } else {
                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Você precisa estar em um mundo diferente do lobby para usar isso!"));
                } else {
                    p.sendMessage(ChatColor.RED + "Você precisa estar em um mundo diferente do lobby para usar isso!");
                }
            }


        }
        return true;
    }
}
