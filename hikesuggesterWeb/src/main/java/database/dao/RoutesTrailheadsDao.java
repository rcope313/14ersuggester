package database.dao;

import database.models.CompareQuery;
import database.models.HikeSuggesterDatabase;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.SearchQuery;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;

public class RoutesTrailheadsDao extends Dao {

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

    private static String buildCompareQuery(CompareQuery query) {
        if (query.getRouteUrls().isEmpty()) {
            return "SELECT * " +
                    "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                    " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                    " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME  +
                    " WHERE " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN ('" + query.getMountainName1() + "', '" + query.getMountainName2() + "')" +
                    " AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN ('" + query.getRouteName1() + "', '" + query.getRouteName2() + "');";

        } else {
            return "SELECT * " +
                    "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                    " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                    " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME +
                    " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " IN ('" + query.getRouteUrls().get(0) + "', '" + query.getRouteUrls().get(1) + "');";
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
                .trailhead(rs.getString(HikeSuggesterDatabase.TRAILHEAD_NAME))
                .trailheadId(Optional.of(rs.getInt(HikeSuggesterDatabase.TRAILHEAD_NAME)))
                .roadDescription(Optional.of(rs.getString(HikeSuggesterDatabase.ROAD_DESCRIPTION)))
                .roadDifficulty(Optional.of(rs.getInt(HikeSuggesterDatabase.ROAD_DIFFICULTY)))
                .trailheadCoordinates(Optional.of(rs.getString(HikeSuggesterDatabase.COORDINATES)))
                .trailheadUpdateDate(Optional.of(rs.getString(HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE)))
                .trailheadUrl(Optional.of(rs.getString(HikeSuggesterDatabase.TRAILHEAD_URL)))
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
            return "AND " + HikeSuggesterDatabase.IS_SNOW_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private static CharSequence createIsStandardRouteMySqlSyntax(SearchQuery query) {
        if (query.isStandardRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = true";
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
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_NAME + " IN (";
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
            return "AND " + HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = true";
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
}
