package utility;

import models.GradeQuality;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static void main (String[] args) {
        System.out.print(convertRouteLengthIntoInteger("Navajo Lake: 9.5 miles RT"));
        System.out.print(convertRouteLengthIntoInteger("9.5 miles starting at Paris Mill"));

    }

    public static GradeQuality convertStringIntoGradeQuality(String gradeString) {
        GradeQuality resultGradeQuality = new GradeQuality();

        if (gradeString.split(" ")[1].equals("Difficult")) {
            resultGradeQuality.setGrade(Integer.valueOf(gradeString.split(" ")[3]));
            resultGradeQuality.setQuality("Difficult");

        }

        else if (gradeString.split(" ")[1].equals("Easy")) {
            resultGradeQuality.setGrade(Integer.valueOf(gradeString.split(" ")[3]));
            resultGradeQuality.setQuality("Easy");

        } else {
            resultGradeQuality.setGrade(Integer.valueOf(gradeString.split(" ")[2]));
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
            return Double.valueOf(stringArrayMi[0]);
        } else {
            return Double.valueOf(stringArrayMiles[0]);
        }

    }





}





