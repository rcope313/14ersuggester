package database.update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateUtils {

    public static boolean checkDateWeekly (String strDate) {
        if (strDate == null) {
            return false;
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate updateDate = convertStringToDate(strDate);
        LocalDate updateDatePlusOneWeek = updateDate.plusWeeks(1);

        if (currentDate.isAfter(updateDatePlusOneWeek) || currentDate.equals(updateDatePlusOneWeek)) {
            return true;
        } else {
            return false;
        }
    }

    private static LocalDate convertStringToDate (String strDate) {
        if (strDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            LocalDate date = LocalDate.parse(strDate, formatter);
            return date;
        } else {
            return null;
        }
    }
}
