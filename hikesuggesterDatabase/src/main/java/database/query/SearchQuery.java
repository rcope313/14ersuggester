package database.query;

import models.CliColumn;
import models.CliColumnDesign;
import models.HikeSuggesterDatabase;

import java.util.ArrayList;
import java.util.StringJoiner;

public class SearchQuery implements Query {

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


    @Override
    public String createQuerySyntax() {
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



    @Override
    public boolean isVerbose() {
        return isVerbose;
    }

    @Override
    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public void setQuery(String query) {
        this.query = query;

    }


    @Override
    public ArrayList<String> getMountainNames() {
        return mountainNames;
    }

    @Override
    public void setMountainNames(ArrayList<String> mountainNames) {
        this.mountainNames = mountainNames;
    }

    @Override
    public ArrayList<String> getRouteNames() {
        return routeNames;
    }

    @Override
    public void setRouteNames(ArrayList<String> routeNames) {
        this.routeNames = routeNames;
    }

    @Override
    public boolean isStandardRoute() {
        return isStandardRoute;
    }

    @Override
    public void setStandardRoute(boolean standardRoute) {
        isStandardRoute = standardRoute;
    }

    @Override
    public boolean isSnowRoute() {
        return isSnowRoute;
    }

    @Override
    public void setSnowRoute(boolean snowRoute) {
        isSnowRoute = snowRoute;
    }

    @Override
    public ArrayList<Integer> getGrades() {
        return grades;
    }

    @Override
    public void setGrades(ArrayList<Integer> grades) {
        this.grades = grades;
    }

    @Override
    public ArrayList<String> getGradeQualities() {
        return gradeQualities;
    }

    @Override
    public void setGradeQualities(ArrayList<String> gradeQualities) {
        this.gradeQualities = gradeQualities;
    }

    @Override
    public ArrayList<String> getTrailheads() {
        return trailheads;
    }

    @Override
    public void setTrailheads(ArrayList<String> trailheads) {
        this.trailheads = trailheads;
    }

    @Override
    public int getStartElevation() {
        return startElevation;
    }

    @Override
    public void setStartElevation(int startElevation) {
        this.startElevation = startElevation;
    }

    @Override
    public int getSummitElevation() {
        return summitElevation;
    }

    @Override
    public void setSummitElevation(int summitElevation) {
        this.summitElevation = summitElevation;
    }

    @Override
    public int getTotalGain() {
        return totalGain;
    }

    @Override
    public void setTotalGain(int totalGain) {
        this.totalGain = totalGain;
    }

    @Override
    public double getRouteLength() {
        return routeLength;
    }

    @Override
    public void setRouteLength(double routeLength) {
        this.routeLength = routeLength;
    }

    @Override
    public String getExposure() {
        return exposure;
    }

    @Override
    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    @Override
    public String getRockfallPotential() {
        return rockfallPotential;
    }

    @Override
    public void setRockfallPotential(String rockfallPotential) {
        this.rockfallPotential = rockfallPotential;
    }

    @Override
    public String getRouteFinding() {
        return routeFinding;
    }

    @Override
    public void setRouteFinding(String routeFinding) {
        this.routeFinding = routeFinding;
    }

    @Override
    public String getCommitment() {
        return commitment;
    }

    @Override
    public void setCommitment(String commitment) {
        this.commitment = commitment;
    }

    @Override
    public boolean isHasMultipleRoutes() {
        return hasMultipleRoutes;
    }

    @Override
    public void setHasMultipleRoutes(boolean hasMultipleRoutes) {
        this.hasMultipleRoutes = hasMultipleRoutes;
    }

    @Override
    public ArrayList<String> getRouteUrls() {
        return routeUrls;
    }

    @Override
    public void setRouteUrls(ArrayList<String> routeUrls) {
        this.routeUrls = routeUrls;
    }

    @Override
    public ArrayList<String> getTrailheadCoordinates() {
        return trailheadCoordinates;
    }

    @Override
    public void setTrailheadCoordinates(ArrayList<String> trailheadCoordinates) {
        this.trailheadCoordinates = trailheadCoordinates;
    }

    @Override
    public ArrayList<Integer> getRoadDifficulties() {
        return roadDifficulties;
    }

    @Override
    public void setRoadDifficulties(ArrayList<Integer> roadDifficulties) {
        this.roadDifficulties = roadDifficulties;
    }

    @Override
    public ArrayList<String> getTrailheadUrls() {
        return trailheadUrls;
    }

    @Override
    public void setTrailheadUrls(ArrayList<String> trailheadUrls) {
        this.trailheadUrls = trailheadUrls;
    }



    @Override
    public String getMountainName1() {
        throw new IllegalArgumentException("Search query does not use mountain name 1 field");
    }

    @Override
    public void setMountainName1(String mountainName1) {
        throw new IllegalArgumentException("Search query does not use mountain name 1 field");

    }

    @Override
    public String getRouteName1() {
        throw new IllegalArgumentException("Search query does not use route name 1 field");
    }

    @Override
    public void setRouteName1(String routeName1) {
        throw new IllegalArgumentException("Search query does not use route name 1 field");


    }

    @Override
    public String getMountainName2() {
        throw new IllegalArgumentException("Search query does not use mountain name 2 field");

    }

    @Override
    public void setMountainName2(String mountainName2) {
        throw new IllegalArgumentException("Search query does not use mountain name 2 field");

    }

    @Override
    public String getRouteName2() {
        throw new IllegalArgumentException("Search query does not use mountain name 2 field");
    }

    @Override
    public void setRouteName2(String routeName2) {
        throw new IllegalArgumentException("Search query does not use mountain name 2 field");

    }


}
