package br.com.xg7network.xg7lobby.Comandos.Moderação;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static br.com.xg7network.xg7lobby.XG7Lobby.action;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class Unban implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("xg7lobby.admin")) {
            if (args.length == 1) {
                String prName = args[0];
                OfflinePlayer p = Bukkit.getOfflinePlayer(prName);
                if (p != null) {
                    Bukkit.getBanList(BanList.Type.NAME).pardon(p.getName());
                    if (sender instanceof Player) {
                        Player pm = (Player) sender;
                        action.mandarAction(pm, ChatColor.GREEN + "[XG7Lobby] Você perdoou " + ChatColor.YELLOW + p.getName());
                    } else {
                        sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você perdoou " + ChatColor.YELLOW + p.getName());
                    }
                } else {
                    if (sender instanceof Player) {
                        Player pm = (Player) sender;
                        action.mandarAction(pm,ChatColor.RED + "Este jogador não existe!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                    }
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    action.mandarAction(p, "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /unban <Jogador>");
                } else {
                    sender.sendMessage("[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /unban <Jogador>");
                }
            }
        } else {
            if (XG7Lobby.mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    action.mandarAction(p, mensagem.getMessage().getString("mensagens.permissao_comandos").replace("[Comando]", "/" + command.getName()));
                } else {
                    sender.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                }

            }
        }
        return true;
    }
}
