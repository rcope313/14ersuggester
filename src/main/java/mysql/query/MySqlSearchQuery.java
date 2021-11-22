package mysql.query;

import models.CliColumn;
import models.HikeSuggesterDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class MySqlSearchQuery extends MySqlQuery {

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


    public static void main (String[] args) {
        MySqlSearchQuery searchQuery = new MySqlSearchQuery();
        searchQuery.setMountainNames(new ArrayList<>(Arrays.asList("Handies Peak")));
        searchQuery.setRouteNames(new ArrayList<>(Arrays.asList("East Slopes")));
        searchQuery.setVerbose(true);


        String searchQuerySyntax = searchQuery.createMySqlSyntaxForSearchQuery();
        ArrayList<CliColumn> cliColumnFields;

        if (searchQuery.isVerbose()) {
            cliColumnFields = searchQuery.designateCliColumnFieldsVerbose();
        } else {
            cliColumnFields = searchQuery.designateCliColumnFieldsNonVerbose();

        }
        searchQuery.viewCliTable(cliColumnFields, searchQuerySyntax);


    }



    public String createMySqlSyntaxForSearchQuery() {
        if (getQuery() == null) {
            return createSelectStatementMySqlSyntax() + createWhereStatementsMySqlSyntax();
        } else {
            return createMySqlSyntaxForSearchQueryByCliInputOnly();
        }
    }

    private String createMySqlSyntaxForSearchQueryByCliInputOnly() {
        return createSelectStatementMySqlSyntax() + getQuery();

    }

    private String createSelectStatementMySqlSyntax() {
        return
                "SELECT *" +
                        " FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES  +
                        " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS  +
                        " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME + " ";

    }

    private String createWhereStatementsMySqlSyntax() {
        return "WHERE " + HikeSuggesterDatabase.FOURTEENER_ROUTE_ID + " >= 0 " + createBodyStatementsMySqlSyntax();

    }

    private String createBodyStatementsMySqlSyntax() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        stringJoiner.add(createMountainNamesMySqlSyntax());
        stringJoiner.add(createRouteNamesMySqlSyntax());
        stringJoiner.add(createIsSnowRouteMySqlSyntax());
        stringJoiner.add(createIsStandardRouteMySqlSyntax());
        stringJoiner.add(createGradeMySqlSyntax());
        stringJoiner.add(createGradeQualityMySqlSyntax());
        stringJoiner.add(createTrailheadsMySqlSyntax());
        stringJoiner.add(createStartElevationMySqlSyntax());
        stringJoiner.add(createSummitElevationMySqlSyntax());
        stringJoiner.add(createTotalGainMySqlSyntax());
        stringJoiner.add(createRouteLengthMySqlSyntax());
        stringJoiner.add(createExposureMySqlSyntax());
        stringJoiner.add(createRockfallPotentialMySqlSyntax());
        stringJoiner.add(createRouteFindingMySqlSyntax());
        stringJoiner.add(createCommitmentMySqlSyntax());
        stringJoiner.add(createHasMultipleRoutesMySqlSyntax());
        stringJoiner.add(createRouteUrlsMySqlSyntax());
        stringJoiner.add(createTrailheadCoordinatesMySqlSyntax());
        stringJoiner.add(createRoadDifficultiesMySqlSyntax());
        stringJoiner.add(createTrailheadUrlsMySqlSyntax());

        return stringJoiner.toString() + ";";
    }



    private CharSequence createMountainNamesMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getMountainNames() != null) {
            getMountainNames().forEach((mountainName) -> stringJoiner.add("'" + mountainName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRouteNamesMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRouteNames() != null) {
            getRouteNames().forEach((routeName) -> stringJoiner.add("'" + routeName + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createIsSnowRouteMySqlSyntax() {
        if (isSnowRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_SNOW_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createIsStandardRouteMySqlSyntax() {
        if (isStandardRoute()) {
            return "AND " + HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createGradeMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getGrades() != null) {
            getGrades().forEach((grade) -> stringJoiner.add(String.valueOf(grade)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createGradeQualityMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.GRADE_QUALITY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getGradeQualities() != null) {
            getGradeQualities().forEach((gradeQuality) -> stringJoiner.add("'" + gradeQuality + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadsMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.TRAILHEAD_NAME + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getTrailheads() != null) {
            getTrailheads().forEach((trailhead) -> stringJoiner.add("'" + trailhead + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createStartElevationMySqlSyntax() {
        if (getStartElevation() != 0) {
            String startElevationString = String.valueOf(getStartElevation());
            return "AND " + HikeSuggesterDatabase.START_ELEVATION + " >= " + startElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createSummitElevationMySqlSyntax() {
        if (getSummitElevation() != 0) {
            String summitElevationString = String.valueOf(getSummitElevation());
            return "AND " + HikeSuggesterDatabase.SUMMIT_ELEVATION + " >= " + summitElevationString;
        } else {
            return "";
        }
    }

    private CharSequence createTotalGainMySqlSyntax() {
        if (getTotalGain() != 0) {
            String totalGainString = String.valueOf(getTotalGain());
            return "AND " + HikeSuggesterDatabase.TOTAL_GAIN + " >= " + totalGainString;
        } else {
            return "";
        }
    }

    private CharSequence createRouteLengthMySqlSyntax() {
        if (getRouteLength() != 0) {
            String routeLengthString = String.valueOf(getRouteLength());
            return "AND " + HikeSuggesterDatabase.ROUTE_LENGTH + " >= " + routeLengthString;
        } else {
            return "";
        }
    }

    private CharSequence createExposureMySqlSyntax() {
        if (getExposure() != null) {
            return "AND " + HikeSuggesterDatabase.EXPOSURE + " = '" + getExposure() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRockfallPotentialMySqlSyntax() {
        if (getRockfallPotential() != null) {
            return "AND " + HikeSuggesterDatabase.ROCKFALL_POTENTIAL + " = '" + getRockfallPotential() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createRouteFindingMySqlSyntax() {
        if (getRouteFinding() != null) {
            return "AND " + HikeSuggesterDatabase.ROUTE_FINDING + " = '" + getRouteFinding() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createCommitmentMySqlSyntax() {
        if (getCommitment() != null) {
            return "AND " + HikeSuggesterDatabase.COMMITMENT + " = '" + getCommitment() + "'";
        } else {
            return "";
        }
    }

    private CharSequence createHasMultipleRoutesMySqlSyntax() {
        if (isHasMultipleRoutes()) {
            return "AND " + HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = true";
        } else {
            return "";
        }
    }

    private CharSequence createRouteUrlsMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.ROUTE_URL + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRouteUrls() != null) {
            getRouteUrls().forEach((url) -> stringJoiner.add("'" + url + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadCoordinatesMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.COORDINATES + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getTrailheadCoordinates() != null) {
            getTrailheadCoordinates().forEach((coordinate) -> stringJoiner.add("'" + coordinate + "'"));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createRoadDifficultiesMySqlSyntax() {
        String syntax = "AND " + HikeSuggesterDatabase.ROAD_DIFFICULTY + " IN (";
        StringJoiner stringJoiner = new StringJoiner(", ");
        if (getRoadDifficulties() != null) {
            getRoadDifficulties().forEach((roadDifficulty) -> stringJoiner.add(String.valueOf(roadDifficulty)));
            return syntax + stringJoiner.toString() + ") ";
        } else {
            return "";
        }
    }

    private CharSequence createTrailheadUrlsMySqlSyntax() {
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
