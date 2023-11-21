package com.xg7network.xg7lobby.Utils.Sql;

import com.xg7network.xg7lobby.Configs.ConfigType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;

public class SqlConnect {
    private final String HOST = configManager.getConfig(ConfigType.CONFIG).getString("sql.host");
    private final int PORT = configManager.getConfig(ConfigType.CONFIG).getInt("sql.port");
    private final String DATABASE = configManager.getConfig(ConfigType.CONFIG).getString("sql.database");
    private final String USER = configManager.getConfig(ConfigType.CONFIG).getString("sql.user");
    private final String PASS = configManager.getConfig(ConfigType.CONFIG).getString("sql.password");

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false", USER, PASS);
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
