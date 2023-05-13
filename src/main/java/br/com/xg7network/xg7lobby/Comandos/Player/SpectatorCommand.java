package br.com.xg7network.xg7lobby.Comandos.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static br.com.xg7network.xg7lobby.XG7Lobby.action;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class SpectatorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("xg7lobby.gamemode.spectator") || p.hasPermission("xg7lobby.admin")) {
                if (args.length == 0) {
                    if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
                        p.setGameMode(GameMode.SPECTATOR);
                        if (mensagem.getMessage().getBoolean("mensagens.ativar_mensagem_modo_de_jogo")) {
                            action.mandarAction(p, mensagem.getMessage().getString("mensagens.modo_espectador"));
                        }
                    } else {
                        if (mensagem.getMessage().getBoolean("mensagens.ativar_mensagem_modo_de_jogo")) {
                            action.mandarAction(p, mensagem.getMessage().getString("VocêJáEstáNoModo").replace("[MODO]", p.getGameMode().toString()));
                        }
                    }
                } else if (args.length == 1) {
                    if (p.hasPermission("xg7lobby.gamemode.others")) {
                        Player target = Bukkit.getPlayerExact(args[0]);
                        if (target != null) {
                            if (target.isOnline()) {
                                target.setGameMode(GameMode.SPECTATOR);
                                if (mensagem.getMessage().getBoolean("mensagens.ativar_mensagem_modo_de_jogo")) {
                                    action.mandarAction(target, mensagem.getMessage().getString("mensagens.espectador"));
                                }
                            } else {
                                action.mandarAction(p, ChatColor.RED + "Este jogador não está online");
                            }
                        } else {
                            action.mandarAction(p, ChatColor.RED + "Esse jogador não foi encontrado!");
                        }
                    } else {
                        if (mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                            action.mandarAction(p, mensagem.getMessage().getString("mensagens.permissao_colocar_gamemode_aos_outros").replace("&", "§"));
                        }
                    }
                }
            } else {
                if (mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                    action.mandarAction(p, mensagem.getMessage().getString("mensagens.permissao_comandos").replace("[Comando]", "/" + command.getName()).replace("&", "§"));
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[XG7Lobby] Esse comano só pode ser executado por players!");
        }
        return true;
    }

}
