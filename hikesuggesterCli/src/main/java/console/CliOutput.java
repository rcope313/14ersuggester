package console;

import database.dao.FourteenerRoutesDao;
import database.models.ImmutableCompareQuery;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRoute;
import models.Column;
import models.FourteenerRoute;

import java.util.ArrayList;
import java.util.StringJoiner;

public class CliOutput {

    public static void buildCliTable(ImmutableCompareQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRoute> routes = FourteenerRoutesDao.get(query);
        inputImmutableStoredRoutesIntoCliTable(routes, designateColumnFields());
    }

    public static void buildCliTable(ImmutableSearchQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRoute> routes = FourteenerRoutesDao.get(query);
        inputImmutableStoredRoutesIntoCliTable(routes, designateColumnFields());
    }

    private static ArrayList<Column> designateColumnFields() {
        ArrayList<Column> columnFields = new ArrayList<>();
        columnFields.add(Column.MOUNTAIN_NAME);
        columnFields.add(Column.ROUTE_NAME);
        columnFields.add(Column.GRADE);
        columnFields.add(Column.TOTAL_GAIN);
        columnFields.add(Column.ROUTE_LENGTH);
        columnFields.add(Column.ROUTE_URL);
        return columnFields;
    }

    private static void buildCliTableHeaders(ArrayList<Column> columnList) {
        ArrayList<String> cliTableHeaders = new ArrayList<>();
        columnList.forEach((column) -> cliTableHeaders.add(column.getCliColumn()));
        Object[] cliTableHeadersArray = cliTableHeaders.toArray();
        System.out.format(cliHeaderFormatter(columnList), cliTableHeadersArray);
    }

    private static String cliHeaderFormatter(ArrayList<Column> columnList) {
        StringJoiner formatHeaderStringJoiner = new StringJoiner("");
        columnList.forEach((column) -> formatHeaderStringJoiner.add(column.getFormatString() + "s"));
        formatHeaderStringJoiner.add("\n");
        return formatHeaderStringJoiner.toString();
    }

    private static void inputImmutableStoredRoutesIntoCliTable(ArrayList<ImmutableStoredRoute> routes, ArrayList<Column> columnList) {
        routes.forEach((route) -> convertImmutableStoredRouteIntoCliTable(route, columnList));
    }

    private static void convertImmutableStoredRouteIntoCliTable(ImmutableStoredRoute route, ArrayList<Column> columnList) {
        ArrayList<String> routeData = new ArrayList<>();
        routeData.add(route.getRouteName());
        routeData.add(route.getMountainName());
        routeData.add(String.valueOf(route.getGrade()));
        routeData.add(String.valueOf(route.getTotalGain()));
        routeData.add(String.valueOf(route.getRouteLength()));
        routeData.add(route.getUrl());
        System.out.format(cliDataFormatter(columnList), routeData.toArray());
    }

    private static String cliDataFormatter(ArrayList<Column> columnList) {
        StringJoiner formatDataStringJoiner = new StringJoiner("");
        columnList.forEach((column) -> formatDataStringJoiner.add(column.getFormatString() + column.getFormatRegex()));
        formatDataStringJoiner.add("\n");
        return formatDataStringJoiner.toString();
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
}
