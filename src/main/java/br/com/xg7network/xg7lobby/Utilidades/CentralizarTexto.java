package br.com.xg7network.xg7lobby.Utilidades;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class CentralizarTexto {
    final static int max = 40;
    static int contador;
    static char c = '&';

    public void centralizarTexto(List<String> anuncioLista) {
        for (String anuncio: anuncioLista) {
            if (anuncio.contains("<centro>") && anuncio.contains("</centro>")) {
                for (int i = 0; i < anuncio.length(); i++) {
                    if (anuncio.charAt(i) == c) {
                        contador += 2;
                    }
                }
                anuncio = anuncio.replace("<centro>", "");
                anuncio = anuncio.replace("</centro>", "");
                ChatColor.translateAlternateColorCodes('&', anuncio);
                int espacoEsquerdo = anuncio.length() - (max - contador);
                int espacoDireito = (max - contador) - anuncio.length() - espacoEsquerdo;
                String textoFormatado = String.format("%" + espacoEsquerdo + "s%s%" + espacoDireito + "s", "", anuncio, "");
                System.out.println(textoFormatado);
                contador = 0;
            } else {
                ChatColor.translateAlternateColorCodes('&', anuncio);
                Bukkit.broadcastMessage(anuncio);
            }
        }
    }
}
