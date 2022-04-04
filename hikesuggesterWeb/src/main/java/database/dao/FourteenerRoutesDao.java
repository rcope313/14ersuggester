package database.dao;

import database.models.ImmutableFetchedRoute;
import database.models.ImmutableStoredRoute;
import database.models.HikeSuggesterDatabase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FourteenerRoutesDao extends Dao {

    public FourteenerRoutesDao(Connection conn) {
        super(conn);
    }

    public void insert(ImmutableFetchedRoute route) {
        if (route != null) {
            try {
                conn.createStatement().execute(insertQuery(route));
                System.out.print("MountainName: " + route.getMountainName() + "\n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");
                System.out.println("ENTRY CREATED \n");
            } catch (Exception e) {
                System.out.print("MountainName: " + route.getMountainName() + "\n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");
                System.out.print("ENTRY IGNORED \n");
            }
        }
    }

    public ImmutableStoredRoute get(ImmutableFetchedRoute route) {
        String getQuery = getQuery(route);
        ImmutableStoredRoute storedRoute = null;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(getQuery);
            while (rs.next()) {
                storedRoute = buildImmutableStoredRoute(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (storedRoute == null) {
            throw new IllegalStateException("Route not in database.");
        }
        return storedRoute;
    }

    private static String getQuery(ImmutableFetchedRoute route) {
        return "SELECT *" +
                " FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " = '" + route.getUrl() +"' ";
    }

    private static String insertQuery(ImmutableFetchedRoute route) {
        return "INSERT INTO " +
                HikeSuggesterDatabase.getColumnNamesFourteenerRoutesTable() +
                " VALUES ('" +
                route.getRouteName() + "', '" +
                route.getMountainName() + "', " +
                route.getIsSnowRoute() + ", " +
                route.getIsStandardRoute() + ", " +
                route.getGrade() + ", '" +
                route.getGradeQuality() + "', " +
                route.getStartElevation() + ", " +
                route.getSummitElevation() + ", " +
                route.getTotalGain() + ", " +
                route.getRouteLength() + ", '" +
                route.getExposure() + "', '" +
                route.getRockfallPotential() + "', '" +
                route.getRouteFinding() + "', '" +
                route.getCommitment() + "', " +
                route.getHasMultipleRoutes() + ", '" +
                route.getUrl() + "', '" +
                route.getTrailhead() + "', '" +
                java.time.LocalDate.now() + "')";
    }

    private static ImmutableStoredRoute buildImmutableStoredRoute(ResultSet rs) throws SQLException {
        return ImmutableStoredRoute.builder()
                .id(rs.getInt(HikeSuggesterDatabase.FOURTEENER_ROUTE_ID))
                .mountainName(rs.getString(HikeSuggesterDatabase.MOUNTAIN_NAME))
                .routeName(rs.getString(HikeSuggesterDatabase.ROUTE_NAME))
                .isSnowRoute(rs.getInt(HikeSuggesterDatabase.IS_SNOW_ROUTE) == 1)
                .isStandardRoute(rs.getInt(HikeSuggesterDatabase.IS_STANDARD_ROUTE) == 1)
                .grade(rs.getInt(HikeSuggesterDatabase.GRADE))
                .gradeQuality(rs.getString(HikeSuggesterDatabase.GRADE_QUALITY))
                .trailhead(rs.getString(HikeSuggesterDatabase.ROUTE_TRAILHEAD))
                .startElevation(rs.getInt(HikeSuggesterDatabase.START_ELEVATION))
                .summitElevation(rs.getInt(HikeSuggesterDatabase.SUMMIT_ELEVATION))
                .totalGain(rs.getInt(HikeSuggesterDatabase.TOTAL_GAIN))
                .routeLength(rs.getDouble(HikeSuggesterDatabase.ROUTE_LENGTH))
                .exposure(rs.getString(HikeSuggesterDatabase.EXPOSURE))
                .rockfallPotential(rs.getString(HikeSuggesterDatabase.ROCKFALL_POTENTIAL))
                .routeFinding(rs.getString(HikeSuggesterDatabase.ROUTE_FINDING))
                .commitment(rs.getString(HikeSuggesterDatabase.COMMITMENT))
                .hasMultipleRoutes(rs.getInt(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES) == 1)
                .url(rs.getString(HikeSuggesterDatabase.ROUTE_URL))
                .updateDate(rs.getString(HikeSuggesterDatabase.ROUTE_UPDATE_DATE))
                .build();
    }
}
