package database.dao;

import database.models.HikeSuggesterDatabase;
import database.models.ImmutableCompareQuery;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRouteAndTrailhead;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringJoiner;

public class RoutesTrailheadsDao extends DatabaseConnection {

    public static ArrayList<ImmutableStoredRouteAndTrailhead> get(ImmutableSearchQuery query) {
        String searchQuery = searchQuery(query);
        return getStoredRoutesAndTrailheads(searchQuery);
    }

    public static ArrayList<ImmutableStoredRouteAndTrailhead> get(ImmutableCompareQuery query) {
        String compareQuery = compareQuery(query);
        return getStoredRoutesAndTrailheads(compareQuery);
    }

    private static String compareQuery(ImmutableCompareQuery query) {
        if (!query.getRouteUrls().isPresent()) {
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
                    " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " IN ('" + query.getRouteUrls().get().get(0) + "', '" + query.getRouteUrls().get().get(1) + "');";
        }
    }

    private static String searchQuery(ImmutableSearchQuery query) {
        return createSelectStatementMySqlSyntax() + createWhereStatementsMySqlSyntax(query);
    }

    private static ArrayList<ImmutableStoredRouteAndTrailhead> getStoredRoutesAndTrailheads(String query) {
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = new ArrayList<>();
        try (Statement stmt = DatabaseConnection.createStatement()) {
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

    private static String createWhereStatementsMySqlSyntax(ImmutableSearchQuery query) {
        return "WHERE " + HikeSuggesterDatabase.FOURTEENER_ROUTE_ID + " >= 0 " + createBodyStatementsMySqlSyntax(query);
    }

    private static String createBodyStatementsMySqlSyntax(ImmutableSearchQuery query) {
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

    private static CharSequence createMountainNamesMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getMountainNames() != null) {
            query.getMountainNames().forEach((mountainName) -> stringJoiner.add("'" + mountainName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createRouteNamesMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRouteNames() != null) {
            query.getRouteNames().forEach((routeName) -> stringJoiner.add("'" + routeName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createIsSnowRouteMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getIsSnowRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_SNOW_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private static CharSequence createIsStandardRouteMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getIsStandardRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private static CharSequence createGradeMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getGrades() != null) {
            query.getGrades().forEach((grade) -> stringJoiner.add(String.valueOf(grade)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createGradeQualityMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE_QUALITY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getGradeQualities() != null) {
            query.getGradeQualities().forEach((gradeQuality) -> stringJoiner.add("'" + gradeQuality + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createTrailheadsMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getTrailheads() != null) {
            query.getTrailheads().forEach((trailhead) -> stringJoiner.add("'" + trailhead + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createStartElevationMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getStartElevation() != 0) {
            String startElevationString = String.valueOf(query.getStartElevation());
            return "AND " + HikeSuggesterDatabase.START_ELEVATION + " >= " + startElevationString;
        } else {
            return "";
        }
    }

    private static CharSequence createSummitElevationMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getSummitElevation() != 0) {
            String summitElevationString = String.valueOf(query.getSummitElevation());
            return "AND " + HikeSuggesterDatabase.SUMMIT_ELEVATION + " >= " + summitElevationString;
        } else {
            return "";
        }
    }

    private static CharSequence createTotalGainMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getTotalGain() != 0) {
            String totalGainString = String.valueOf(query.getTotalGain());
            return "AND " + HikeSuggesterDatabase.TOTAL_GAIN + " >= " + totalGainString;
        } else {
            return "";
        }
    }

    private static CharSequence createRouteLengthMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getRouteLength() != 0) {
            String routeLengthString = String.valueOf(query.getRouteLength());
            return "AND " + HikeSuggesterDatabase.ROUTE_LENGTH + " >= " + routeLengthString;
        } else {
            return "";
        }
    }

    private static CharSequence createExposureMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getExposure() != null) {
            return "AND " + HikeSuggesterDatabase.EXPOSURE + " = '" + query.getExposure() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createRockfallPotentialMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getRockfallPotential() != null) {
            return "AND " + HikeSuggesterDatabase.ROCKFALL_POTENTIAL + " = '" + query.getRockfallPotential() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createRouteFindingMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getRouteFinding() != null) {
            return "AND " + HikeSuggesterDatabase.ROUTE_FINDING + " = '" + query.getRouteFinding() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createCommitmentMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getCommitment() != null) {
            return "AND " + HikeSuggesterDatabase.COMMITMENT + " = '" + query.getCommitment() + "'";
        } else {
            return "";
        }
    }

    private static CharSequence createHasMultipleRoutesMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getHasMultipleRoutes()) {
            return "AND " + HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = true";
        } else {
            return "";
        }
    }

    private static CharSequence createRouteUrlsMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_URL + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRouteUrls() != null) {
            query.getRouteUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createRoadDifficultiesMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROAD_DIFFICULTY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRoadDifficulties() != null) {
            query.getRoadDifficulties().forEach((roadDifficulty) -> stringJoiner.add(String.valueOf(roadDifficulty)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private static CharSequence createTrailheadUrlsMySqlSyntax(ImmutableSearchQuery query) {
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
