package com.xg7plugins.xg7lobby.tasks.tasksimpl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.tasks.Task;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WarnVersion extends Task {

    private static final String THIS_VERSION = "1.2-RETURN";

    public WarnVersion() {
        super("xg7lwarnversion", 6000);
    }

    @SneakyThrows
    @Override
    public void run() {
        URL url = new URL("https://raw.githubusercontent.com/DaviXG7/XG7Lobby/master/src/version.json");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
            builder.append(line);
        String json = builder.toString();

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        String versaoMaisRecente = jsonObject.get("version").getAsString();
        if (!versaoMaisRecente.equals(THIS_VERSION)) {
            Bukkit.getLogger().warning("[XG7Lobby] A new version of XG7Lobby has been Founded! Please update to: " + versaoMaisRecente);
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.isOp()) {
                    p.sendMessage("\n§9[XG7§3Lob§bby] §8| §cA new version of XG7Lobby has been Founded! Please update to: " + versaoMaisRecente + "\n");
                }
            });
        }
    }
}
