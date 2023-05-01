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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static br.com.xg7network.xg7lobby.XG7Lobby.data;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class Mute implements CommandExecutor, Listener {

    protected static List<String> targets = data.getData().getStringList("Mutados");
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("xg7lobby.admin")) {
            if (args.length == 1) {
                Player targetM = Bukkit.getPlayerExact(args[0]);
                targets.add(targetM.getName());
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "Você mutou " + targetM.getName() + "!"));
                    } else {
                        p.sendMessage(ChatColor.GREEN + "Você mutou " + targetM.getName() + "!");
                    }
                } else {
                    sender.sendMessage(ChatColor.GREEN + "Você mutou " + targetM.getName() + "!");
                }
                if (targetM.isOnline()) {
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        targetM.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.QuandoMutado").replace("&", "§")));
                    } else {
                        targetM.sendMessage(mensagem.getMessage().getString("mensagens.QuandoMutado").replace("&", "§"));
                    }
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /mute <Jogador>"));
                    } else {
                        sender.sendMessage(ChatColor.GOLD + "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /mute <Jogador>");
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /mute <Jogador>");
                }
            }
        } else {
            if (XG7Lobby.mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName())));
                    } else {
                        p.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                    }
                } else {
                    sender.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                }

            }
        }
        return true;
    }

    @EventHandler
    public void onMute(AsyncPlayerChatEvent e) {
        if (targets.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.no_mute").replace("&", "§")));
            } else {
                e.getPlayer().sendMessage(mensagem.getMessage().getString("mensagens.no_mute").replace("&", "§"));
            }
        }


    }
}
