package br.com.xg7network.xg7lobby.Comandos.Moderação;

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

import static br.com.xg7network.xg7lobby.XG7Lobby.action;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class Kick implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            if (sender.hasPermission("xg7lobby.admin")) {
                if (args.length == 1) {
                    Player pr = Bukkit.getPlayerExact(args[0]);
                    if (pr.isOnline()) {
                        pr.kickPlayer("");
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            action.mandarAction(p, ChatColor.RED + "Este jogador não está OnLine");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Este jogador não está OnLine");
                        }
                    }
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        action.mandarAction(p, ChatColor.GREEN + "[XG7Lobby] Você expulsou " + ChatColor.YELLOW + pr.getName());
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você expulsou " + ChatColor.YELLOW + pr.getName());
                    }
                } else if (args.length >= 2) {
                    Player pr = Bukkit.getPlayerExact(args[0]);
                    String str = "";
                    for (int i = 1; i < args.length; i++) {
                        str += args[i] + " ";
                    }
                        pr.kickPlayer(str.toString().trim().replace("&", "§"));
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        action.mandarAction(p, ChatColor.GREEN + "[XG7Lobby] Você expulsou " + ChatColor.YELLOW + pr.getName() + ChatColor.GREEN + " por" + ChatColor.RESET + str.trim());
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você expulsou " + ChatColor.YELLOW + pr.getName() + ChatColor.GREEN + " por" + ChatColor.RESET + str.trim().replace("&", "§"));
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        action.mandarAction(p, ChatColor.GOLD + "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /kick <Jogador> [Razão]");
                    } else {
                        sender.sendMessage(ChatColor.GOLD + "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /kick <Jogador> [Razão]");
                    }
                }
            } else {
                if (XG7Lobby.mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        action.mandarAction(p, mensagem.getMessage().getString("mensagens.permissao_comandos"));
                    } else {
                        sender.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                    }

                }
            }
        return true;
    }
}
