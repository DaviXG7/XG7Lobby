package com.xg7plugins.xg7lobby.commands.implcommands.tocreatorcommands;

import com.cryptomorin.xseries.XMaterial;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BugsCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbybugreport";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.ANVIL.parseMaterial(), "§6Bug report command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Sends a bug report to the creator!";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbybugreport";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    private static long cooldownToSendOtherBug = 0;

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if (System.currentTimeMillis() < cooldownToSendOtherBug) {
            sender.sendMessage("§9[XG7§3Lob§bby] §cYou can only send one bug every 15 minutes!");
            return true;
        }
        sender.sendMessage("§9[XG7§3Lob§bby] §aYour bug will be sent, thank you for your help!");
        cooldownToSendOtherBug = System.currentTimeMillis() + 900000;
        new Thread(() -> {

            //Manda a atualização para o discord

            JsonObject embed = new JsonObject();
            embed.addProperty("title", sender.getName() + " reportou um bug");
            embed.addProperty("color", 0x00FFFF);

            JsonArray  fields = new JsonArray();
            JsonObject field1 = new JsonObject();
            field1.addProperty("name", "Versão do servidor:");
            field1.addProperty("value", "<:java:1252027840552108143> " + Bukkit.getVersion());
            field1.addProperty("inline", true);
            fields.add(field1);

            JsonObject field2 = new JsonObject();
            field2.addProperty("name", "IP do servidor:");
            field2.addProperty("value", Bukkit.getServer().getIp() + ":" + Bukkit.getServer().getPort());
            field2.addProperty("inline", true);
            fields.add(field2);

            JsonObject field3 = new JsonObject();
            field3.addProperty("name", "Conteúdo:");
            field3.addProperty("value", String.join(" ", args));
            field3.addProperty("inline", false);
            fields.add(field3);

            embed.add("fields", fields);

            JsonObject element = new JsonObject();
            element.addProperty("text", "Plugin - XG7Lobby");

            embed.add("footer",element);

            JsonArray array = new JsonArray();
            array.add(embed);

            JsonObject payload = new JsonObject();
            payload.addProperty("content", "BUG");
            payload.add("embeds", array);

            // Envia o payload ao webhook
            try {
                URL url = new URL("https://discord.com/api/webhooks/1206357532197785660/BxlaCHOAjOIG4T5UHh9RvxO5ifyENfJrnCuP84F2WTIMv9Ova2NQvkGUppK8RlMDlgkl");
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.addRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                OutputStream stream = connection.getOutputStream();
                stream.write(payload.toString().getBytes());
                stream.flush();
                stream.close();

                connection.getInputStream().close();
                connection.disconnect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();



        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
