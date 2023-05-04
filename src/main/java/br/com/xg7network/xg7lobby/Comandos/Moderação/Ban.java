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

public class Ban implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("xg7lobby.admin")) {
            if (args.length == 1) {
                String pName = args[0];
                Player pIsOn = Bukkit.getPlayerExact(pName);
                OfflinePlayer pIsOff = Bukkit.getOfflinePlayer(pName);
                if (pIsOn != null || pIsOff != null) {
                    if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOn.getName()) || !Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOff.getName())) {
                        if (pIsOn != null) {
                            pIsOn.kickPlayer("");
                            Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOn.getName(), "", null, null);
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                action.mandarAction(p, ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName());
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName());
                            }
                        } else if (pIsOff != null) {
                            Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOff.getName(), "", null, null);
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                action.mandarAction(p,  ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName());
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName());
                            }
                        } else {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                action.mandarAction(p, ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName());
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName());
                            }
                        }
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            action.mandarAction(p, ChatColor.RED + "Este jogador foi encontrado na lista de banidos!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Este jogador foi encontrado na lista de banidos!");
                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        action.mandarAction(p, ChatColor.RED + "Este jogador não existe!");

                    } else {
                        sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                    }
                }
            } else if (args.length >= 2) {
                String pName = args[0];
                Player pIsOn = Bukkit.getPlayerExact(pName);
                OfflinePlayer pIsOff = Bukkit.getOfflinePlayer(pName);
                String str = "";
                for (int i = 1; i < args.length; i++) {
                    str += args[i] + " ";
                }
                if (pIsOn.isOnline()) {
                    pIsOn.kickPlayer(str.trim().replace("&", "§"));
                }
                if (pIsOn != null || pIsOff != null && !Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOn.getName()) || !Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOff.getName())) {
                    if (pIsOn != null) {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOn.getName(), str.trim().replace("&", "§"), null, null);
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            action.mandarAction(p, ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por" + ChatColor.RESET + str.trim());
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por" + ChatColor.RESET + str.trim());
                        }
                    } else if (pIsOff != null) {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOff.getName(), str.trim().replace("&", "§"), null, null);
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            action.mandarAction(p, ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por" + ChatColor.RESET + str.trim());
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por" + ChatColor.RESET + str.trim().replace("&", "§"));
                        }
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            action.mandarAction(p, ChatColor.RED + "Este jogador foi encontrado na lista de banidos!");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Este jogador foi encontrado na lista de banidos!");
                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        action.mandarAction(p, ChatColor.RED + "Este jogador não existe!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Este jogador não existe!");

                    }
                }
                return true;
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    action.mandarAction(p, ChatColor.GOLD + "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /ban <Jogador> [Razão]");
                } else {
                    sender.sendMessage(ChatColor.GOLD + "[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /ban <Jogador> [Razão]");
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
