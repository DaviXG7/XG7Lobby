package br.com.xg7network.xg7lobby.Comandos.Lobby;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;
import static org.bukkit.Bukkit.getPlayer;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("xg7lobby.fly") || p.hasPermission("xg7lobby.admin")) {
                if (args.length == 0) {
                    if (p.isFlying()) {
                        p.setAllowFlight(false);
                        p.setFlying(false);
                        p.sendMessage(mensagem.getMessage().getString("mensagens.flyoff").replace("&", "§"));
                        return true;
                    } else if (!p.isFlying()) {
                        p.setAllowFlight(true);
                        p.setFlying(true);
                        p.sendMessage(mensagem.getMessage().getString("mensagens.flyon").replace("&", "§"));
                        return true;
                    }
                } else if (args.length == 1) {
                    String pName;
                    pName = args[0];
                    Player pr = getPlayer(pName);
                    assert pr != null;
                    if (pr.getPlayer().isFlying()) {
                        pr.setAllowFlight(false);
                        pr.setFlying(false);
                        pr.sendMessage(mensagem.getMessage().getString("mensagens.flyon").replace("&", "§"));
                        return true;
                    } else {
                        pr.setAllowFlight(true);
                        pr.setFlying(true);
                        pr.sendMessage(mensagem.getMessage().getString("mensagens.flyon").replace("&", "§"));
                        return true;
                    }
                }
            } else {
                if (XG7Lobby.mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName())));
                    } else {
                        p.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[XG7Lobby] Esse commando só pode ser executado por players");
        }
        return true;
    }
}