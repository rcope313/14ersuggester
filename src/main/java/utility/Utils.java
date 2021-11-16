package utility;

import models.FourteenerRoute;
import models.GradeQuality;
import mysql.MySqlConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

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

    public static boolean checkRowDateForUpdate (String routeUrl) {

        String query =
                "SELECT updateDate " +
                "FROM fourteener_routes " +
                "WHERE fourteener_routes.url = '" + routeUrl + "';";

        try (Statement stmt = MySqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                return checkDateWeekly(rs.getString("fourteener_routes.updateDate"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;


    }

    private static boolean checkDateWeekly (String calendarDate) {
        if (calendarDate.equals("businesslogic")) {
            return true;
        } else {
            return false;
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
        return coordinatesPhrase.split(": ")[1];
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





