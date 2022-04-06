package database.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost/hike_suggester?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }
}

