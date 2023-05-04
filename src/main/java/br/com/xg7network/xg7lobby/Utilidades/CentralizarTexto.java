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
                int espacoEsquerdo =((max + contador) - anuncio.length()) / 2;
                int espacoDireito = (max + contador) - anuncio.length() - espacoEsquerdo;
                String textoCentralizado = String.format("%" + espacoEsquerdo + "s%s%" + espacoDireito + "s", "", anuncio, "");
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
