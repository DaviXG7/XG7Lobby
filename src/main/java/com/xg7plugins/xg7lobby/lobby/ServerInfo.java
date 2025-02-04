package com.xg7plugins.xg7lobby.lobby;

import com.xg7plugins.XG7Plugins;
import com.xg7plugins.xg7lobby.XG7Lobby;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Data
public class ServerInfo {

    private String name;
    private String address;
    private int port;
    private transient boolean chatLocked;

    public ServerInfo(XG7Lobby plugin) {
        this.name = plugin.getConfig("config").get("lobby-server-name", String.class).orElse("lobby");
        this.address = Bukkit.getIp();
        this.port = Bukkit.getPort();
    }

    private ServerInfo() {}

    public void connectPlayer(Player player) throws IOException {
        System.out.println("Conectando jogador " + player.getName() + " ao servidor " + name);
        if (!XG7Plugins.isBungeecord()) {
            System.out.println("O servidor não é bungeecord");
            return;
        }

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);
        out.writeUTF("Connect");
        out.writeUTF(name);
        out.flush(); // Garante que os dados sejam escritos corretamente

        System.out.println("Enviando mensagem BungeeCord: [Connect, " + name + "]");
        player.sendPluginMessage(XG7Lobby.getInstance(), "BungeeCord", byteArray.toByteArray());

        out.close();
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ServerInfo that = (ServerInfo) other;
        return port == that.port && name.equals(that.name) && address.equals(that.address);
    }

}
