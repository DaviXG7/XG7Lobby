package br.com.xg7network.xg7lobby.Comandos.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class AdventureCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("xg7lobby.command.creative") ||
                    p.hasPermission("xg7lobby.admin")) {
                p.setGameMode(GameMode.ADVENTURE);
                if (mensagem.getMessage().getBoolean("mensagens.ativar_mensagem_modo_de_jogo")) {
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.modo_aventura").replace("&", "§")));
                    } else {
                        p.sendMessage(mensagem.getMessage().getString("mensagens.modo_aventura").replace("&", "§"));
                    }
                }
            } else {
                if (mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_comandos")));
                        } else {
                            player.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                        }
                    } else {
                        sender.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                    }
                    return true;
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[XG7Lobby] Esse comano só pode ser executado por players!");
        }
        return true;
    }
}
