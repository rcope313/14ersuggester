package mysql.query;

import mysql.MySqlConnection;
import utility.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class MySqlSearchQuery {

    private boolean isVerbose = false;
    private String query = null;
    private ArrayList<String> mountainNames;
    private ArrayList<String> routeNames;
    private boolean isStandardRoute = false;
    private boolean isSnowRoute = false;
    private ArrayList<Integer> grades;
    private ArrayList<String> gradeQualities;
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
    private ArrayList<String>trailheadCoordinates;
    private ArrayList<Integer> roadDifficulties;
    private ArrayList<String> trailheadUrls;



    public static void main (String[] args) throws SQLException {
        MySqlSearchQuery query = new MySqlSearchQuery();
        query.setMountainNames(new ArrayList<>(Arrays.asList("Culebra Peak", "Blanca Peak")));
        query.setRouteFinding("Moderate");

        query.viewMySqlTable();


    }

    public void viewMySqlTable() throws SQLException {
        if (isVerbose()) {
            viewMySqlTableVerbose();
        } else {
            viewMySqlTableNonVerbose();
        }

    }

    private void viewMySqlTableNonVerbose() throws SQLException {

        System.out.format("%-35s%-25s%-15s%-15s%-40s\n", "Route Name", "Mountain Name", "Total Gain", "Route Length", "URL");
        String query = createSearchQuery();

        try (Statement stmt = MySqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String routeName = rs.getString("fourteener_routes.routeName");
                String mountainName = rs.getString("fourteener_routes.mountainName");
                int totalGain = rs.getInt("fourteener_routes.totalGain");
                double routeLength = rs.getDouble("fourteener_routes.routeLength");
                String url = rs.getString("fourteener_routes.url");

                Utils.checkRowDateForUpdate(url);
                System.out.format("%-35s%-25s%-15d%-15f%-40s\n", routeName, mountainName, totalGain, routeLength, url);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewMySqlTableVerbose() throws SQLException {

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


        try (Statement stmt = MySqlConnection.createStatement()) {
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
        if (getQuery() == null) {
            return createSelectStatement() + createWhereStatements();
        } else {
            return createSearchQueryByInputOnly();
        }
    }

    private String createSearchQueryByInputOnly() {
        return createSelectStatement() + getQuery();

    }

    private String createSelectStatement() {
        if (isVerbose()) {
            return
                    "SELECT * " +
                    "FROM fourteener_routes " +
                    "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name ";
        } else {
            return
                    "SELECT " +
                    "fourteener_routes.routeName, " +
                    "fourteener_routes.mountainName, " +
                    "fourteener_routes.totalGain, " +
                    "fourteener_routes.routeLength, " +
                    "fourteener_routes.url " +
                    "FROM fourteener_routes " +
                    "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name ";
        }
    }

    private String createWhereStatements() {
        return "WHERE fourteenerRouteId >= 0 " + createSearchQueryString();

    }

    private String createSearchQueryString() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        stringJoiner.add(createMountainNamesSyntax());
        stringJoiner.add(createRouteNamesSyntax());
        stringJoiner.add(createIsSnowRouteSyntax());
        stringJoiner.add(createIsStandardRouteSyntax());
        stringJoiner.add(createGradeSyntax());
        stringJoiner.add(createGradeQualitySyntax());
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
        stringJoiner.add(createTrailheadCoordinatesSyntax());
        stringJoiner.add(createRoadDifficultiesSyntax());
        stringJoiner.add(createTrailheadUrlsSyntax());

        return stringJoiner.toString() + ";";
    }



    private CharSequence createMountainNamesSyntax() {
        String syntax = "AND fourteener_routes.mountainName IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getMountainNames() != null) {
            getMountainNames().forEach((mountainName) -> stringJoiner.add("'" + mountainName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRouteNamesSyntax() {
        String syntax = "AND fourteener_routes.routeName IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRouteNames() != null) {
            getRouteNames().forEach((routeName) -> stringJoiner.add("'" + routeName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createIsSnowRouteSyntax() {
        if (isSnowRoute()) {
            return "AND fourteener_routes.isSnowRoute = true";
        } else {
            return "";
        }
    }

    private CharSequence createIsStandardRouteSyntax() {
        if (isStandardRoute()) {
            return "AND fourteener_routes.isStandardRoute = true";
        } else {
            return "";
        }
    }

    private CharSequence createGradeSyntax() {
        String syntax = "AND fourteener_routes.grade IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getGrades() != null) {
            getGrades().forEach((grade) -> stringJoiner.add(String.valueOf(grade)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createGradeQualitySyntax() {
        String syntax = "AND fourteener_routes.gradeQuality IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getGradeQualities() != null) {
            getGradeQualities().forEach((gradeQuality) -> stringJoiner.add("'" + gradeQuality + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadsSyntax() {
        String syntax = "AND trailheads.name IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getTrailheads() != null) {
            getTrailheads().forEach((trailhead) -> stringJoiner.add("'" + trailhead + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createStartElevationSyntax() {
        if (getStartElevation() != 0) {
            String startElevationString = String.valueOf(getStartElevation() );
            return "AND fourteener_routes.startElevation >= " + startElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createSummitElevationSyntax() {
        if (getSummitElevation() != 0) {
            String summitElevationString = String.valueOf(getSummitElevation());
            return "AND fourteener_routes.summitElevation >= " + summitElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createTotalGainSyntax() {
        if (getTotalGain() != 0) {
            String totalGainString = String.valueOf(getTotalGain());
            return "AND fourteener_routes.totalGain >= " + totalGainString;
        } else {
            return "";
        }
    }

    private CharSequence createRouteLengthSyntax() {
        if (getRouteLength() != 0) {
            String routeLengthString = String.valueOf(getRouteLength());
            return "AND fourteener_routes.routeLength >= " + routeLengthString;
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
            return "AND fourteener_routes.commitment = '" + getCommitment() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createHasMultipleRoutesSyntax() {
        if (isHasMultipleRoutes()) {
            return "AND fourteener_routes.hasMultipleRoutes = true";
        } else {
            return "";
        }
    }

    private CharSequence createRouteUrlsSyntax() {
        String syntax = "AND fourteener_routes.url IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRouteUrls() != null) {
            getRouteUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadCoordinatesSyntax() {
        String syntax = "AND trailheads.coordinates IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getTrailheadCoordinates() != null) {
            getTrailheadCoordinates().forEach((coordinate) -> stringJoiner.add("'" + coordinate + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRoadDifficultiesSyntax() {
        String syntax = "AND trailheads.roadDifficulty IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRoadDifficulties() != null) {
            getRoadDifficulties().forEach((roadDifficulty) -> stringJoiner.add(String.valueOf(roadDifficulty)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadUrlsSyntax() {
        String syntax = "AND trailheads.url IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getTrailheadUrls() != null) {
            getTrailheadUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }












    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Integer> grades) {
        this.grades = grades;
    }

    public ArrayList<String> getGradeQualities() {
        return gradeQualities;
    }

    public void setGradeQualities(ArrayList<String> gradeQualities) {
        this.gradeQualities = gradeQualities;
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


    public ArrayList<String> getTrailheadUrls() {
        return trailheadUrls;
    }

    public void setTrailheadUrls(ArrayList<String> trailheadUrls) {
        this.trailheadUrls = trailheadUrls;
    }


}
