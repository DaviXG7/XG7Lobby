package com.xg7network.xg7lobby.Player;

import com.xg7network.xg7lobby.Configs.ConfigType;
import com.xg7network.xg7lobby.XG7Lobby;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class PlayersManager {

    private static Connection connection;

    public static void connect() {
        XG7Lobby.getPlugin().getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Connecting to the database...");
        try {

            Class.forName("org.sqlite.JDBC");

            String HOST = configManager.getConfig(ConfigType.CONFIG).getString("sql.host");
            int PORT = configManager.getConfig(ConfigType.CONFIG).getInt("sql.port");
            String DATABASE = configManager.getConfig(ConfigType.CONFIG).getString("sql.database");
            String USER = configManager.getConfig(ConfigType.CONFIG).getString("sql.user");
            String PASS = configManager.getConfig(ConfigType.CONFIG).getString("sql.pass");

            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USER, PASS);

            XG7Lobby.getPlugin().getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "Connection to the database completed successfully");


        } catch (SQLException ignored) {
            XG7Lobby.getPlugin().getServer().getConsoleSender().sendMessage(prefix + ChatColor.RED + "Unable to connect to database! Using the plugin's generic default database");
            File file = new File(XG7Lobby.getPlugin().getDataFolder(), "playerdata.db");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + XG7Lobby.getPlugin().getDataFolder().getPath() + "/playerdata.db");
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS players (id VARCHAR(255) PRIMARY KEY, playershide BOOLEAN, muted BOOLEAN, lasttounmute BIGINT, firstJoin BIGINT)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS warns (playerid VARCHAR(255), id VARCHAR(255) PRIMARY KEY, warn VARCHAR(255), whenw BIGINT)").execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnect() throws SQLException {
        if (connection != null) connection.close();
    }

    public static PlayerData createData(Player player) {


        PlayerData playerData = new PlayerData(player);

        try {

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM players WHERE id = ?");
            preparedStatement1.setString(1, playerData.getId());

            ResultSet resultSet = preparedStatement1.executeQuery();

            if (!resultSet.next()) {

                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (id, playershide, muted, lasttounmute, firstJoin) VALUES (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, playerData.getId());
                preparedStatement.setBoolean(2, playerData.isPlayershide());
                preparedStatement.setBoolean(3, playerData.isMuted());
                preparedStatement.setLong(4, playerData.getLastDayToUnmute());
                preparedStatement.setLong(5, playerData.getFirstJoinLong());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getData(playerData.getId());

    }

    public static PlayerData getData(String id) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE id = ?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM warns WHERE playerid = ?");
            preparedStatement.setString(1, id);
            preparedStatement2.setString(1, id);
            ResultSet set = preparedStatement.executeQuery();
            ResultSet set2 = preparedStatement2.executeQuery();

            List<Warn> warns = new ArrayList<>();
            while (set2.next()) {
                warns.add(new Warn(set2.getString(1), set2.getString(2), set2.getString(3), set2.getLong(4)));
            }

            if (set.next()) {
                return new PlayerData(set.getString("id"),set.getBoolean(2), set.getBoolean(3), warns, set.getLong(4), set.getLong(5));

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return null;
    }

    public static void deleteData(String id) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM players WHERE id = ?");
            preparedStatement.setString(1, id);
            preparedStatement.execute();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteWarn(String playerid, String id) {
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM warns WHERE id = ? AND playerid = ?");
            preparedStatement1.setString(1, id);
            preparedStatement1.setString(2, playerid);

            return preparedStatement1.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void update(String id, PlayerData playerData) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET playershide = ?, muted = ?, lasttounmute = ?, firstJoin = ? WHERE id = ?");
            preparedStatement.setString(5, id);
            preparedStatement.setBoolean(1, playerData.isPlayershide());
            preparedStatement.setBoolean(2, playerData.isMuted());
            preparedStatement.setLong(3, playerData.getLastDayToUnmute());
            preparedStatement.setLong(4, playerData.getFirstJoinLong());
            preparedStatement.execute();

            for (Warn warn : playerData.getInfractions()) {

                PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM warns WHERE id = ?");
                preparedStatement1.setString(1, warn.getId());

                ResultSet resultSet = preparedStatement1.executeQuery();

                if (!resultSet.next()) {
                    PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO warns (playerid, id, warn, whenw) VALUES (?, ?, ?, ?);");
                    preparedStatement2.setString(1, playerData.getId());
                    preparedStatement2.setString(2, warn.getId());
                    preparedStatement2.setString(3, warn.getWarn());
                    preparedStatement2.setLong(4, warn.getWhenInMills());
                    preparedStatement2.executeUpdate();
                }

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PlayerData> getDatas() {
        try {
            ResultSet set = connection.prepareStatement("SELECT * FROM players").executeQuery();

            List<PlayerData> dataList = new ArrayList<>();
            while (set.next()) {
                dataList.add(new PlayerData(set.getString("id"),set.getBoolean(2), set.getBoolean(3), null, set.getLong(4), set.getLong(5)));
            }

            return dataList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
