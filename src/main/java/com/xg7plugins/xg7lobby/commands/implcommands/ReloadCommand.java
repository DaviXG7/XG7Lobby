package com.xg7plugins.xg7lobby.commands.implcommands;

import com.cryptomorin.xseries.XMaterial;
import com.xg7plugins.xg7lobby.cache.CacheManager;
import com.xg7plugins.xg7lobby.commands.Command;
import com.xg7plugins.xg7lobby.commands.PermissionType;
import com.xg7plugins.xg7lobby.commands.SubCommand;
import com.xg7plugins.xg7lobby.data.handler.Config;
import com.xg7plugins.xg7lobby.data.handler.SQLHandler;
import com.xg7plugins.xg7lobby.events.EventManager;
import com.xg7plugins.xg7lobby.scores.Bossbar;
import com.xg7plugins.xg7lobby.tasks.Task;
import com.xg7plugins.xg7lobby.tasks.TaskManager;
import com.xg7plugins.xg7lobby.tasks.tasksimpl.ScoreTask;
import com.xg7plugins.xg7lobby.utils.Text;
import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements Command {
    @Override
    public String getName() {
        return "xg7lobbyreload";
    }

    @Override
    public InventoryItem getIcon() {
        return new InventoryItem(XMaterial.NETHER_STAR.parseItem(), "§6Reload command", Arrays.asList("§9Description: §r" + getDescription(), "§9Usage: §7§o" + getSyntax(), "§9Permission: §b" + getPermission().getPerm()), 1, -1);
    }

    @Override
    public String getDescription() {
        return "Reloads the plugin";
    }

    @Override
    public String getSyntax() {
        return "/xg7lobbyreload [config, scores, db, all, cache, tasks]";
    }

    @Override
    public boolean isOnlyInLobbyWorld() {
        return false;
    }

    @Override
    public PermissionType getPermission() {
        return PermissionType.RELOAD;
    }

    @Override
    public boolean isOnlyPlayer() {
        return false;
    }

    @Override
    public List<SubCommand> getSubCommands() {
        return Arrays.asList(new ReloadAll(), new ReloadScores(), new ReloadDB(), new ReloadCache(), new ReloadTask(), new ReloadConfig(), new ReloadMenus());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return args.length == 1 ? Arrays.asList("config", "scores", "db", "all", "cache", "tasks", "menus") : new ArrayList<>();
    }

    static class ReloadAll implements SubCommand {

        @Override
        public String getName() {
            return "all";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.RELOAD;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Text.send("&bReloading...", sender);
            Config.reload();
            Config.reloadMenus();
            CacheManager.reloadAll();
            TaskManager.cancelAll();
            if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 9) {
                Bukkit.getOnlinePlayers().forEach(Bossbar::removePlayer);
            }
            TaskManager.initTimerTasks();
            Text.send("&aReloaded!", sender);

        }
    }
    static class ReloadScores implements SubCommand {

        @Override
        public String getName() {
            return "scores";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.RELOAD;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Text.send("&bReloading scores...", sender);

            TaskManager.cancelTask("xg7lscore");
            if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) >= 9) {
                Bukkit.getOnlinePlayers().forEach(Bossbar::removePlayer);
                Bukkit.getOnlinePlayers().forEach(Bossbar::addPlayer);
            }
            TaskManager.addTask(new ScoreTask());

            Text.send("&aReloaded!", sender);

        }
    }
    static class ReloadDB implements SubCommand {

        @Override
        public String getName() {
            return "db";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.RELOAD_DB;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Text.send("&bReloading database...", sender);
            SQLHandler.closeConnection();
            SQLHandler.connect();
            Text.send("&aReloaded!", sender);
        }
    }
    static class ReloadCache implements SubCommand {

        @Override
        public String getName() {
            return "cache";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.RELOAD_CACHE;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Text.send("&bReloading cache...", sender);
            CacheManager.reloadAll();
            Text.send("&aReloaded!", sender);
        }
    }
    static class ReloadTask implements SubCommand {

        @Override
        public String getName() {
            return "tasks";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.RELOAD_TASK;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Text.send("&bReloading tasks...", sender);
            TaskManager.cancelAll();
            TaskManager.initTimerTasks();
            Text.send("&aReloaded!", sender);
        }
    }
    static class ReloadConfig implements SubCommand {

        @Override
        public String getName() {
            return "config";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.RELOAD_CONFIG;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Text.send("&bReloading configs...", sender);
            Config.reload();
            Config.reloadMenus();
            EventManager.reload();
            Text.send("&aReloaded!", sender);
        }
    }
    static class ReloadMenus implements SubCommand {

        @Override
        public String getName() {
            return "menus";
        }

        @Override
        public PermissionType getPermission() {
            return PermissionType.RELOAD_MENUS;
        }

        @Override
        public void onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
            Text.send("&bReloading menus...", sender);
            Config.reloadMenus();
            Text.send("&aReloaded!", sender);
        }
    }
}
