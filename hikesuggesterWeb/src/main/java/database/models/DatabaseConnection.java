package database.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost/hike_suggester";
    private static final String USER = "root";
    private static final String PASS = "root1234";

    public static Statement createStatement() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn.createStatement();
    }

    static boolean hasUpdateDateOverWeekAgo(String strDate) {
        if (strDate == null) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate updateDate = convertStringToDate(strDate);
        LocalDate updateDatePlusOneWeek = updateDate.plusWeeks(1);
        return currentDate.isAfter(updateDatePlusOneWeek) || currentDate.equals(updateDatePlusOneWeek);
    }

    static LocalDate convertStringToDate (String strDate) {
        if (strDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            return LocalDate.parse(strDate, formatter);
        } else {
            return null;
        }
    }
}

