package utility;

import models.GradeQuality;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {



    public static boolean convertIntToBoolean (int x) {
        if (x == 0) {
            return false;
        }
        if (x == 1) {
            return true;
        } else {
            throw new IllegalArgumentException("Value neither 0 or 1");
        }
    }

    public static ArrayList<Integer> convertArrayToArrayList (Integer[] array) {
        if (array == null) {
            return null;
        } else {
            return new ArrayList<>(Arrays.asList(array));
        }
    }

    public static ArrayList<String> convertArrayToArrayList (String[] array) {
        if (array == null) {
            return null;
        } else {
            return new ArrayList<>(Arrays.asList(array));
        }

    }

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

    public static GradeQuality convertStringIntoGradeQuality(String gradeString) {
        GradeQuality resultGradeQuality = new GradeQuality();

        if (gradeString.split(" ")[1].equals("Difficult")) {
            resultGradeQuality.setGrade(Integer.parseInt(gradeString.split(" ")[3]));
            resultGradeQuality.setQuality("Difficult");

        }

        else if (gradeString.split(" ")[1].equals("Easy")) {
            resultGradeQuality.setGrade(Integer.parseInt(gradeString.split(" ")[3]));
            resultGradeQuality.setQuality("Easy");

        } else {
            resultGradeQuality.setGrade(Integer.parseInt(gradeString.split(" ")[2]));
            resultGradeQuality.setQuality("");
        }

        return resultGradeQuality;
    }

    public static int convertStartAndSummitElevationStringToInteger(String elevation) {

        String elevationWithoutFeet = elevation.substring(0, elevation.length() - 5);
        char[] charArray = elevationWithoutFeet.toCharArray();

        if (charArray.length < 5) {
            return (Character.getNumericValue(charArray[0]) * 100)
                    + (Character.getNumericValue(charArray[1]) * 10)
                    + Character.getNumericValue(charArray[2]);
        }

        else if (charArray.length < 6) {
            return (Character.getNumericValue(charArray[0]) * 1000)
                    + (Character.getNumericValue(charArray[2]) * 100)
                    + (Character.getNumericValue(charArray[3]) * 10)
                    + Character.getNumericValue(charArray[4]);
        } else {
            return (Character.getNumericValue(charArray[0]) * 10000)
                    + (Character.getNumericValue(charArray[1]) * 1000)
                    + (Character.getNumericValue(charArray[3]) * 100)
                    + (Character.getNumericValue(charArray[4]) * 10)
                    + Character.getNumericValue(charArray[5]);

        }
    }

    public static int convertElevationIntoInteger (String str) {
       char[] charArray = str.toCharArray();

        if (charArray.length < 5) {
            return (Character.getNumericValue(charArray[0]) * 100)
                    + (Character.getNumericValue(charArray[1]) * 10)
                    + Character.getNumericValue(charArray[2]);
        }

        else if (charArray.length < 6) {
            return (Character.getNumericValue(charArray[0]) * 1000)
                    + (Character.getNumericValue(charArray[2]) * 100)
                    + (Character.getNumericValue(charArray[3]) * 10)
                    + Character.getNumericValue(charArray[4]);
        } else {
            return (Character.getNumericValue(charArray[0]) * 10000)
                    + (Character.getNumericValue(charArray[1]) * 1000)
                    + (Character.getNumericValue(charArray[3]) * 100)
                    + (Character.getNumericValue(charArray[4]) * 10)
                    + Character.getNumericValue(charArray[5]);

        }

    }

    public static int convertTotalGainIntoInteger(String str) {

        if (str.split("\n").length > 1) {
            return 0;
        }

        String[] stringArray = str.split(" feet");
        return convertElevationIntoInteger(stringArray[0]);

    }

    public static double convertRouteLengthIntoInteger(String str) {

        if (str.split("\n").length > 1) {
            return 0;
        }
        String[] stringArrayMiles = str.split(" miles");
        String[] stringArrayMi = str.split(" mi");

        if (stringArrayMiles.length == 1) {
            return Double.parseDouble(stringArrayMi[0]);
        } else {
            return Double.parseDouble(stringArrayMiles[0]);
        }

    }

    public static String convertCoordinatesPhraseToCoordinates (String coordinatesPhrase) {
        return coordinatesPhrase.split(": ")[1].trim();
    }

    public static int convertRoadDifficultyPhraseToInt(String roadDifficultyPhrase) {
        return Integer.parseInt(roadDifficultyPhrase.split(" ")[1]);
    }

    public static String insertApostrophe (String str) {

        String resultString = "";

        for (Character c : str.toCharArray()) {
            if (c.charValue() == 39) {
                resultString = resultString + "''";
            } else {
                resultString = resultString + c;

            }
        }

        return resultString;

    }

}





