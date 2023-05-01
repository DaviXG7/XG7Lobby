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

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;
import static br.com.xg7network.xg7lobby.Comandos.Moderação.Mute.targets;

public class Unmute implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("xg7lobby.admin")) {
            if (args.length == 1) {
                Player newTarget = Bukkit.getPlayerExact(args[0]);
                if (targets.contains(newTarget.getName())) {
                    targets.remove(newTarget.getName());
                    sender.sendMessage(ChatColor.GREEN + "Você desmutou " + newTarget.getName() + "!");
                    if (newTarget.isOnline()) {
                        if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                            newTarget.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.QuandoDesmutado")));
                        } else {
                            newTarget.sendMessage(mensagem.getMessage().getString("mensagens.QuandoDesmutado"));
                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("[XG7Lobby] Este jogador não está na lista de mutados"));
                        } else {
                            sender.sendMessage("[XG7Lobby] Este jogador não está na lista de mutados");
                        }
                    } else {
                        sender.sendMessage("[XG7Lobby] Este jogador não está na lista de mutados");
                    }
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /unmute <Jogador>"));
                    } else {
                        sender.sendMessage("[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /unmute <Jogador>");
                    }
                } else {
                    sender.sendMessage("[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /unmute <Jogador>");
                }
            }
        } else {
            if (XG7Lobby.mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
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
            }
        }
        return true;
    }
}
