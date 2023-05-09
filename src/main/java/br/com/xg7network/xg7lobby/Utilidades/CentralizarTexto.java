package br.com.xg7network.xg7lobby.Utilidades;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class CentralizarTexto {
    final static int max = 54;
    static int contador;
    final char c = '&';

    public String centralizarTexto(List<String> anuncioLista) {
        for (String anuncio: anuncioLista) {
            if (anuncio.contains("<centro>") && anuncio.contains("</centro>")) {
                for (int i = 0; i < anuncio.length(); i++) {
                    if (anuncio.charAt(i) == c) {
                        contador += 2;
                    }
                }
                anuncio = anuncio.replace("<centro>", "");
                anuncio = anuncio.replace("</centro>", "");
                anuncio = ChatColor.translateAlternateColorCodes('&', anuncio);

                int preenchimento = ((max + contador) - anuncio.length()) / 2;
                int extra = anuncio.length() % 2; // caso o tamanho da string seja ímpar, tem um caractere a mais para adiconar no final

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < preenchimento; i++) {
                    sb.append(' ');
                }

                sb.append(anuncio);

                for (int i = 0; i < preenchimento + extra; i++) {
                    sb.append(' ');
                }

                anuncio = sb.toString();

                contador = 0;
                return anuncio;
            } else {
                anuncio = ChatColor.translateAlternateColorCodes('&', anuncio);
                return anuncio;
            }
        }
        return "";
    }
}
