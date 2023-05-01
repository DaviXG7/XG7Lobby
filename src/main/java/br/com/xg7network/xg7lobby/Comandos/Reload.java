package br.com.xg7network.xg7lobby.Comandos;

import br.com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;

public class Reload implements CommandExecutor {

    private XG7Lobby pl;

    public Reload(XG7Lobby pl) {
        this.pl = pl;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("xg7lobby.admin")) {
            sender.sendMessage(ChatColor.GREEN + "[XG7 Lobby] Recarregando o plugin...");
            Bukkit.getPluginManager().disablePlugin(pl);
            Bukkit.getPluginManager().enablePlugin(pl);
            sender.sendMessage(ChatColor.GREEN + "[XG7 Lobby] O plugin foi recaregado com sucesso!");
        } else {
            sender.sendMessage(mensagem.getMessage().getString("mensagens.permissao_comandos").replace("&", "§").replace("[Comando]", "/" + command.getName()));
        }
        return true;
    }
}
