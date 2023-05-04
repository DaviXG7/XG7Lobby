package br.com.xg7network.xg7lobby.Comandos.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static br.com.xg7network.xg7lobby.XG7Lobby.action;
import static br.com.xg7network.xg7lobby.XG7Lobby.mensagem;
import static net.md_5.bungee.api.ChatMessageType.*;

public class CreativeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("xg7lobby.command.creative") ||
            p.hasPermission("xg7lobby.admin")) {
                if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                    p.setGameMode(GameMode.CREATIVE);
                    if (mensagem.getMessage().getBoolean("mensagens.ativar_mensagem_modo_de_jogo")) {
                        action.mandarAction(p, mensagem.getMessage().getString("mensagens.modo_criativo"));
                    }
                } else {
                    if (mensagem.getMessage().getBoolean("mensagens.ativar_mensagem_modo_de_jogo")) {
                        action.mandarAction(p, mensagem.getMessage().getString("VocêJáEstáNoModo").replace("[MODO]", p.getGameMode().toString()));
                    }
                }
            } else {
                if (mensagem.getMessage().getBoolean("mensagens.ativar_permissao_mensagem")) {
                    action.mandarAction(p, mensagem.getMessage().getString("mensagens.permissao_comandos").replace("[Comando]", "/" + command.getName()));
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "[XG7Lobby] Esse comano só pode ser executado por players!");
        }
        return true;
    }
}
