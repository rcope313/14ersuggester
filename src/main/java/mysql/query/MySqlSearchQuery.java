package mysql.query;

import models.HikeSuggesterDatabase;
import mysql.MySqlConnection;
import mysql.update.UpdateFourteenerRoutes;
import mysql.update.UpdateTrailheads;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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



    public void viewMySqlTableWithWeeklyUpdate()  {
        if (isVerbose()) {
            viewMySqlTableVerboseWithWeeklyUpdate();
        } else {
            viewMySqlTableNonVerboseWithWeeklyUpdate();
        }

    }

    private void viewMySqlTableNonVerboseWithWeeklyUpdate() {

        System.out.format("%-35s%-25s%-15s%-15s%-40s\n", "Route Name", "Mountain Name", "Total Gain", "Route Length", "URL");
        String query = createSearchQuery();

        try (Statement stmt = MySqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String routeName = rs.getString(HikeSuggesterDatabase.ROUTE_NAME);
                String mountainName = rs.getString(HikeSuggesterDatabase.MOUNTAIN_NAME);
                int totalGain = rs.getInt(HikeSuggesterDatabase.TOTAL_GAIN);
                double routeLength = rs.getDouble(HikeSuggesterDatabase.ROUTE_LENGTH);
                String routeUrl = rs.getString(HikeSuggesterDatabase.ROUTE_URL);
                String routeUpdateDate = rs.getString (HikeSuggesterDatabase.ROUTE_UPDATE_DATE);
                String trailheadUrl = rs.getString(HikeSuggesterDatabase.TRAILHEAD_URL);
                String trailheadUpdateDate = rs.getString (HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE);

                System.out.format("%-35s%-25s%-15d%-15f%-40s\n", routeName, mountainName, totalGain, routeLength, routeUrl);
                UpdateFourteenerRoutes.weeklyUpdate(routeUpdateDate, routeUrl);
                UpdateTrailheads.weeklyUpdate(trailheadUpdateDate, trailheadUrl);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewMySqlTableVerboseWithWeeklyUpdate()  {

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
                int id = rs.getInt(HikeSuggesterDatabase.FOURTEENER_ROUTE_ID);
                String routeName = rs.getString(HikeSuggesterDatabase.ROUTE_NAME);
                String mountainName = rs.getString(HikeSuggesterDatabase.MOUNTAIN_NAME);
                int isSnowRoute = rs.getInt(HikeSuggesterDatabase.IS_SNOW_ROUTE);

                int isStandardRoute = rs.getInt(HikeSuggesterDatabase.IS_STANDARD_ROUTE);
                int grade = rs.getInt(HikeSuggesterDatabase.GRADE);
                String gradeQuality = rs.getString(HikeSuggesterDatabase.GRADE_QUALITY);
                int startElevation = rs.getInt(HikeSuggesterDatabase.START_ELEVATION);

                int summitElevation = rs.getInt(HikeSuggesterDatabase.SUMMIT_ELEVATION);
                int totalGain = rs.getInt(HikeSuggesterDatabase.TOTAL_GAIN);
                double routeLength = rs.getDouble(HikeSuggesterDatabase.ROUTE_LENGTH);
                String exposure = rs.getString(HikeSuggesterDatabase.EXPOSURE);

                String rockfallPotential = rs.getString(HikeSuggesterDatabase.ROCKFALL_POTENTIAL);
                String routeFinding = rs.getString(HikeSuggesterDatabase.ROUTE_FINDING);
                String commitment = rs.getString(HikeSuggesterDatabase.COMMITMENT);
                int hasMultipleRoutes = rs.getInt(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES);

                String routeUrl = rs.getString(HikeSuggesterDatabase.ROUTE_URL);
                String trailhead = rs.getString(HikeSuggesterDatabase.ROUTE_TRAILHEAD);

                String routeUpdateDate = rs.getString(HikeSuggesterDatabase.ROUTE_UPDATE_DATE);
                String trailheadUrl = rs.getString(HikeSuggesterDatabase.TRAILHEAD_URL);
                String trailheadUpdateDate = rs.getString(HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE);


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
                        routeUrl, trailhead);

                UpdateFourteenerRoutes.weeklyUpdate(routeUpdateDate, routeUrl);
                UpdateTrailheads.weeklyUpdate(trailheadUpdateDate, trailheadUrl);

            }

        } catch (Exception e) {
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
        return
                "SELECT *" +
                        " FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES  +
                        " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS  +
                        " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME + " ";

    }

    private String createWhereStatements() {
        return "WHERE " + HikeSuggesterDatabase.FOURTEENER_ROUTE_ID + " >= 0 " + createSearchQueryString();

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
        String syntax = "AND " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getMountainNames() != null) {
            getMountainNames().forEach((mountainName) -> stringJoiner.add("'" + mountainName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRouteNamesSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN (";
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
            return "AND " + HikeSuggesterDatabase.IS_SNOW_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createIsStandardRouteSyntax() {
        if (isStandardRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createGradeSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getGrades() != null) {
            getGrades().forEach((grade) -> stringJoiner.add(String.valueOf(grade)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createGradeQualitySyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE_QUALITY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getGradeQualities() != null) {
            getGradeQualities().forEach((gradeQuality) -> stringJoiner.add("'" + gradeQuality + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadsSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_NAME + " IN (";
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
            String startElevationString = String.valueOf(getStartElevation());
            return "AND " + HikeSuggesterDatabase.START_ELEVATION + " >= " + startElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createSummitElevationSyntax() {
        if (getSummitElevation() != 0) {
            String summitElevationString = String.valueOf(getSummitElevation());
            return "AND " + HikeSuggesterDatabase.SUMMIT_ELEVATION + " >= " + summitElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createTotalGainSyntax() {
        if (getTotalGain() != 0) {
            String totalGainString = String.valueOf(getTotalGain());
            return "AND " + HikeSuggesterDatabase.TOTAL_GAIN + " >= " + totalGainString;
        } else {
            return "";
        }
    }

    private CharSequence createRouteLengthSyntax() {
        if (getRouteLength() != 0) {
            String routeLengthString = String.valueOf(getRouteLength());
            return "AND " + HikeSuggesterDatabase.ROUTE_LENGTH + " >= " + routeLengthString;
        } else {
            return "";
        }
    }

    private CharSequence createExposureSyntax() {
        if (getExposure() != null) {
            return "AND " + HikeSuggesterDatabase.EXPOSURE + " = '" + getExposure() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRockfallPotentialSyntax() {
        if (getRockfallPotential() != null) {
            return "AND " + HikeSuggesterDatabase.ROCKFALL_POTENTIAL + " = '" + getRockfallPotential() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRouteFindingSyntax() {
        if (getRouteFinding() != null) {
            return "AND " + HikeSuggesterDatabase.ROUTE_FINDING + " = '" + getRouteFinding() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createCommitmentSyntax() {
        if (getCommitment() != null) {
            return "AND " + HikeSuggesterDatabase.COMMITMENT + " = '" + getCommitment() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createHasMultipleRoutesSyntax() {
        if (isHasMultipleRoutes()) {
            return "AND " + HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createRouteUrlsSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_URL + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRouteUrls() != null) {
            getRouteUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadCoordinatesSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.COORDINATES + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getTrailheadCoordinates() != null) {
            getTrailheadCoordinates().forEach((coordinate) -> stringJoiner.add("'" + coordinate + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRoadDifficultiesSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.ROAD_DIFFICULTY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRoadDifficulties() != null) {
            getRoadDifficulties().forEach((roadDifficulty) -> stringJoiner.add(String.valueOf(roadDifficulty)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadUrlsSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_URL + " IN (";
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
