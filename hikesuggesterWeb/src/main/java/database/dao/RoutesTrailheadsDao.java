package database.dao;

import com.gargoylesoftware.htmlunit.WebClient;
import database.models.CompareQuery;
import database.models.HikeSuggesterDatabase;
import database.models.ImmutableFetchedRoute;
import database.models.ImmutableFetchedTrailhead;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webscraper.FourteenerRouteScraper;
import webscraper.TrailheadScraper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;

public class RoutesTrailheadsDao extends Dao {
    final private static Logger LOG = LoggerFactory.getLogger(RoutesTrailheadsDao.class);

    public RoutesTrailheadsDao(Connection conn) {
        super(conn);
    }

    public ArrayList<ImmutableStoredRouteAndTrailhead> get(SearchQuery query) {
        String searchQuery = buildSearchQuery(query);
        return getStoredRoutesAndTrailheads(searchQuery);
    }

    public ArrayList<ImmutableStoredRouteAndTrailhead> get(CompareQuery query) {
        String compareQuery = buildCompareQuery(query);
        return getStoredRoutesAndTrailheads(compareQuery);
    }

    public void update(ImmutableStoredRouteAndTrailhead routeAndTrailhead) throws Exception {
        if (hasUpdateDateOverWeekAgo(routeAndTrailhead.getRouteUpdateDate())) {
            Optional<ImmutableFetchedRoute> updatedRoute = new FourteenerRouteScraper(new WebClient()).scrapeImmutableFetchedRoute(routeAndTrailhead.getRouteUrl());
            if (updatedRoute.isPresent()) {
                conn.createStatement().execute(updateRouteQuery(updatedRoute.get()));
                LOG.info("Updated route {}", routeAndTrailhead.getRouteUrl());
            }
        }
        if (routeAndTrailhead.getTrailheadUpdateDate().isPresent()) {
            if (hasUpdateDateOverWeekAgo(routeAndTrailhead.getTrailheadUpdateDate().get())) {
                Optional<ImmutableFetchedTrailhead> updatedTrailhead = new TrailheadScraper(new WebClient()).scrapeImmutableFetchedTrailhead(routeAndTrailhead.getTrailheadUrl().get());
                if (updatedTrailhead.isPresent()) {
                    conn.createStatement().execute(updateTrailheadQuery(updatedTrailhead.get()));
                    LOG.info("Updated trailhead {}", routeAndTrailhead.getTrailheadUrl());
                }
            }
        }
    }

    private static String updateTrailheadQuery(ImmutableFetchedTrailhead trailhead)  {
        if (trailhead == null) {
            return "SELECT * FROM " + HikeSuggesterDatabase.TRAILHEADS;
        } else {
            return "UPDATE " + HikeSuggesterDatabase.TRAILHEADS + "\n" +
                    "SET " + HikeSuggesterDatabase.TRAILHEAD_NAME + " = '" + trailhead.getName() + "', \n" +
                    HikeSuggesterDatabase.COORDINATES + " = '" + trailhead.getCoordinates() + "', \n" +
                    HikeSuggesterDatabase.ROAD_DIFFICULTY + " = " + trailhead.getRoadDifficulty() + ", \n" +
                    HikeSuggesterDatabase.ROAD_DESCRIPTION + " = '" + trailhead.getRoadDescription() + "', \n" +
                    HikeSuggesterDatabase.WINTER_ACCESS + " = '" + trailhead.getWinterAccess() + "', \n" +
                    HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE + " = '" + java.time.LocalDate.now() + "' \n" +
                    "WHERE " + HikeSuggesterDatabase.TRAILHEAD_URL + " = '" + trailhead.getUrl() + "';";
        }
    }

