package br.com.xg7network.xg7lobby.Comandos.Lobby;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static br.com.xg7network.xg7lobby.XG7Lobby.data;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class setLobby implements CommandExecutor {
    private XG7Lobby pl;
    FileConfiguration lobbyC;

    File lobbyF;
    public setLobby (XG7Lobby pl) {
        this.pl = pl;
        this.lobbyC = data.getData();
        this.lobbyF = new File(pl.getDataFolder(), "data.yml");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("xg7lsetlobby")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (p.hasPermission("xg7lobby.admin")) {
                    if (pl.getConfig().getStringList("mundos_ativados").contains(p.getWorld().getName())) {
                        lobbyC.set("lobby.Mundo", p.getWorld().getName());
                        lobbyC.set("lobby.X", p.getLocation().getX());
                        lobbyC.set("lobby.Y", p.getLocation().getY());
                        lobbyC.set("lobby.Z", p.getLocation().getZ());
                        lobbyC.set("lobby.Yaw", p.getLocation().getYaw());
                        lobbyC.set("lobby.Pitch", p.getLocation().getPitch());
                        try {
                            lobbyC.save(lobbyF);
                        } catch (IOException e) {
                            p.sendMessage(ChatColor.RED + "Não foi possível salvar as informações do lobby");
                            throw new RuntimeException(e);
                        }


                        sender.sendMessage(ChatColor.GRAY + "O lobby de seu servidor foi configurado com sucesso na coordenada "
                                + ChatColor.GREEN + data.getData().getInt("lobby.X") + ", "
                                + data.getData().getInt("lobby.Y") + ", "
                                + data.getData().getInt("lobby.Z") + ", " + ChatColor.GRAY + "no mundo "
                                + ChatColor.GREEN + data.getData().get("lobby.Mundo") + ".");
                        return true;
                    } else {
                        p.sendMessage(ChatColor.RED + "Você não pode executar este comando no mundo em que não está ativado pelo plugin!");
                        return true;
                    }
                } else {
                    if (XG7Lobby.mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                        if (mensagem.getMessage().getBoolean("AvisoEmActionBars")) {
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName())));
                        } else {
                            p.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
                        }
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[XG7Lobby] Esse comano só pode ser executado por players!");
            }
        }
        return true;
    }
}
