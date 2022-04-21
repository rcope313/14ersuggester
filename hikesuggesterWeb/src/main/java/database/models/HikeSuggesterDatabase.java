package database.models;

public class HikeSuggesterDatabase {
    public final static String HIKE_SUGGESTER = "hike_suggester";
    public final static String FOURTEENER_ROUTES = "fourteener_routes";
    public final static String FOURTEENER_ROUTE_ID = "fourteener_routes.fourteenerRouteId";
    public final static String ROUTE_NAME = "fourteener_routes.routeName";
    public final static String MOUNTAIN_NAME = "fourteener_routes.mountainName";
    public final static String IS_SNOW_ROUTE = "fourteener_routes.isSnowRoute";
    public final static String IS_STANDARD_ROUTE = "fourteener_routes.isStandardRoute";
    public final static String GRADE = "fourteener_routes.grade";
    public final static String GRADE_QUALITY = "fourteener_routes.gradeQuality";
    public final static String START_ELEVATION = "fourteener_routes.startElevation";
    public final static String SUMMIT_ELEVATION = "fourteener_routes.summitElevation";
    public final static String TOTAL_GAIN = "fourteener_routes.totalGain";
    public final static String ROUTE_LENGTH = "fourteener_routes.routeLength";
    public final static String EXPOSURE = "fourteener_routes.exposure";
    public final static String ROCKFALL_POTENTIAL = "fourteener_routes.rockfallPotential";
    public final static String ROUTE_FINDING = "fourteener_routes.routeFinding";
    public final static String COMMITMENT = "fourteener_routes.commitment";
    public final static String HAS_MULTIPLE_ROUTES = "fourteener_routes.hasMultipleRoutes";
    public final static String ROUTE_URL = "fourteener_routes.url";
    public final static String ROUTE_TRAILHEAD = "fourteener_routes.trailhead";
    public final static String ROUTE_UPDATE_DATE = "fourteener_routes.updateDate";
    public final static String TRAILHEADS = "trailheads";
    public final static String TRAILHEAD_ID = "trailheads.trailheadId";
    public final static String TRAILHEAD_NAME = "trailheads.name";
    public final static String COORDINATES = "trailheads.coordinates";
    public final static String ROAD_DIFFICULTY = "trailheads.roadDifficulty";
    public final static String ROAD_DESCRIPTION = "trailheads.roadDescription";
    public final static String WINTER_ACCESS = "trailheads.winterAccess";
    public final static String TRAILHEAD_URL = "trailheads.url";
    public final static String TRAILHEAD_UPDATE_DATE = "trailheads.updateDate";

    public static String getColumnNamesFourteenerRoutesTable() {
        return FOURTEENER_ROUTES +
                " ("
                + ROUTE_NAME.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + MOUNTAIN_NAME.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + IS_SNOW_ROUTE.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + IS_STANDARD_ROUTE.substring(FOURTEENER_ROUTES.length() + 1)+ ", "
                + GRADE.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + GRADE_QUALITY.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + START_ELEVATION.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + SUMMIT_ELEVATION.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + TOTAL_GAIN.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + ROUTE_LENGTH.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + EXPOSURE.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + ROCKFALL_POTENTIAL.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + ROUTE_FINDING.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + COMMITMENT.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + HAS_MULTIPLE_ROUTES.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + ROUTE_URL.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + ROUTE_TRAILHEAD.substring(FOURTEENER_ROUTES.length() + 1) + ", "
                + ROUTE_UPDATE_DATE.substring(FOURTEENER_ROUTES.length() + 1) + ")";
    }

    public static String getColumnNamesTrailheadsTable() {
        return TRAILHEADS +
                " ("
                + TRAILHEAD_NAME.substring(TRAILHEADS.length() + 1) + ", "
                + COORDINATES.substring(TRAILHEADS.length() + 1) + ", "
                + ROAD_DIFFICULTY.substring(TRAILHEADS.length() + 1) + ", "
                + ROAD_DESCRIPTION.substring(TRAILHEADS.length() + 1) + ", "
                + WINTER_ACCESS.substring(TRAILHEADS.length() + 1) + ", "
                + TRAILHEAD_URL.substring(TRAILHEADS.length() + 1) + ", "
                + TRAILHEAD_UPDATE_DATE.substring(TRAILHEADS.length() + 1) + ")";
    }

}
