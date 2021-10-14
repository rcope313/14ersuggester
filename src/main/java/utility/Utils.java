package utility;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static void main (String[] args) {
        System.out.print(convertRouteLengthIntoMap("Navajo Lake: 9.5 miles RT"));
        System.out.print(convertRouteLengthIntoMap("9.5 miles starting at Paris Mill"));

    }


    public static Map<Integer,String> convertGradeIntoMap(String gradeString) {
        Map<Integer,String> gradeMap = new HashMap<>();

        if (gradeString.split(" ")[1].equals("Difficult")) {
            gradeMap.put(Integer.valueOf(gradeString.split(" ")[3]), "Difficult");

        }

        else if (gradeString.split(" ")[1].equals("Easy")) {
            gradeMap.put(Integer.valueOf(gradeString.split(" ")[3]), "Easy");

        } else {
            gradeMap.put(Integer.valueOf(gradeString.split(" ")[2]), "");
        }

        return gradeMap;
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

    public static Map<Integer,String> convertTotalGainIntoMap (String str) {

        Map<Integer,String> totalGainMap = new HashMap<>();

        for (String s : str.split("\n")) {
            String[] stringArray = s.split(" feet");

            if (stringArray.length == 1) {
                totalGainMap.put(convertElevationIntoInteger(stringArray[0]), "");
            } else {
                totalGainMap.put(convertElevationIntoInteger(stringArray[0]), stringArray[1].substring(1));
            }

        }

        return totalGainMap;


    }

    public static Map<Double,String> convertRouteLengthIntoMap (String str) {

        Map<Double, String> totalRouteLengthMap = new HashMap<>();

        for (String s : str.split("\n")) {
            String[] stringArrayMiles = s.split(" miles");
            String[] stringArrayMi = s.split(" mi");

            if (stringArrayMiles.length == 1) {
                if (stringArrayMi[1].length() == 3) {
                    totalRouteLengthMap.put(Double.valueOf(stringArrayMiles[0]), "");
                } else {
                    totalRouteLengthMap.put(Double.valueOf(stringArrayMi[0]), stringArrayMi[1].substring(1));
                }
            } else {
                totalRouteLengthMap.put(Double.valueOf(stringArrayMiles[0]), stringArrayMiles[1].substring(1));
            }

        }

        return totalRouteLengthMap;

    }

    public static double convertMileageStringToInteger(String mileage) {
        String excludeMiles = mileage.substring(0, mileage.length() - 6);
        return Double.valueOf(excludeMiles);


    }




}





