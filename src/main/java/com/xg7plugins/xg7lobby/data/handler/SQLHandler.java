package com.xg7plugins.xg7lobby.data.handler;

import com.xg7plugins.xg7lobby.data.ConfigType;
import com.xg7plugins.xg7lobby.XG7Lobby;
import com.xg7plugins.xg7lobby.utils.Log;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLHandler {

    private static Connection connection;

    private static final String HOST = Config.getString(ConfigType.CONFIG, "sql.host");
    private static final int PORT = Config.getInt(ConfigType.CONFIG, "sql.port");
    private static final String DATABASE = Config.getString(ConfigType.CONFIG, "sql.database");
    private static final String USER = Config.getString(ConfigType.CONFIG, "sql.user");
    private static final String PASS = Config.getString(ConfigType.CONFIG, "sql.pass");

    @SneakyThrows
    public static void connect() {

        Log.info("Conecting to the database...");

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USER, PASS);
            Log.fine("Successful conected!");
        } catch (SQLException e) {
            Log.severe("Unable to connect to the database using the default sqllite database!");

            Class.forName("org.sqlite.JDBC");

            File file = new File(XG7Lobby.getPlugin().getDataFolder(), "playerdata.db");

            if (!file.exists()) file.createNewFile();

            connection = DriverManager.getConnection("jdbc:sqlite:" + XG7Lobby.getPlugin().getDataFolder().getPath() + "/playerdata.db");

            Log.fine("SQLite connection created!");

        }

        Log.loading("Loading tables...");

        connection.prepareStatement("CREATE TABLE IF NOT EXISTS players(id TEXT PRIMARY KEY, isplayershide BOOLEAN, ismuted BOOLEAN, isbuildenabled BOOLEAN, isflying BOOLEAN, ispvpenabled BOOLEAN, timeforunmute BIGINT, firstJoin BIGINT)").executeUpdate();
        connection.prepareStatement("CREATE TABLE IF NOT EXISTS warns(playerid TEXT, level INT, warnid TEXT PRIMARY KEY, warn TEXT, whenw BIGINT)").executeUpdate();

        Log.loading("Loaded!");
    }

    @SneakyThrows
    public static void closeConnection() {
        connection.close();
        connection = null;
    }

    @SneakyThrows
    public synchronized static List<Map<String, Object>> select(String sql, Object... args) {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    List<Map<String, Object>> list = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();

                        for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++){
                            map.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                        }
                        list.add(map);
                    }
                    return list;
                }

            }

    }

    @SneakyThrows
    public synchronized static int update(String sql, Object... args) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        }
    }

    @SneakyThrows
    public synchronized static int delete(String sql, Object... args) {
        return update(sql, args);
    }

}
