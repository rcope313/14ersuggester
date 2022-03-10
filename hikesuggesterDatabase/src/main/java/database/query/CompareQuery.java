package database.query;

import models.FourteenerRoute;
import models.HikeSuggesterDatabase;
import java.util.ArrayList;
import java.util.StringJoiner;

public class CompareQuery implements Query{
    private String mountainName1;
    private String routeName1;
    private String mountainName2;
    private String routeName2;
    private ArrayList<String> routeUrls;

    public CompareQuery() {
        checkFieldsOfMySqlCompareQuery();
    }

    public String createQuerySyntax() {
        if (getRouteUrls() == null) {
            return "SELECT * " +
                    "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                    " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                    " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME  +
                    " WHERE " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN ('" + getMountainName1() + "', '" + getMountainName2() + "')" +
                    " AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN ('" + getRouteName1() + "', '" + getRouteName2() + "');";

        } else {
            return "SELECT * " +
                    "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                    " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                    " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME +
                    " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " IN ('" + getRouteUrls().get(0) + "', '" + getRouteUrls().get(1) + "');";
        }
    }

    public String createDifferenceString (FourteenerRoute route0, FourteenerRoute route1) {
        StringJoiner differencesStringJoiner = new StringJoiner("\n");

        snowRouteDiff(differencesStringJoiner, route0, route1);
        standardRouteDiff(differencesStringJoiner, route0, route1);
        gradeDiff(differencesStringJoiner, route0, route1);
        gradeQualityDiff(differencesStringJoiner, route0, route1);
        startElevationDiff(differencesStringJoiner, route0, route1);
        summitElevationDiff(differencesStringJoiner, route0, route1);
        totalGainDiff(differencesStringJoiner, route0, route1);
        routeLengthDiff(differencesStringJoiner, route0, route1);
        exposureDiff(differencesStringJoiner, route0, route1);
        rockfallPotentialDiff(differencesStringJoiner, route0, route1);
        routeFindingDiff(differencesStringJoiner, route0, route1);
        commitmentDiff(differencesStringJoiner, route0, route1);
        hasMultipleRoutesDiff(differencesStringJoiner, route0, route1);
        trailheadDiff(differencesStringJoiner, route0, route1);

        return differencesStringJoiner.toString();
    }

    private void snowRouteDiff (StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if  (route0.isSnowRoute() != route1.isSnowRoute()) {
             differencesStringJoiner.add("Snow Route: " + route0.isSnowRoute() + ", " + route1.isSnowRoute() + "\n");
        }
    }

    private void standardRouteDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.isStandardRoute() != route1.isStandardRoute()) {
            differencesStringJoiner.add("Standard Route: " + route0.isStandardRoute() + ", " + route1.isStandardRoute());
        }
    }

    private void gradeDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getGradeQuality().getGrade() != route1.getGradeQuality().getGrade()) {
            differencesStringJoiner.add("Grade: " + route0.getGradeQuality().getGrade() + ", " + route1.getGradeQuality().getGrade());
        }
    }

    private void gradeQualityDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getGradeQuality().getQuality().equals(route1.getGradeQuality().getQuality())) {
            differencesStringJoiner.add("Grade Quality: " + route0.getGradeQuality().getQuality() + ", " + route1.getGradeQuality().getQuality());
        }
    }

    private void startElevationDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getStartElevation() != route1.getStartElevation()) {
            differencesStringJoiner.add("Start Elevation: " + route0.getStartElevation() + ", " + route1.getStartElevation());
        }
    }

    private void summitElevationDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getSummitElevation() != route1.getSummitElevation()) {
            differencesStringJoiner.add("Summit Elevation: " + route0.getSummitElevation() + ", " + route1.getSummitElevation());
        }
    }

    private void totalGainDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getTotalGain() != route1.getTotalGain()) {
            differencesStringJoiner.add("Total Gain: " + route0.getTotalGain() + ", " + route1.getTotalGain());
        }
    }

    private void routeLengthDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getRouteLength() != route1.getRouteLength()) {
            differencesStringJoiner.add("Route Length: " + route0.getRouteLength() + ", " + route1.getRouteLength());
        }
    }

    private void exposureDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getExposure().equals(route1.getExposure())) {
            differencesStringJoiner.add("Exposure: " + route0.getExposure() + ", " + route1.getExposure());
        }
    }

    private void rockfallPotentialDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getRockfallPotential().equals(route1.getRockfallPotential())) {
            differencesStringJoiner.add("Rockfall Potential: " + route0.getRockfallPotential() + ", " + route1.getRockfallPotential());
        }
    }

    private void routeFindingDiff (StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getRouteFinding().equals(route1.getRouteFinding())) {
            differencesStringJoiner.add("Route Finding: " + route0.getRouteFinding() + ", " + route1.getRouteFinding());
        }
    }

    private void commitmentDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getCommitment().equals(route1.getCommitment())) {
            differencesStringJoiner.add("Commitment: " + route0.getCommitment() + ", " + route1.getCommitment());
        }
    }

    private void hasMultipleRoutesDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.hasMultipleRoutes() != route1.hasMultipleRoutes()) {
            differencesStringJoiner.add("Multiple Routes: " + route0.hasMultipleRoutes() + ", " + route1.hasMultipleRoutes());
        }
    }

    private void trailheadDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getTrailhead().equals(route1.getTrailhead())) {
            differencesStringJoiner.add("Trailhead: " + route0.getTrailhead()+ ", \n" + route1.getTrailhead());
        }
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
}