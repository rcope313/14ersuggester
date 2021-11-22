package models;

public class CliColumnDesign {

    final public static CliColumn ROUTE_ID = new CliColumn
            (HikeSuggesterDatabase.FOURTEENER_ROUTE_ID, "ID", "%-10", "s");

    final public static CliColumn ROUTE_NAME = new CliColumn
            (HikeSuggesterDatabase.ROUTE_NAME, "Route Name", "%-35", "s");

    final public static CliColumn MOUNTAIN_NAME = new CliColumn
            (HikeSuggesterDatabase.MOUNTAIN_NAME, "Mountain Name", "%-25", "s");

    final public static CliColumn SNOW_ROUTE = new CliColumn
            (HikeSuggesterDatabase.IS_SNOW_ROUTE, "Snow Route", "%-15", "d");

    final public static CliColumn STANDARD_ROUTE = new CliColumn
            (HikeSuggesterDatabase.IS_STANDARD_ROUTE, "Standard Roue", "%-17", "d");

    final public static CliColumn GRADE = new CliColumn
            (HikeSuggesterDatabase.GRADE, "Grade", "%-10", "d");

    final public static CliColumn GRADE_QUALITY = new CliColumn
            (HikeSuggesterDatabase.GRADE_QUALITY, "Grade Quality", "%-17", "s");

    final public static CliColumn START_ELEVATION = new CliColumn
            (HikeSuggesterDatabase.START_ELEVATION, "Start Elevation", "%-20", "d");

    final public static CliColumn SUMMIT_ELEVATION = new CliColumn
            (HikeSuggesterDatabase.SUMMIT_ELEVATION, "Summit Elevation", "%-20", "d");

    final public static CliColumn TOTAL_GAIN = new CliColumn
            (HikeSuggesterDatabase.TOTAL_GAIN, "Total Gain", "%-17", "d");

    final public static CliColumn ROUTE_LENGTH = new CliColumn
            (HikeSuggesterDatabase.ROUTE_LENGTH, "Route Length", "%-17", "f");

    final public static CliColumn EXPOSURE = new CliColumn
            (HikeSuggesterDatabase.EXPOSURE, "Exposure", "%-20", "s");

    final public static CliColumn ROCKFALL_POTENTIAL = new CliColumn
            (HikeSuggesterDatabase.ROCKFALL_POTENTIAL, "Rockfall Potential", "%-20", "s");

    final public static CliColumn ROUTE_FINDING = new CliColumn
            (HikeSuggesterDatabase.ROUTE_FINDING, "Route Finding", "%-17", "s");

    final public static CliColumn COMMITMENT = new CliColumn
            (HikeSuggesterDatabase.COMMITMENT, "Commitment", "%-17", "s");

    final public static CliColumn MULTIPLE_ROUTES = new CliColumn
            (HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES, "Multiple Routes", "%-20", "d");

    final public static CliColumn ROUTE_URL = new CliColumn
            (HikeSuggesterDatabase.ROUTE_URL, "Route URL", "%-50", "s");

    final public static CliColumn TRAILHEAD = new CliColumn
            (HikeSuggesterDatabase.ROUTE_TRAILHEAD, "Trailhead", "%-40", "s");


}
