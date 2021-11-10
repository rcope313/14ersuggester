package mysql;

import picocli.CommandLine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;

public class MySqlSearchQuery {

    //need to deal with grades
    private boolean isVerbose = false;
    private ArrayList<String> mountainNames;
    private ArrayList<String> routeNames;
    private boolean isStandardRoute = false;
    private boolean isSnowRoute = false;
    private ArrayList<String> trailheads;
    private int startElevation;
    private int summitElevation;
    private int totalGain;
    private double routeLength;
    private String exposure;
    private String rockfallPotential;
    private String routeFinding;
    private String commitment;
    private boolean hasMultipleRoutes = false;
    private ArrayList<String> routeUrls;
    private ArrayList<String> trailheadUrls;
    private ArrayList<String>trailheadCoordinates;
    private ArrayList<Integer> roadDifficulties;


    public static void main (String[] args) throws SQLException {
        MySqlSearchQuery query = new MySqlSearchQuery();
        query.setStandardRoute(true);
        query.setTotalGain(4000);
        query.setVerbose(true);

        query.viewMySqlTableVerbose();
    }

    public void viewMySqlTable() throws SQLException {

        System.out.format("%-35s%-25s%-15s%-15s%-40s\n", "Route Name", "Mountain Name", "Total Gain", "Route Length", "URL");
        String query = createSearchQuery();


        try (Statement stmt = MySqlConnection.createStatement();) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String routeName = rs.getString("fourteener_routes.routeName");
                String mountainName = rs.getString("fourteener_routes.mountainName");
                int totalGain = rs.getInt("fourteener_routes.totalGain");
                double routeLength = rs.getDouble("fourteener_routes.routeLength");
                String url = rs.getString("fourteener_routes.url");

                System.out.format("%-35s%-25s%-15d%-15f%-40s\n", routeName, mountainName, totalGain, routeLength, url);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewMySqlTableVerbose() throws SQLException {

        System.out.format
                ("%-10s%-35s%-25s%-15s" +
                 "%-17s%-10s%-17s%-20s" +
                 "%-20s%-17s%-17s%-20s" +
                 "%-20s%-20s%-20s%-20s" +
                 "%-50s%-20s\n",

                 "ID",
                 "Route Name",
                 "Mountain Name",
                 "Snow Route",

                 "Standard Route",
                 "Grade",
                 "Grade Quality",
                 "Start Elevation",

                 "Summit Elevation",
                 "Total Gain",
                 "Route Length",
                 "Exposure",

                 "Rockfall Potential",
                 "Route Finding",
                 "Commitment",
                 "Multiple Routes?",

                 "URL",
                 "Trailhead");
        String query = createSearchQuery();


        try (Statement stmt = MySqlConnection.createStatement();) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("fourteenerRouteId");
                String routeName = rs.getString("fourteener_routes.routeName");
                String mountainName = rs.getString("fourteener_routes.mountainName");
                int isSnowRoute = rs.getInt("fourteener_routes.isSnowRoute");

                int isStandardRoute = rs.getInt("fourteener_routes.isSnowRoute");
                int grade = rs.getInt("fourteener_routes.grade");
                String gradeQuality = rs.getString("fourteener_routes.gradeQuality");
                int startElevation = rs.getInt("fourteener_routes.startElevation");

                int summitElevation = rs.getInt("fourteener_routes.summitElevation");
                int totalGain = rs.getInt("fourteener_routes.totalGain");
                double routeLength = rs.getDouble("fourteener_routes.routeLength");
                String exposure = rs.getString("fourteener_routes.exposure");

                String rockfallPotential = rs.getString("fourteener_routes.exposure");
                String routeFinding = rs.getString("fourteener_routes.routeFinding");
                String commitment = rs.getString("fourteener_routes.commitment");
                int hasMultipleRoutes = rs.getInt("fourteener_routes.hasMultipleRoutes");

                String url = rs.getString("fourteener_routes.url");
                String trailhead = rs.getString("fourteener_routes.trailhead");

                System.out.format(
                        "%-10d%-35s%-25s%-15d" +
                        "%-17d%-10d%-17s%-20d" +
                        "%-20d%-17d%-17f%-20s" +
                        "%-20s%-20s%-20s%-20d" +
                        "%-50s%-20s\n",
                        id, routeName, mountainName, isSnowRoute,
                        isStandardRoute, grade, gradeQuality, startElevation,
                        summitElevation, totalGain, routeLength, exposure,
                        rockfallPotential, routeFinding, commitment, hasMultipleRoutes,
                        url, trailhead);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String createSearchQuery() {
        return createSelectStatement() + createWhereStatements();
    }



    private String createSelectStatement() {
        if (isVerbose()) {
            return "SELECT * FROM hike_suggester.fourteener_routes ";
        } else {
            return "SELECT " +
                    "fourteener_routes.routeName, " +
                    "fourteener_routes.mountainName, " +
                    "fourteener_routes.totalGain, " +
                    "fourteener_routes.routeLength, " +
                    "fourteener_routes.url " +
                    "FROM hike_suggester.fourteener_routes ";
        }
    }

    private String createWhereStatements() {
        return "WHERE fourteenerRouteId > 1 " + createSearchQueryString();

    }

    private String createSearchQueryString() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        stringJoiner.add(createMountainNamesSyntax());
        stringJoiner.add(createRouteNamesSyntax());
        stringJoiner.add(createIsSnowRouteSyntax());
        stringJoiner.add(createIsStandardRouteSyntax());
        stringJoiner.add(createTrailheadsSyntax());
        stringJoiner.add(createStartElevationSyntax());
        stringJoiner.add(createSummitElevationSyntax());
        stringJoiner.add(createTotalGainSyntax());
        stringJoiner.add(createRouteLengthSyntax());
        stringJoiner.add(createExposureSyntax());
        stringJoiner.add(createRockfallPotentialSyntax());
        stringJoiner.add(createRouteFindingSyntax());
        stringJoiner.add(createCommitmentSyntax());
        stringJoiner.add(createHasMultipleRoutesSyntax());
        stringJoiner.add(createRouteUrlsSyntax());
        stringJoiner.add(createTrailheadUrlsSyntax());
        stringJoiner.add(createTrailheadCoordinatesSyntax());
        stringJoiner.add(createRoadDifficultiesSyntax());

        return stringJoiner.toString() + ";";
    }



    private CharSequence createMountainNamesSyntax() {

        StringJoiner stringJoiner = new StringJoiner(" ");
        if (getMountainNames() != null) {
            getMountainNames().forEach((mountainName) -> stringJoiner
                    .add("AND fourteener_routes.mountainName = '"
                            + mountainName + "'"));
        }

        return stringJoiner.toString();
    }

    private CharSequence createRouteNamesSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (getRouteNames() != null) {
            getRouteNames().forEach((routeName) -> stringJoiner
                            .add("AND fourteener_routes.routeName = '"
                                    + routeName + "' "));
        }
        return stringJoiner.toString();
    }

