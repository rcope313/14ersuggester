package database.load;

public class LoadUtils {

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