    private static String updateRouteQuery(ImmutableFetchedRoute route)  {
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

    static String buildCompareQuery(CompareQuery query) {
        if (query.getRouteUrls() != null && query.getRouteUrls().size() == 2) {
            return "SELECT * " +
                    "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                    " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                    " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME +
                    " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " IN ('" + query.getRouteUrls().get(0) + "', '" + query.getRouteUrls().get(1) + "');";
        }
        else if (query.getMountainName1() == null || query.getMountainName2() == null || query.getRouteName1() == null || query.getRouteName2() == null) {
            throw new IllegalStateException("Incomplete query. Please enter both route names and mountain names, or route urls only.");
        } else {
            return "SELECT * " +
                    "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                    " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                    " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME +
                    " WHERE " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN ('" + query.getMountainName1() + "', '" + query.getMountainName2() + "')" +
                    " AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN ('" + query.getRouteName1() + "', '" + query.getRouteName2() + "');";
        }
    }

    private static String buildSearchQuery(SearchQuery query) {
        return createSelectStatementMySqlSyntax() + createWhereStatementsMySqlSyntax(query);
    }

    private ArrayList<ImmutableStoredRouteAndTrailhead> getStoredRoutesAndTrailheads(String query) {
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = new ArrayList<>();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                routes.add(buildImmutableStoredRouteAndTrailhead(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    private static ImmutableStoredRouteAndTrailhead buildImmutableStoredRouteAndTrailhead(ResultSet rs) throws SQLException {
        Optional<Integer> trailheadId, roadDifficulty;
        trailheadId = rs.getInt(HikeSuggesterDatabase.TRAILHEAD_ID) == 0 ? Optional.empty() : Optional.of(rs.getInt(HikeSuggesterDatabase.TRAILHEAD_ID));
        roadDifficulty = rs.getInt(HikeSuggesterDatabase.ROAD_DIFFICULTY) == 0 ? Optional.empty() : Optional.of(rs.getInt(HikeSuggesterDatabase.ROAD_DIFFICULTY));

        return ImmutableStoredRouteAndTrailhead.builder()
                .routeId(rs.getInt(HikeSuggesterDatabase.FOURTEENER_ROUTE_ID))
                .mountainName(rs.getString(HikeSuggesterDatabase.MOUNTAIN_NAME))
                .routeName(rs.getString(HikeSuggesterDatabase.ROUTE_NAME))
                .isSnowRoute(rs.getInt(HikeSuggesterDatabase.IS_SNOW_ROUTE) == 1)
                .isStandardRoute(rs.getInt(HikeSuggesterDatabase.IS_STANDARD_ROUTE) == 1)
                .grade(rs.getInt(HikeSuggesterDatabase.GRADE))
                .gradeQuality(rs.getString(HikeSuggesterDatabase.GRADE_QUALITY))
                .startElevation(rs.getInt(HikeSuggesterDatabase.START_ELEVATION))
                .summitElevation(rs.getInt(HikeSuggesterDatabase.SUMMIT_ELEVATION))
                .totalGain(rs.getInt(HikeSuggesterDatabase.TOTAL_GAIN))
                .routeLength(rs.getDouble(HikeSuggesterDatabase.ROUTE_LENGTH))
                .exposure(rs.getString(HikeSuggesterDatabase.EXPOSURE))
                .rockfallPotential(rs.getString(HikeSuggesterDatabase.ROCKFALL_POTENTIAL))
                .routeFinding(rs.getString(HikeSuggesterDatabase.ROUTE_FINDING))
                .commitment(rs.getString(HikeSuggesterDatabase.COMMITMENT))
                .hasMultipleRoutes(rs.getInt(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES) == 1)
                .routeUrl(rs.getString(HikeSuggesterDatabase.ROUTE_URL))
                .routeUpdateDate(rs.getString(HikeSuggesterDatabase.ROUTE_UPDATE_DATE))
                .trailhead(rs.getString(HikeSuggesterDatabase.ROUTE_TRAILHEAD))
                .trailheadId(trailheadId)
                .roadDescription(Optional.ofNullable(rs.getString(HikeSuggesterDatabase.ROAD_DESCRIPTION)))
                .roadDifficulty(roadDifficulty)
                .winterAccess(Optional.ofNullable(rs.getString(HikeSuggesterDatabase.WINTER_ACCESS)))
                .trailheadCoordinates(Optional.ofNullable(rs.getString(HikeSuggesterDatabase.COORDINATES)))
                .trailheadUpdateDate(Optional.ofNullable(rs.getString(HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE)))
                .trailheadUrl(Optional.ofNullable(rs.getString(HikeSuggesterDatabase.TRAILHEAD_URL)))
                .build();
    }

    private static String createSelectStatementMySqlSyntax() {
        return "SELECT *" +
                " FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES  +
                " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS  +
                " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME + " ";
    }

    private static String createWhereStatementsMySqlSyntax(SearchQuery query) {
        return "WHERE " + HikeSuggesterDatabase.FOURTEENER_ROUTE_ID + " >= 0 " + createBodyStatementsMySqlSyntax(query);
    }

    private static String createBodyStatementsMySqlSyntax(SearchQuery query) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        stringJoiner.add(createMountainNamesMySqlSyntax(query));
        stringJoiner.add(createRouteNamesMySqlSyntax(query));
        stringJoiner.add(createIsSnowRouteMySqlSyntax(query));
        stringJoiner.add(createIsStandardRouteMySqlSyntax(query));
        stringJoiner.add(createGradeMySqlSyntax(query));
        stringJoiner.add(createGradeQualityMySqlSyntax(query));
        stringJoiner.add(createTrailheadsMySqlSyntax(query));
        stringJoiner.add(createStartElevationMySqlSyntax(query));
        stringJoiner.add(createSummitElevationMySqlSyntax(query));
        stringJoiner.add(createTotalGainMySqlSyntax(query));
        stringJoiner.add(createRouteLengthMySqlSyntax(query));
        stringJoiner.add(createExposureMySqlSyntax(query));
        stringJoiner.add(createRockfallPotentialMySqlSyntax(query));
        stringJoiner.add(createRouteFindingMySqlSyntax(query));
        stringJoiner.add(createCommitmentMySqlSyntax(query));
        stringJoiner.add(createHasMultipleRoutesMySqlSyntax(query));
        stringJoiner.add(createRouteUrlsMySqlSyntax(query));
        stringJoiner.add(createRoadDifficultiesMySqlSyntax(query));
        stringJoiner.add(createTrailheadUrlsMySqlSyntax(query));
        return stringJoiner + ";";
    }

    private static CharSequence createMountainNamesMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getMountainNames() != null) {
            query.getMountainNames().forEach((mountainName) -> stringJoiner.add("'" + mountainName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createRouteNamesMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRouteNames() != null) {
            query.getRouteNames().forEach((routeName) -> stringJoiner.add("'" + routeName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createIsSnowRouteMySqlSyntax(SearchQuery query) {
        if (query.isSnowRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_SNOW_ROUTE + " = 1";
        } else {
            return "";
        }
    }

    private static CharSequence createIsStandardRouteMySqlSyntax(SearchQuery query) {
        if (query.isStandardRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = 1";
        } else {
            return "";
        }
    }

    private static CharSequence createGradeMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getGrades() != null) {
            query.getGrades().forEach((grade) -> stringJoiner.add(String.valueOf(grade)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createGradeQualityMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE_QUALITY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getGradeQualities() != null) {
            query.getGradeQualities().forEach((gradeQuality) -> stringJoiner.add("'" + gradeQuality + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createTrailheadsMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getTrailheads() != null) {
            query.getTrailheads().forEach((trailhead) -> stringJoiner.add("'" + trailhead + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createStartElevationMySqlSyntax(SearchQuery query) {
        if (query.getStartElevation() != 0) {
            String startElevationString = String.valueOf(query.getStartElevation());
            return "AND " + HikeSuggesterDatabase.START_ELEVATION + " >= " + startElevationString;
        } else {
            return "";
        }
    }

    private static CharSequence createSummitElevationMySqlSyntax(SearchQuery query) {
        if (query.getSummitElevation() != 0) {
            String summitElevationString = String.valueOf(query.getSummitElevation());
            return "AND " + HikeSuggesterDatabase.SUMMIT_ELEVATION + " >= " + summitElevationString;
        } else {
            return "";
        }
    }

    private static CharSequence createTotalGainMySqlSyntax(SearchQuery query) {
        if (query.getTotalGain() != 0) {
            String totalGainString = String.valueOf(query.getTotalGain());
            return "AND " + HikeSuggesterDatabase.TOTAL_GAIN + " >= " + totalGainString;
        } else {
            return "";
        }
    }

    private static CharSequence createRouteLengthMySqlSyntax(SearchQuery query) {
        if (query.getRouteLength() != 0) {
            String routeLengthString = String.valueOf(query.getRouteLength());
            return "AND " + HikeSuggesterDatabase.ROUTE_LENGTH + " >= " + routeLengthString;
        } else {
            return "";
        }
    }

    private static CharSequence createExposureMySqlSyntax(SearchQuery query) {
        if (query.getExposure() != null) {
            return "AND " + HikeSuggesterDatabase.EXPOSURE + " = '" + query.getExposure() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createRockfallPotentialMySqlSyntax(SearchQuery query) {
        if (query.getRockfallPotential() != null) {
            return "AND " + HikeSuggesterDatabase.ROCKFALL_POTENTIAL + " = '" + query.getRockfallPotential() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createRouteFindingMySqlSyntax(SearchQuery query) {
        if (query.getRouteFinding() != null) {
            return "AND " + HikeSuggesterDatabase.ROUTE_FINDING + " = '" + query.getRouteFinding() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createCommitmentMySqlSyntax(SearchQuery query) {
        if (query.getCommitment() != null) {
            return "AND " + HikeSuggesterDatabase.COMMITMENT + " = '" + query.getCommitment() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createHasMultipleRoutesMySqlSyntax(SearchQuery query) {
        if (query.isHasMultipleRoutes()) {
            return "AND " + HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = 1";
        } else {
            return "";
        }
    }

    private static CharSequence createRouteUrlsMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_URL + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRouteUrls() != null) {
            query.getRouteUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createRoadDifficultiesMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROAD_DIFFICULTY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRoadDifficulties() != null) {
            query.getRoadDifficulties().forEach((roadDifficulty) -> stringJoiner.add(String.valueOf(roadDifficulty)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createTrailheadUrlsMySqlSyntax(SearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_URL + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getTrailheadUrls() != null) {
            query.getTrailheadUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    static boolean hasUpdateDateOverWeekAgo(String strDate) {
        if (strDate == null) {
            return false;
        }
        LocalDate currentDate = LocalDate.now();
        LocalDate updateDate = convertStringToDate(strDate);
        LocalDate updateDatePlusOneWeek = updateDate.plusWeeks(1);
        return currentDate.isAfter(updateDatePlusOneWeek) || currentDate.equals(updateDatePlusOneWeek);
    }

    private static LocalDate convertStringToDate (String strDate) {
        if (strDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
            return LocalDate.parse(strDate, formatter);
        } else {
            return null;
        }
    }
}
