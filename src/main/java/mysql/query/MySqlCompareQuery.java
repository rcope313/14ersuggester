package mysql.query;


import models.*;
import mysql.MySqlConnection;
import utility.Utils;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class MySqlCompareQuery extends MySqlQuery {
    private String mountainName1;
    private String routeName1;
    private String mountainName2;
    private String routeName2;
    private ArrayList<String> routeUrls;
    private boolean isVerbose;

    public MySqlCompareQuery() {
        checkFieldsOfMySqlCompareQuery();
    }

    public static void main (String[] args) {
        MySqlCompareQuery compareQuery = new MySqlCompareQuery();
        compareQuery.setMountainName1("Humboldt Peak");
        compareQuery.setRouteName1("West Ridge");
        compareQuery.setMountainName2("Handies Peak");
        compareQuery.setRouteName2("East Slopes");

        String compareQuerySyntax = compareQuery.createMySqlSyntaxForCompareQuery();
        ArrayList<FourteenerRoute> routeList = compareQuery.createFourteenerRoutesFromCliInput(compareQuerySyntax);
        ArrayList<CliColumn> cliColumnFields = compareQuery.buildCliColumnFieldsByCliInput(routeList.get(0), routeList.get(1));

        compareQuery.viewCliTable(cliColumnFields, compareQuerySyntax);

    }



    public String createMySqlSyntaxForCompareQuery() {
        if (getRouteUrls() == null) {
            return
                    "SELECT * " +
                            "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                            " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                            " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME  +
                            " WHERE " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN ('" + getMountainName1() + "', '" + getMountainName2() + "')" +
                            " AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN ('" + getRouteName1() + "', '" + getRouteName2() + "');";

        } else {
            return
                    "SELECT * " +
                            "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                            " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                            " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME +
                            " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " IN ('" + getRouteUrls().get(0) + "', '" + getRouteUrls().get(1) + "');";


        }
    }

    public ArrayList<FourteenerRoute> createFourteenerRoutesFromCliInput(String query) {

        ArrayList<FourteenerRoute> fourteenerRoutes = new ArrayList<>();

        try (Statement stmt = MySqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                FourteenerRoute currentRoute = new FourteenerRoute();
                GradeQuality gradeQuality = new GradeQuality();

                currentRoute.setFourteenerRouteId(rs.getInt(HikeSuggesterDatabase.FOURTEENER_ROUTE_ID));
                currentRoute.setRouteName(rs.getString(HikeSuggesterDatabase.ROUTE_NAME));
                currentRoute.setMountainName(rs.getString(HikeSuggesterDatabase.MOUNTAIN_NAME));
                currentRoute.setSnowRoute
                        (Utils.convertIntToBoolean(rs.getInt(HikeSuggesterDatabase.IS_SNOW_ROUTE)));

                currentRoute.setStandardRoute
                        (Utils.convertIntToBoolean(rs.getInt(HikeSuggesterDatabase.IS_STANDARD_ROUTE)));
                gradeQuality.setGrade(rs.getInt(HikeSuggesterDatabase.GRADE));
                gradeQuality.setQuality(rs.getString(HikeSuggesterDatabase.GRADE_QUALITY));
                currentRoute.setGradeQuality(gradeQuality);
                currentRoute.setStartElevation(rs.getInt(HikeSuggesterDatabase.START_ELEVATION));

                currentRoute.setSummitElevation(rs.getInt(HikeSuggesterDatabase.SUMMIT_ELEVATION));
                currentRoute.setTotalGain(rs.getInt(HikeSuggesterDatabase.TOTAL_GAIN));
                currentRoute.setRouteLength(rs.getDouble(HikeSuggesterDatabase.ROUTE_LENGTH));
                currentRoute.setExposure(rs.getString(HikeSuggesterDatabase.EXPOSURE));

                currentRoute.setRockfallPotential(rs.getString(HikeSuggesterDatabase.ROCKFALL_POTENTIAL));
                currentRoute.setRouteFinding(rs.getString(HikeSuggesterDatabase.ROUTE_FINDING));
                currentRoute.setCommitment(rs.getString(HikeSuggesterDatabase.COMMITMENT));
                currentRoute.setHasMultipleRoutes
                        (Utils.convertIntToBoolean(rs.getInt(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES)));

                currentRoute.setUrl(rs.getString(HikeSuggesterDatabase.ROUTE_URL));
                currentRoute.setTrailhead(rs.getString(HikeSuggesterDatabase.ROUTE_TRAILHEAD));

                fourteenerRoutes.add(currentRoute);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fourteenerRoutes.size() > 2) {
            throw new IllegalStateException("Results yielded more than two Fourteener Routes (the two mountains entered share the " +
                    "same route names). Please enter 14er url of intended routes/mountains.");
        } if (fourteenerRoutes.size() < 2) {
            throw new IllegalStateException("Results yielded less than two Fourteener Routes. Please check the spelling of the Mountain Name and " +
                    "the Route Name.");

        }

        return fourteenerRoutes;
    }

    public ArrayList<CliColumn> buildCliColumnFieldsByCliInput(FourteenerRoute route0, FourteenerRoute route1) {
        ArrayList<CliColumn> cliColumnFields = new ArrayList<>();
        cliColumnFields.add(CliColumnDesign.MOUNTAIN_NAME);
        cliColumnFields.add(CliColumnDesign.ROUTE_NAME);

        if (isSnowRouteDiff(route0, route1)) { cliColumnFields.add(CliColumnDesign.SNOW_ROUTE); }
        if (isStandardRouteDiff(route0, route1)) { cliColumnFields.add(CliColumnDesign.STANDARD_ROUTE); }
        if (gradeDiff(route0, route1)) { cliColumnFields.add(CliColumnDesign.GRADE); }
        if (gradeQualityDiff(route0, route1)) { cliColumnFields.add(CliColumnDesign.GRADE_QUALITY); }
        if (startElevationDiff(route0, route1)) { cliColumnFields.add(CliColumnDesign.START_ELEVATION); }
        if (summitElevationDiff(route0, route1)) { cliColumnFields.add(CliColumnDesign.SUMMIT_ELEVATION); }
        if (totalGainDiff(route0, route1)) { cliColumnFields.add(CliColumnDesign.TOTAL_GAIN); }
        if (routeLengthDiff(route0, route1)) {
            cliColumnFields.add(CliColumnDesign.ROUTE_LENGTH); }
        if (exposureDiff(route0, route1)) {
            cliColumnFields.add(CliColumnDesign.EXPOSURE); }
        if (rockfallPotentialDiff(route0, route1)) {
            cliColumnFields.add(CliColumnDesign.ROCKFALL_POTENTIAL); }
        if (routeFindingDiff(route0, route1)) {
            cliColumnFields.add(CliColumnDesign.ROUTE_FINDING); }
        if (commitmentDiff(route0, route1)) {
            cliColumnFields.add(CliColumnDesign.COMMITMENT); }
        if (hasMultipleRoutesDiff(route0, route1)) {
            cliColumnFields.add(CliColumnDesign.MULTIPLE_ROUTES); }
        if (trailheadDiff(route0, route1)) {
            cliColumnFields.add(CliColumnDesign.TRAILHEAD); }

        cliColumnFields.add(CliColumnDesign.ROUTE_URL);
        return cliColumnFields;

    }




    private boolean isSnowRouteDiff (FourteenerRoute route1, FourteenerRoute route2) {
        return route1.isSnowRoute() != route2.isSnowRoute();

    }

    private boolean isStandardRouteDiff (FourteenerRoute route1, FourteenerRoute route2) {
        return route1.isStandardRoute() != route2.isStandardRoute();
    }

    private boolean gradeDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return route1.getGradeQuality().getGrade() != route2.getGradeQuality().getGrade();
    }

    private boolean gradeQualityDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return !route1.getGradeQuality().getQuality().equals(route2.getGradeQuality().getQuality());
    }

    private boolean startElevationDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return route1.getStartElevation() != route2.getStartElevation();
    }

    private boolean summitElevationDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return route1.getSummitElevation() != route2.getSummitElevation();
    }

    private boolean totalGainDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return route1.getTotalGain() != route2.getTotalGain();
    }

    private boolean routeLengthDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return route1.getRouteLength() != route2.getRouteLength();
    }

    private boolean exposureDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return !route1.getExposure().equals(route2.getExposure());
    }

    private boolean rockfallPotentialDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return !route1.getRockfallPotential().equals(route2.getRockfallPotential());
    }

    private boolean routeFindingDiff (FourteenerRoute route1, FourteenerRoute route2) {
        return !route1.getRouteFinding().equals(route2.getRouteFinding());
    }

    private boolean commitmentDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return !route1.getCommitment().equals(route2.getCommitment());
    }

    private boolean hasMultipleRoutesDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return route1.hasMultipleRoutes() != route2.hasMultipleRoutes();
    }

    private boolean trailheadDiff(FourteenerRoute route1, FourteenerRoute route2) {
        return !route1.getTrailhead().equals(route2.getTrailhead());
    }









    private void checkFieldsOfMySqlCompareQuery () {
        if (getMountainName1() != null &&
                getRouteName1() != null &&
                getMountainName2() != null &&
                getRouteName2() != null &&
                getRouteUrls() != null) {

            throw new IllegalStateException("Option fields cannot be entered alongside parameter fields");
        }
    }

    public String getMountainName1() {
        return mountainName1;
    }

    public void setMountainName1(String mountainName1) {
        this.mountainName1 = mountainName1;
    }

    public String getRouteName1() {
        return routeName1;
    }

    public void setRouteName1(String routeName1) {
        this.routeName1 = routeName1;
    }

    public String getMountainName2() {
        return mountainName2;
    }

    public void setMountainName2(String mountainName2) {
        this.mountainName2 = mountainName2;
    }

    public String getRouteName2() {
        return routeName2;
    }

    public void setRouteName2(String routeName2) {
        this.routeName2 = routeName2;
    }

    public ArrayList<String> getRouteUrls() {
        return routeUrls;
    }

    public void setRouteUrls(ArrayList<String> routeUrls) {
        this.routeUrls = routeUrls;
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }
}