    private CharSequence createIsSnowRouteSyntax() {
        if (isSnowRoute() == true) {
            return "AND fourteener_routes.isSnowRoute = true";
        } else {
            return "";
        }
    }

    private CharSequence createIsStandardRouteSyntax() {
        if (isStandardRoute() == true) {
            return "AND fourteener_routes.isStandardRoute = true";
        } else {
            return "";
        }
    }

    // this won't work without a join
    private CharSequence createTrailheadsSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (trailheads != null) {
            getTrailheads().forEach((trailhead) -> stringJoiner
                            .add("AND trailheads.name = '"
                                    + trailhead + "' "));
        }

        return stringJoiner.toString();
    }

    private CharSequence createStartElevationSyntax() {
        if (getStartElevation() != 0) {
            String startElevationString = String.valueOf(getStartElevation() );
            return "AND fourteener_routes.startElevation = " + startElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createSummitElevationSyntax() {
        if (getSummitElevation() != 0) {
            String summitElevationString = String.valueOf(getSummitElevation());
            return "AND fourteener_routes.summitElevation = " + summitElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createTotalGainSyntax() {
        if (getTotalGain() != 0) {
            String totalGainString = String.valueOf(getTotalGain());
            return "AND fourteener_routes.totalGain > " + totalGainString;
        } else {
            return "";
        }
    }

    private CharSequence createRouteLengthSyntax() {
        if (getRouteLength() != 0) {
            String routeLengthString = String.valueOf(getRouteLength());
            return "AND fourteener_routes.routeLength > " + routeLengthString;
        } else {
            return "";
        }
    }

    private CharSequence createExposureSyntax() {
        if (getExposure() != null) {
            return "AND fourteener_routes.exposure = '" + getExposure() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRockfallPotentialSyntax() {
        if (getRockfallPotential() != null) {
            return "AND fourteener_routes.rockfallPotential = '" + getRockfallPotential() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRouteFindingSyntax() {
        if (getRouteFinding() != null) {
            return "AND fourteener_routes.routeFinding = '" + getRouteFinding() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createCommitmentSyntax() {
        if (getCommitment() != null) {
            return "AND fourteener_routes.commitment = '" + commitment + "'";
        } else {
            return "";
        }
    }

    private CharSequence createHasMultipleRoutesSyntax() {
        if (isHasMultipleRoutes() == true) {
            return "AND fourteener_routes.hasMultipleRoutes = true";
        } else {
            return "";
        }
    }

    private CharSequence createRouteUrlsSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (getRouteUrls() != null) {

            getRouteUrls().forEach((routeUrl) -> stringJoiner
                    .add("AND fourteener_routes.url = '"
                            + routeUrl + "' "));
        }
        return stringJoiner.toString();
    }

    private CharSequence createTrailheadUrlsSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (getTrailheadUrls() != null) {

            getTrailheadUrls().forEach((trailheadUrl) -> stringJoiner
                    .add("AND trailheads.url = '"
                            + trailheadUrl + "' "));
        }
        return stringJoiner.toString();
    }

    private CharSequence createTrailheadCoordinatesSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (getTrailheadCoordinates() != null) {

          getTrailheadCoordinates().forEach((trailheadCoordinate) -> stringJoiner
                  .add("AND trailheads.coordinates = '"
                          + trailheadCoordinate + "' "));
        }

        return stringJoiner.toString();
    }

    private CharSequence createRoadDifficultiesSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (getRoadDifficulties() != null) {

            getRoadDifficulties().forEach((roadDifficulty) -> stringJoiner
                            .add("AND trailheads.coordinates = '"
                                    + String.valueOf(roadDifficulty) + "' "));
        }
        return stringJoiner.toString();
    }












    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public ArrayList<String> getMountainNames() {
        return mountainNames;
    }

    public void setMountainNames(ArrayList<String> mountainNames) {
        this.mountainNames = mountainNames;
    }

    public ArrayList<String> getRouteNames() {
        return routeNames;
    }

    public void setRouteNames(ArrayList<String> routeNames) {
        this.routeNames = routeNames;
    }

    public boolean isStandardRoute() {
        return isStandardRoute;
    }

    public void setStandardRoute(boolean standardRoute) {
        isStandardRoute = standardRoute;
    }

    public boolean isSnowRoute() {
        return isSnowRoute;
    }

    public void setSnowRoute(boolean snowRoute) {
        isSnowRoute = snowRoute;
    }

    public ArrayList<String> getTrailheads() {
        return trailheads;
    }

    public void setTrailheads(ArrayList<String> trailheads) {
        this.trailheads = trailheads;
    }

    public int getStartElevation() {
        return startElevation;
    }

    public void setStartElevation(int startElevation) {
        this.startElevation = startElevation;
    }

    public int getSummitElevation() {
        return summitElevation;
    }

    public void setSummitElevation(int summitElevation) {
        this.summitElevation = summitElevation;
    }

    public int getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(int totalGain) {
        this.totalGain = totalGain;
    }

    public double getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(double routeLength) {
        this.routeLength = routeLength;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public String getRockfallPotential() {
        return rockfallPotential;
    }

    public void setRockfallPotential(String rockfallPotential) {
        this.rockfallPotential = rockfallPotential;
    }

    public String getRouteFinding() {
        return routeFinding;
    }

    public void setRouteFinding(String routeFinding) {
        this.routeFinding = routeFinding;
    }

    public String getCommitment() {
        return commitment;
    }

    public void setCommitment(String commitment) {
        this.commitment = commitment;
    }

    public boolean isHasMultipleRoutes() {
        return hasMultipleRoutes;
    }

    public void setHasMultipleRoutes(boolean hasMultipleRoutes) {
        this.hasMultipleRoutes = hasMultipleRoutes;
    }

    public ArrayList<String> getRouteUrls() {
        return routeUrls;
    }

    public void setRouteUrls(ArrayList<String> routeUrls) {
        this.routeUrls = routeUrls;
    }

    public ArrayList<String> getTrailheadUrls() {
        return trailheadUrls;
    }

    public void setTrailheadUrls(ArrayList<String> trailheadUrls) {
        this.trailheadUrls = trailheadUrls;
    }

    public ArrayList<String> getTrailheadCoordinates() {
        return trailheadCoordinates;
    }

    public void setTrailheadCoordinates(ArrayList<String> trailheadCoordinates) {
        this.trailheadCoordinates = trailheadCoordinates;
    }

    public ArrayList<Integer> getRoadDifficulties() {
        return roadDifficulties;
    }

    public void setRoadDifficulties(ArrayList<Integer> roadDifficulties) {
        this.roadDifficulties = roadDifficulties;
    }
}
