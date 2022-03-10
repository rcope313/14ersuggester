package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost/hike_suggester";
    private static final String USER = "root";
    private static final String PASS = "root1234";

    public static Statement createStatement() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn.createStatement();
    }
}

