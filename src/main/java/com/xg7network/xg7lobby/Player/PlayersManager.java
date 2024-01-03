package com.xg7network.xg7lobby.Player;

import com.google.gson.Gson;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class PlayersManager implements Listener {

    private static List<PlayerData> players = new ArrayList<>();

    public static PlayerData createData(Player player) {


        PlayerData playerData = new PlayerData(player);
        players.add(playerData);

        save();

        return playerData;

    }

    public static List<PlayerData> getDatas() {
        return players;
    }

    public static PlayerData getData(String id) {
        for (PlayerData data : players)
            if (data.getId().equalsIgnoreCase(id))
                return data;


        return null;
    }

    public static void deleteData(String id) {

        for (PlayerData data : players) {
            if (data.getId().equalsIgnoreCase(id)) {
                players.remove(data);
                return;
            }
        }
        save();
    }

    public static PlayerData update(String id, PlayerData playerData) {
        for (PlayerData data : players) {
            if (data.getId().equalsIgnoreCase(id)) {

                data.setMuted(playerData.isMuted());
                data.setInfractions(playerData.getInfractions());
                data.setLastDayToUnmute(playerData.getLastDayToUnmute());

                save();

                return data;
            }
        }
        return null;
    }


    public static void save() {

        try {

            Gson gson = new Gson();

            File file = new File(XG7Lobby.getPlugin().getDataFolder(), "data/playerdata.json");
            file.getParentFile().mkdir();
            file.createNewFile();

            Writer writer = new FileWriter(file, false);
            gson.toJson(players, writer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load() {

        try {

            Gson gson = new Gson();

            File file = new File(XG7Lobby.getPlugin().getDataFolder(), "data/playerdata.json");
            if (file.exists()) {
                Reader reader = new FileReader(file);
                PlayerData[] data = gson.fromJson(reader, PlayerData[].class);
                if (data != null) players = new ArrayList<>(Arrays.asList(data));

                Bukkit.getConsoleSender().sendMessage(prefix + "Player data loaded!");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (players.isEmpty()) createData(player).setFirstJoin(System.currentTimeMillis());
        else {
            if (getData(player.getUniqueId().toString()) == null) {
                createData(player).setFirstJoin(System.currentTimeMillis());
            }
        }

    }

}
