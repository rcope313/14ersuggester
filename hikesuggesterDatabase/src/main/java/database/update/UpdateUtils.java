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
        return currentDate.isAfter(updateDatePlusOneWeek) || currentDate.equals(updateDatePlusOneWeek);
    }

    private static LocalDate convertStringToDate (String strDate) {
        if (strDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            return LocalDate.parse(strDate, formatter);
        } else {
            return null;
        }
    }
}
