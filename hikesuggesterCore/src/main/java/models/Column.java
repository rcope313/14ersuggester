package models;

public enum Column {
    ROUTE_ID, ROUTE_NAME, MOUNTAIN_NAME, SNOW_ROUTE, STANDARD_ROUTE, GRADE, GRADE_QUALITY,
    START_ELEVATION, SUMMIT_ELEVATION, TOTAL_GAIN, ROUTE_LENGTH, EXPOSURE, ROCKFALL_POTENTIAL,
    ROUTE_FINDING, COMMITMENT, MULTIPLE_ROUTES, ROUTE_URL, TRAILHEAD;

    String getDatabaseColumn() {
        return switch (this) {
            case ROUTE_ID -> HikeSuggesterDatabase.FOURTEENER_ROUTE_ID;
            case ROUTE_NAME -> HikeSuggesterDatabase.ROUTE_NAME;
            case MOUNTAIN_NAME -> HikeSuggesterDatabase.MOUNTAIN_NAME;
            case SNOW_ROUTE -> HikeSuggesterDatabase.IS_SNOW_ROUTE;
            case STANDARD_ROUTE -> HikeSuggesterDatabase.IS_STANDARD_ROUTE;
            case GRADE -> HikeSuggesterDatabase.GRADE;
            case GRADE_QUALITY -> HikeSuggesterDatabase.GRADE_QUALITY;
            case START_ELEVATION -> HikeSuggesterDatabase.START_ELEVATION;
            case SUMMIT_ELEVATION -> HikeSuggesterDatabase.SUMMIT_ELEVATION;
            case TOTAL_GAIN -> HikeSuggesterDatabase.TOTAL_GAIN;
            case ROUTE_LENGTH -> HikeSuggesterDatabase.ROUTE_LENGTH;
            case EXPOSURE -> HikeSuggesterDatabase.EXPOSURE;
            case ROCKFALL_POTENTIAL -> HikeSuggesterDatabase.ROCKFALL_POTENTIAL;
            case ROUTE_FINDING -> HikeSuggesterDatabase.ROUTE_FINDING;
            case COMMITMENT -> HikeSuggesterDatabase.COMMITMENT;
            case MULTIPLE_ROUTES -> HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES;
            case ROUTE_URL -> HikeSuggesterDatabase.ROUTE_URL;
            case TRAILHEAD -> HikeSuggesterDatabase.TRAILHEAD_NAME;
        };
    }

    String getCliColumn() {
        return switch (this) {
            case ROUTE_ID -> "ID";
            case ROUTE_NAME -> "Route Name";
            case MOUNTAIN_NAME -> "Mountain Name";
            case SNOW_ROUTE -> "Snow Route";
            case STANDARD_ROUTE -> "Standard Route";
            case GRADE -> "Grade";
            case GRADE_QUALITY -> "Grade Quality";
            case START_ELEVATION -> "Start Elevation";
            case SUMMIT_ELEVATION -> "Summit Elevation";
            case TOTAL_GAIN -> "Total Gain";
            case ROUTE_LENGTH -> "Route Length";
            case EXPOSURE -> "Exposure";
            case ROCKFALL_POTENTIAL -> "Rockfall Potential";
            case ROUTE_FINDING -> "Route Finding";
            case COMMITMENT -> "Commitment";
            case MULTIPLE_ROUTES -> "Multiple Routes";
            case ROUTE_URL -> "Route URL";
            case TRAILHEAD -> "Trailhead";
        };
    }

    String getFormatString() {
        return switch (this) {
            case ROUTE_ID -> "%-10";
            case ROUTE_NAME -> "%-35";
            case MOUNTAIN_NAME -> "%-22";
            case SNOW_ROUTE, STANDARD_ROUTE, ROUTE_LENGTH, ROCKFALL_POTENTIAL, ROUTE_FINDING -> "%-15";
            case GRADE -> "%-7";
            case GRADE_QUALITY -> "%-17";
            case START_ELEVATION, SUMMIT_ELEVATION, MULTIPLE_ROUTES -> "%-20";
            case TOTAL_GAIN -> "%-12";
            case EXPOSURE, COMMITMENT -> "%-13";
            case ROUTE_URL -> "%-50";
            case TRAILHEAD -> "%-40";
        };
    }

    String getFormatRegex() {
        return switch (this) {
            case ROUTE_ID, ROUTE_NAME, MOUNTAIN_NAME, GRADE_QUALITY, EXPOSURE, ROCKFALL_POTENTIAL, ROUTE_FINDING, COMMITMENT, ROUTE_URL, TRAILHEAD -> "s";
            case SNOW_ROUTE, STANDARD_ROUTE, GRADE, START_ELEVATION, SUMMIT_ELEVATION, TOTAL_GAIN, MULTIPLE_ROUTES -> "d";
            case ROUTE_LENGTH -> "f";
        };
    }

}
