package com.xg7network.xg7lobby.Utils.Sql;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import static com.xg7network.xg7lobby.XG7Lobby.prefix;

public class SqlUtil {

    public static void set(Connection connection, String query, List<Object> values) {

        try {
            if (values != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                for (int i = 0; i < values.size(); i++) {
                    switch (values.get(i).getClass().getSimpleName()) {

                        case "Double":

                            preparedStatement.setDouble(i + 1, (Double) values.get(i));

                        case "String":

                            preparedStatement.setString(i + 1, (String) values.get(i));

                        case "Integer":

                            preparedStatement.setInt(i + 1, (Integer) values.get(i));

                        case "Float":

                            preparedStatement.setFloat(i + 1, (Float) values.get(i));

                        case "Boolean":

                            preparedStatement.setBoolean(i + 1, (Boolean) values.get(i));

                    }
                }

                preparedStatement.executeUpdate();
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public static Object get(Connection connection, String query, String value) {
        Object object;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                object = rs.getObject(value);
            } else {
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return object;
    }

    public static List<Object> get(Connection connection, String query, List<String> values) {
        List<Object> object = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                for (String s : values) object.add(rs.getObject(s));
            else
                return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return object;
    }
    public static List<Object> get(Connection connection, String query, List<String> values, List<Object> conditions) {
        List<Object> object = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            for (int i = 0; i < conditions.size(); i++) {
                switch (conditions.get(i).getClass().getSimpleName()) {

                    case "Double":

                        ps.setDouble(i + 1, (Double) conditions.get(i));

                    case "String":

                        ps.setString(i + 1, (String) conditions.get(i));

                    case "Integer":

                        ps.setInt(i + 1, (Integer) conditions.get(i));

                    case "Float":

                        ps.setFloat(i + 1, (Float) conditions.get(i));

                    case "Boolean":

                        ps.setBoolean(i + 1, (Boolean) conditions.get(i));

                }
            }
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                for (String s : values) object.add(rs.getObject(s));
            else
                return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return object;
    }


}
