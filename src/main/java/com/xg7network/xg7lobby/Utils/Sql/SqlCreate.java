package com.xg7network.xg7lobby.Utils.Sql;

import com.xg7network.xg7lobby.Configs.ConfigType;

import java.sql.*;

import static com.xg7network.xg7lobby.XG7Lobby.configManager;
import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class SqlCreate {

    private Connection connection;

    public SqlCreate(SqlConnect sqlConnect) {

        this.connection = sqlConnect.getConnection();

    }

    public void createLobby() {
        if (!configManager.getConfig(ConfigType.CONFIG).getBoolean("sql.lobby")) return;

        try {

            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet table = metaData.getTables(null, null, "lobby", null);

            if (!table.next()) {

                System.out.println(prefix + "§6SQL: §rCreating lobby table:");

                PreparedStatement ps = connection.prepareStatement("CREATE TABLE lobby (world VARCHAR(50), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT);");
                ps.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    public void createPlayerData() {
        if (!configManager.getConfig(ConfigType.CONFIG).getBoolean("sql.lobby")) return;

        try {

            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet table = metaData.getTables(null, null, "lobby", null);

            if (!table.next()) {

                System.out.println(prefix + "§6SQL: §rCreating player data table:");

                PreparedStatement ps = connection.prepareStatement("CREATE TABLE playerdata (world VARCHAR(50), x DOUBLE, y DOUBLE, z DOUBLE, yaw FLOAT, pitch FLOAT);");
                ps.executeUpdate();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
