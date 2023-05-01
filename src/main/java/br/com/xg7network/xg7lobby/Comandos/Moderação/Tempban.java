package br.com.xg7network.xg7lobby.Comandos.Moderação;

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

import java.util.Calendar;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class Tempban implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("xg7lobby.admin")) {
            if (args.length == 2) {
                String pName = args[0];
                Player pIsOn = Bukkit.getPlayerExact(pName);
                OfflinePlayer pIsOff = Bukkit.getOfflinePlayer(pName);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR, Integer.parseInt(args[1]));
                if (pIsOn != null || pIsOff != null) {
                    if (pIsOn.isOnline()) {
                        pIsOn.kickPlayer("");
                    }
                    if (!Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOn.getName()) || !Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOff.getName())) {
                        if (pIsOn != null) {
                            Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOn.getName(), "", cal.getTime(), null);
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " horas"));
                                } else {
                                    sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " horas");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " horas");
                            }
                        } else if (pIsOff != null) {
                            Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOff.getName(), "", cal.getTime(), null);
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " horas"));
                                } else {
                                    sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " horas");
                                }
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " horas");
                            }

                        } else {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Este jogador não existe!"));
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                                }
                            } else {
                                sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                            }
                        }
                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Este jogador não existe ou não foi encontrado na lista de banidos!"));
                            } else {
                                sender.sendMessage(ChatColor.RED + "Este jogador não existe ou não foi encontrado na lista de banidos!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Este jogador não existe ou não foi encontrado na lista de banidos!");
                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Este jogador não existe!"));
                        } else {
                            sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                    }
                }
            } else if (args.length >= 3) {
                String pName = args[0];
                Player pIsOn = Bukkit.getPlayerExact(pName);
                OfflinePlayer pIsOff = Bukkit.getOfflinePlayer(pName);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR, Integer.parseInt(args[1]));
                String str = "";
                for (int i = 1; i < args.length; i++) {
                    str += args[i] + " ";
                }
                if (pIsOn.isOnline()) {
                    pIsOn.kickPlayer(str.trim().replace("&", "§"));
                }
                if (pIsOn != null || pIsOff != null && !Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOn.getName()) || !Bukkit.getBanList(BanList.Type.NAME).isBanned(pIsOff.getName())) {
                    if (pIsOn != null) {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOn.getName(), str.trim().replace("&", "§"), cal.getTime(), null);
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + str.trim().replace("&", "§") + ChatColor.GREEN + ", por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " Horas"));
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + str.trim().replace("&", "§") + ChatColor.GREEN + ", por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " Horas");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOn.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + str.trim().replace("&", "§") + ChatColor.GREEN + ", por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " Horas");
                        }

                    } else if (pIsOff != null) {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(pIsOff.getName(), "", cal.getTime(), null);
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + str.trim().replace("&", "§") + ChatColor.GREEN + ", por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " Horas"));
                            } else {
                                sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + str.trim().replace("&", "§") + ChatColor.GREEN + ", por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " Horas");
                            }
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "[XG7Lobby] Você baniu " + ChatColor.YELLOW + pIsOff.getName() + ChatColor.GREEN + " por " + ChatColor.RESET + str.trim().replace("&", "§") + ChatColor.GREEN + ", por " + ChatColor.RESET + cal.getTime() + ChatColor.GREEN + " Horas");
                        }

                    } else {
                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Este jogador não existe ou não foi encontrado na lista de banidos!"));
                            } else {
                                sender.sendMessage(ChatColor.RED + "Este jogador não existe ou não foi encontrado na lista de banidos!");
                            }
                        } else {
                            sender.sendMessage(ChatColor.RED + "Este jogador não existe ou não foi encontrado na lista de banidos!");
                        }
                    }
                } else {
                    if (sender instanceof Player) {
                        Player p = (Player) sender;
                        if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Este jogador não existe!"));
                        } else {
                            sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Este jogador não existe!");
                    }
                }
            } else {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /tempban <Jogador> <Tempo> [Mensagem]"));
                    } else {
                        sender.sendMessage("[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /tempban <Jogador> <Tempo> [Mensagem]");
                    }
                } else {
                    sender.sendMessage("[XG7Lobby] Você não usou o comando corretamente! O jeito certo é /tempban <Jogador> <Tempo> [Mensagem]");
                }
            }
        } else {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName())));
                } else {
                    sender.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                }
            } else {
                sender.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
            }
        }
        return true;
    }
}
