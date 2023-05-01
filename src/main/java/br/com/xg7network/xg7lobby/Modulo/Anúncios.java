package br.com.xg7network.xg7lobby.Modulo;

import br.com.xg7network.xg7lobby.XG7Lobby;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Anúncios extends Module {

    public Anúncios(XG7Lobby plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[Módulo] [XG7 Lobby] Carregando Anúncios...");
        if (this.getPlugin().getConfig().getBoolean("Anúncios.ativado")) {
            ConfigurationSection anuncios = this.getPlugin().getConfig().getConfigurationSection("Anúncios.ParteDeAnúncios");
            if (anuncios != null) {
                List<String> secoes = new ArrayList<>(anuncios.getKeys(false));
                if (!secoes.isEmpty()) {
                    final String secaoAleatoria = secoes.get(ThreadLocalRandom.current().nextInt(secoes.size()));
                    final List<String> listaAnuncios = this.getPlugin().getConfig().getStringList("Anúncios.ParteDeAnúncios." + secaoAleatoria);
                    Bukkit.getScheduler().runTaskTimer(this.getPlugin(), () -> {
                        for (String anuncio : listaAnuncios) {
                            anuncio = ChatColor.translateAlternateColorCodes('&', anuncio);
                            char c = '&';
                            int contador = 0;
                            for (int i = 0; i < anuncio.length(); i++) {
                                if (anuncio.charAt(i) == c) {
                                    contador++;
                                }
                            }
                            if (anuncio.contains("[CENTRALIZAR]")) {
                                anuncio = anuncio.replace("[CENTRALIZAR]", "");
                                final int espacosEsquerda = ((40 + contador) - anuncio.length()) / 2;
                                System.out.println(espacosEsquerda);
                                final int espacosDireita = (40 + contador) - anuncio.length() - espacosEsquerda;
                                System.out.println(espacosDireita);
                                anuncio = " ".repeat(espacosEsquerda) + anuncio + " ".repeat(espacosDireita);
                            }
                            Bukkit.broadcastMessage(anuncio);
                        }
                    }, 0, this.getPlugin().getConfig().getInt("Anúncios.delay"));
                }
            }
        }
    }


    @Override
    public void onDisable() {

    }
}
