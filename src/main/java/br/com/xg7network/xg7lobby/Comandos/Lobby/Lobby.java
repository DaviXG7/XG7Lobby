package br.com.xg7network.xg7lobby.Comandos.Lobby;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static br.com.xg7network.xg7lobby.XG7Lobby.*;

public class Lobby implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("xg7llobby")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (data.getData().getString("lobby.Mundo") == null) {
                    if (p.hasPermission("xg7lobby.admin")) {
                        String texto = mensagem.getMessage().getString("mensagens.aviso_lobby_admin");
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            assert texto != null;
                            PlaceholderAPI.setPlaceholders(p.getPlayer(), texto);
                            action.mandarAction(p, texto);
                        } else {
                            action.mandarAction(p, texto);
                        }
                    } else {
                        String texto = mensagem.getMessage().getString("mensagens.aviso_lobby");
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            assert texto != null;
                            PlaceholderAPI.setPlaceholders(p.getPlayer(), texto);
                            action.mandarAction(p, texto);
                        } else {
                            action.mandarAction(p, texto);
                        }
                    }
                } else {
                    if (data.getData().getString("lobby.Mundo") != null) {
                        String lobbyWN = data.getData().getString("lobby.Mundo");
                        if (lobbyWN != null) {
                        World w = Bukkit.getWorld(lobbyWN);
                        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                            String texto = mensagem.getMessage().getString("mensagens.tpLobby");
                            PlaceholderAPI.setPlaceholders(p.getPlayer(), texto);
                            p.sendMessage(Objects.requireNonNull(texto.replace("&", "§")));
                            Objects.requireNonNull(p.getPlayer()).teleport(new Location(w,
                                    data.getData().getDouble("lobby.X"),
                                    data.getData().getDouble("lobby.Y"),
                                    data.getData().getDouble("lobby.Z"),
                                    (float) data.getData().getDouble("lobby.Yaw"),
                                    (float) data.getData().getDouble("lobby.Pitch"))
                            );
                            return true;
                            } else {
                            p.sendMessage(Objects.requireNonNull(mensagem.getMessage().getString("mensagens.tpLobby")).replace("&", "§"));
                            Objects.requireNonNull(p.getPlayer()).teleport(new Location(w,
                                    data.getData().getDouble("lobby.X"),
                                    data.getData().getDouble("lobby.Y"),
                                    data.getData().getDouble("lobby.Z"),
                                    (float) data.getData().getDouble("lobby.Yaw"),
                                    (float) data.getData().getDouble("lobby.Pitch"))
                            );
                        }
                            return true;
                        }
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[XG7Lobby] Esse comano só pode ser executado por players!");
            }
        }
        return true;
    }
}
