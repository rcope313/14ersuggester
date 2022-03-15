package database.dao;

import database.models.ImmutableCompareQuery;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRoute;
import models.HikeSuggesterDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;

public class FourteenerRoutesDao {

    public void insert(ImmutableStoredRoute route) {
        if (route != null) {
            try {
                DatabaseConnection.createStatement().execute(insertQuery(route));
                System.out.println("ENTRY CREATED \n");
                System.out.print("MountainName: " + route.getMountainName() + "\n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");

            } catch (Exception e) {
                System.out.print("DUPLICATE ENTRY IGNORED \n");
                System.out.print("MountainName: " + route.getMountainName() + "\n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");
            }
        }
    }

    public ArrayList<ImmutableStoredRoute> get(ImmutableSearchQuery query) {
        String searchQuery = searchQuery(query);
        return getStoredRoutes(searchQuery);
    }

    public ArrayList<ImmutableStoredRoute> get(ImmutableCompareQuery query) {
        String compareQuery = compareQuery(query);
        return getStoredRoutes(compareQuery);
    }

    public void update() {}

    ArrayList<ImmutableStoredRoute> getStoredRoutes(String query) {
        ArrayList<ImmutableStoredRoute> routes = new ArrayList<>();
        try (Statement stmt = DatabaseConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                routes.add(buildImmutableStoredRoute(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    ImmutableStoredRoute buildImmutableStoredRoute(ResultSet rs) throws SQLException {
        return ImmutableStoredRoute.builder()
                .mountainName(rs.getString(HikeSuggesterDatabase.MOUNTAIN_NAME))
                .routeName(rs.getString(HikeSuggesterDatabase.ROUTE_NAME))
                .isSnowRoute(rs.getInt(HikeSuggesterDatabase.IS_SNOW_ROUTE) == 1)
                .isStandardRoute(rs.getInt(HikeSuggesterDatabase.IS_STANDARD_ROUTE) == 1)
                .grade(rs.getInt(HikeSuggesterDatabase.GRADE))
                .gradeQuality(rs.getString(HikeSuggesterDatabase.GRADE_QUALITY))
                .trailhead(rs.getString(HikeSuggesterDatabase.TRAILHEAD_NAME))
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
                .build();
    }

    private String compareQuery(ImmutableCompareQuery query) {
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

    private String searchQuery(ImmutableSearchQuery query) {
        return createSelectStatementMySqlSyntax() + createWhereStatementsMySqlSyntax(query);
    }

    private String createSelectStatementMySqlSyntax() {
        return "SELECT *" +
                " FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES  +
                " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS  +
                " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME + " ";
    }

    private String createWhereStatementsMySqlSyntax(ImmutableSearchQuery query) {
        return "WHERE " + HikeSuggesterDatabase.FOURTEENER_ROUTE_ID + " >= 0 " + createBodyStatementsMySqlSyntax(query);
    }

    private String createBodyStatementsMySqlSyntax(ImmutableSearchQuery query) {
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

    private CharSequence createMountainNamesMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getMountainNames() != null) {
            query.getMountainNames().forEach((mountainName) -> stringJoiner.add("'" + mountainName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRouteNamesMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRouteNames() != null) {
            query.getRouteNames().forEach((routeName) -> stringJoiner.add("'" + routeName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createIsSnowRouteMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getIsSnowRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_SNOW_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createIsStandardRouteMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getIsStandardRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createGradeMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getGrades() != null) {
            query.getGrades().forEach((grade) -> stringJoiner.add(String.valueOf(grade)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createGradeQualityMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE_QUALITY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getGradeQualities() != null) {
            query.getGradeQualities().forEach((gradeQuality) -> stringJoiner.add("'" + gradeQuality + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadsMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getTrailheads() != null) {
            query.getTrailheads().forEach((trailhead) -> stringJoiner.add("'" + trailhead + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createStartElevationMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getStartElevation() != 0) {
            String startElevationString = String.valueOf(query.getStartElevation());
            return "AND " + HikeSuggesterDatabase.START_ELEVATION + " >= " + startElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createSummitElevationMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getSummitElevation() != 0) {
            String summitElevationString = String.valueOf(query.getSummitElevation());
            return "AND " + HikeSuggesterDatabase.SUMMIT_ELEVATION + " >= " + summitElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createTotalGainMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getTotalGain() != 0) {
            String totalGainString = String.valueOf(query.getTotalGain());
            return "AND " + HikeSuggesterDatabase.TOTAL_GAIN + " >= " + totalGainString;
        } else {
            return "";
        }
    }

    private CharSequence createRouteLengthMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getRouteLength() != 0) {
            String routeLengthString = String.valueOf(query.getRouteLength());
            return "AND " + HikeSuggesterDatabase.ROUTE_LENGTH + " >= " + routeLengthString;
        } else {
            return "";
        }
    }

    private CharSequence createExposureMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getExposure() != null) {
            return "AND " + HikeSuggesterDatabase.EXPOSURE + " = '" + query.getExposure() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRockfallPotentialMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getRockfallPotential() != null) {
            return "AND " + HikeSuggesterDatabase.ROCKFALL_POTENTIAL + " = '" + query.getRockfallPotential() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRouteFindingMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getRouteFinding() != null) {
            return "AND " + HikeSuggesterDatabase.ROUTE_FINDING + " = '" + query.getRouteFinding() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createCommitmentMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getCommitment() != null) {
            return "AND " + HikeSuggesterDatabase.COMMITMENT + " = '" + query.getCommitment() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createHasMultipleRoutesMySqlSyntax(ImmutableSearchQuery query) {
        if (query.getHasMultipleRoutes()) {
            return "AND " + HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createRouteUrlsMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_URL + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRouteUrls() != null) {
            query.getRouteUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRoadDifficultiesMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.ROAD_DIFFICULTY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getRoadDifficulties() != null) {
            query.getRoadDifficulties().forEach((roadDifficulty) -> stringJoiner.add(String.valueOf(roadDifficulty)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadUrlsMySqlSyntax(ImmutableSearchQuery query) {
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_URL + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (query.getTrailheadUrls() != null) {
            query.getTrailheadUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private String insertQuery(ImmutableStoredRoute route) {
        return "INSERT INTO " + HikeSuggesterDatabase.FOURTEENER_ROUTES + " VALUES (" +
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
                route.getExposure() + "', " +
                route.getRockfallPotential() + "', '" +
                route.getRouteFinding() + "', '" +
                route.getCommitment() + "', " +
                route.getHasMultipleRoutes() + ", '" +
                route.getUrl() + "', '" +
                route.getTrailhead() + "')";
    }
}
