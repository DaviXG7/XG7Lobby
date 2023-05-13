package br.com.xg7network.xg7lobby.Comandos;

import net.md_5.bungee.api.chat.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.security.cert.CertPathBuilderResult;


public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (p.hasPermission("xg7lobby.admin")) {
                TextComponent adicionar = new TextComponent("§f Lista de comandos §8(/xg7lajuda comandos)");
                TextComponent texto = new TextComponent("§6Commandos§8: ");
                texto.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fAjuda a comandos").create()));
                texto.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xg7lajuda comandos"));
                texto.addExtra(adicionar);
                p.sendMessage("§8--------------------§9XG7Lobby§8--------------------");
                p.sendMessage("§8Use /lajuda [Seção] para saber mais");
                p.spigot().sendMessage(new BaseComponent[]{texto});
                if (args.length == 1) {
                    if (args[0].equals("comandos")) {
                        TextComponent Lobby = new TextComponent("§6Lobby§8: ");
                        TextComponent adicionarLobby = new TextComponent("§fComandos para lobby §8(/XG7lajuda comandos lobby)");
                        Lobby.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fClique aqui para ver comandos de lobby").create()));
                        Lobby.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xg7lajuda comandos lobby"));
                        TextComponent Mod = new TextComponent("§6Moderação§8: ");
                        TextComponent adicionarMod = new TextComponent("§fComandos para moderação §8(/xg7lajuda comandos mod)");
                        Mod.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fClique aqui para ver comandos de moderação").create()));
                        Mod.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xg7lajuda comandos mod"));
                        TextComponent gamemode = new TextComponent("§6Gamemode§8: ");
                        TextComponent adicionarGamemode = new TextComponent("§fComandos para gamemode §8(/lajuda comandos gamemode)");
                        gamemode.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§fClique aqui para ver comandos de gamemode").create()));
                        gamemode.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/xg7lajuda comandos gamemode"));
                        Lobby.addExtra(adicionarLobby);
                        Mod.addExtra(adicionarMod);
                        gamemode.addExtra(adicionarGamemode);

                        p.sendMessage("§8--------------------§eComandos§8--------------------");
                        p.spigot().sendMessage(new BaseComponent[]{Lobby});
                        p.spigot().sendMessage(new BaseComponent[]{Mod});
                        p.spigot().sendMessage(new BaseComponent[]{gamemode});
                        p.sendMessage("§8------------------------------------------------");
                    } else if (args[0].equals("Configs")) {

                    } else if (args[0].equals("Recursos")) {

                    }
                } else if (args.length == 2) {
                    if (args[0].equals("comandos")) {
                        if (args[1].equals("lobby")) {
                            p.sendMessage("§8--------------------§7Lobby§8--------------------");
                            p.sendMessage("§6/setlobby: §fSalva o local do lobby.");
                            p.sendMessage("§6/lobby: §fTe teletransporta para o lobby.");
                            p.sendMessage("§6/fly: §fFaz você voar, precisa de permissão!");
                            p.sendMessage("§6/vanish: §fEsconde você de todos os outros jogadores.");
                            p.sendMessage("§6/xg7lreload: §fRecarrega o plugin.");
                            p.sendMessage("§fFunciona só em outro mundo.");
                            p.sendMessage("§8---------------------------------------------");

                        } else if (args[1].equals("mod")) {
                            p.sendMessage("§8--------------------§7Moderação§8--------------------");
                            p.sendMessage("§6/ban: §fBane jogadores.");
                            p.sendMessage("§6/kick: §fExpulsa jogadores.");
                            p.sendMessage("§6/mute: §fDeixa os jogadores mudos ao chat");
                            p.sendMessage("§6/tempban: §fBane jogadores temporariamente.");
                            p.sendMessage("§6/unban: §fDesbane jogadores.");
                            p.sendMessage("§6/unmute: §fDesmuta jogadores.");
                            p.sendMessage("§8--------------------------------------------------");

                        } else if (args[1].equals("gamemode")) {
                            p.sendMessage("§8--------------------§7Modo de jogo§8--------------------");
                            p.sendMessage("§6/gamemode: §fAltera o modo de jogo de um jogador: ");
                            p.sendMessage("§6survival: &6para sobrevivência.");
                            p.sendMessage("§6creative: &6para criativo.");
                            p.sendMessage("§6adventure: &6para aventura.");
                            p.sendMessage("§6spectator: &6para espectador");
                            p.sendMessage("§8----------------------------------------------------");

                        }
                    }
                }
            } else {

            }
        } else {

        }
        return true;
    }
}
