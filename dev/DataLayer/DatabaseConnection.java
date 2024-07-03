package dev.DataLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/DB";
    private static final String USER = "yourusername";
    private static final String PASSWORD = "yourpassword";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed())
            return connection;
        else connection =  DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }
}
