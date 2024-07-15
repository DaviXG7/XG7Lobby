package com.xg7plugins.xg7lobby.events.actions;

import com.xg7plugins.xg7lobby.utils.Log;
import com.xg7plugins.xg7lobby.utils.Text;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class Action {

    public static void execute(String text, Player player) {
        ActionType type = null;
        for (ActionType actionType : ActionType.values()) {
            if (text.startsWith("[" + actionType.name() + "] ")) {
                type = actionType;
                text = text.replace("[" + actionType.name() + "] ", "");
                break;
            }
        }
        if (type == null) return;
        if (text.startsWith("[PERMISSION: ")) {
            String perm = text.substring(13, text.indexOf("] "));
            if (!player.hasPermission(perm)) return;
            text = text.replace("[PERMISSION: " + perm + "] ", "");
        }
        if (text.startsWith("[!PERMISSION: ")) {
            String perm = text.substring(14, text.indexOf("] "));
            if (player.hasPermission(perm)) return;
            text = text.replace("[!PERMISSION: " + perm + "] ", "");
        }

        text = text.replace("[PLAYER]", player.getName());

        String[] textSplited = text.split(", ");

        switch (type) {
            case TITLE:

                if (textSplited.length != 2 && textSplited.length != 5) {
                    Log.severe("Action TITLE needs 5 arguments: title, subtitle Optional: (fade in ticks, stay time ticks, fade out ticks)");
                    return;
                }

                if (textSplited.length == 2) {
                    player.sendTitle(Text.getFormatedText(player, textSplited[0]), Text.getFormatedText(player, textSplited[1]));
                    return;
                }
                player.sendTitle(Text.getFormatedText(player, textSplited[0]), Text.getFormatedText(player, textSplited[1]), Integer.parseInt(textSplited[2]), Integer.parseInt(textSplited[3]), Integer.parseInt(textSplited[4]));

                return;
            case OPEN:
                break;
            case CLOSE:
                break;

            case GAMEMODE:

                player.setGameMode(GameMode.valueOf(text.toUpperCase()));

                return;
            case TP:

                if (textSplited.length != 6) {
                    Log.severe("Action TP needs 6 arguments: world, x, y, z, yaw, pitch");
                    return;
                }
                player.teleport(new Location(Bukkit.getWorld(textSplited[0]), Double.parseDouble(textSplited[1]), Double.parseDouble(textSplited[2]), Double.parseDouble(textSplited[3]), Float.parseFloat(textSplited[4]), Float.parseFloat(textSplited[5])));

                return;
            case BROADCAST:

                Bukkit.broadcastMessage(Text.getFormatedText(player, text));

                return;
            case SUMMON:

                player.getWorld().spawnEntity(player.getLocation(), EntityType.valueOf(text.toUpperCase()));

                return;
            case EFFECT:

                if (textSplited.length != 3) {
                    Log.severe("Action EFFECT needs 3 arguments: potion effect type, duration, amplifier");
                    return;
                }

                player.addPotionEffect(new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(textSplited[0])), Integer.parseInt(textSplited[1]), Integer.parseInt(textSplited[2]) - 1));

                return;
            case COMMAND:

                player.performCommand(text);

                return;
            case CONSOLE:

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), text);

                return;
            case FLY:
                break;
            case MESSAGE:

                Text.send(text, player);

                return;
            case SOUND:

                if (textSplited.length != 3) {
                    Log.severe("Action SOUND needs 3 arguments: sound type, volume, pitch");
                    return;
                }

                player.playSound(player.getLocation(), Sound.valueOf(textSplited[0]), Float.parseFloat(textSplited[1]), Float.parseFloat(textSplited[2]));

                return;
            case SWAP:
                break;
            case HIDE:
                break;
            case SHOW:
                break;
            case FIREWORK:

                if (textSplited.length != 6) {
                    Log.severe("Action FIREWORK needs 6 arguments: type, hex: color, hex: colorfade, logic: trail, logic: flicker, power");
                    return;
                }

                Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);

                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                FireworkEffect.Builder builder = FireworkEffect.builder();
                builder.with(FireworkEffect.Type.valueOf(textSplited[0]));
                builder.withColor(Color.fromRGB(Integer.parseInt(textSplited[1].replace("#", ""), 16)));
                builder.withFade(Color.fromRGB(Integer.parseInt(textSplited[2].replace("#", ""), 16)));
                builder.trail(Boolean.parseBoolean(textSplited[3]));
                builder.flicker(Boolean.parseBoolean(textSplited[4]));

                FireworkEffect effect = builder.build();
                fireworkMeta.addEffect(effect);
                fireworkMeta.setPower(Integer.parseInt(textSplited[5]));

                firework.setFireworkMeta(fireworkMeta);

                return;
        }
    }

}
