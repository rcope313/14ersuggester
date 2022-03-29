package database.dao;

import database.models.DatabaseConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Dao {
    final DatabaseConnection conn;

    public Dao(DatabaseConnection conn) {
        this.conn = conn;
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
