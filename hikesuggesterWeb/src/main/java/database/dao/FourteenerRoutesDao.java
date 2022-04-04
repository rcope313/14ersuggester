package database.dao;

import com.gargoylesoftware.htmlunit.WebClient;
import database.models.ImmutableFetchedRoute;
import database.models.ImmutableStoredRoute;
import database.models.HikeSuggesterDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webscraper.FourteenerRouteScraper;
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

    public void update(ImmutableFetchedRoute route) throws Exception {
        ImmutableStoredRoute storedRoute = get(route);
        if (hasUpdateDateOverWeekAgo(storedRoute.getUpdateDate())) {
            ImmutableFetchedRoute updatedRoute = new FourteenerRouteScraper(new WebClient()).scrapeImmutableFetchedRoute(storedRoute.getUrl());
            conn.createStatement().execute(updateQuery(updatedRoute));
        }
    }

    private static String getQuery(ImmutableFetchedRoute route) {
        return "SELECT *" +
                " FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " = '" + route.getUrl() +"' ";
    }

    private static String updateQuery(ImmutableFetchedRoute route)  {
        if (route == null) {
            return "SELECT * FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES;
        } else {
            return "UPDATE "
                    + HikeSuggesterDatabase.FOURTEENER_ROUTES + "\n" +
                    "SET " + HikeSuggesterDatabase.ROUTE_NAME + " = '" + route.getRouteName() + "', \n" +
                    HikeSuggesterDatabase.MOUNTAIN_NAME + " = '" + route.getMountainName() + "', \n" +
                    HikeSuggesterDatabase.IS_SNOW_ROUTE + " = " + route.getIsSnowRoute() + ", \n" +
                    HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = " + route.getIsStandardRoute() + ", \n" +
                    HikeSuggesterDatabase.GRADE + " = " + route.getGrade() + ", \n" +
                    HikeSuggesterDatabase.GRADE_QUALITY + " = '" + route.getGradeQuality() + "', \n" +
                    HikeSuggesterDatabase.START_ELEVATION + " = " + route.getStartElevation() + ", \n" +
                    HikeSuggesterDatabase.SUMMIT_ELEVATION + " = " + route.getSummitElevation() + ", \n" +
                    HikeSuggesterDatabase.TOTAL_GAIN + " = " + route.getTotalGain() + ", \n" +
                    HikeSuggesterDatabase.ROUTE_LENGTH + " = " + route.getRouteLength() + ", \n" +
                    HikeSuggesterDatabase.ROCKFALL_POTENTIAL + " = '" + route.getRockfallPotential() + "', \n" +
                    HikeSuggesterDatabase.ROUTE_FINDING + " = '" + route.getRouteFinding() + "', \n" +
                    HikeSuggesterDatabase.COMMITMENT + " = '" + route.getCommitment() + "', \n" +
                    HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = " + route.getHasMultipleRoutes() + ", \n" +
                    HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = '" + route.getTrailhead() + "', \n" +
                    HikeSuggesterDatabase.ROUTE_UPDATE_DATE + " = '" + java.time.LocalDate.now() + "' \n" +
                    "WHERE " + HikeSuggesterDatabase.ROUTE_URL + " = '" + route.getUrl() + "';";
        }
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
