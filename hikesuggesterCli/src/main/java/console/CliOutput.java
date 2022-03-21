package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableCompareQuery;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRouteAndTrailhead;
import models.Column;
import java.util.ArrayList;
import java.util.StringJoiner;

public class CliOutput {

    public static void buildCliTable(ImmutableCompareQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = RoutesTrailheadsDao.get(query);
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
        createDifferenceString(routes.get(0), routes.get(1));
    }

    public static void buildCliTable(ImmutableSearchQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = RoutesTrailheadsDao.get(query);
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
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

    private static void inputImmutableStoredRouteAndTrailheadsIntoCliTable(ArrayList<ImmutableStoredRouteAndTrailhead> routes, ArrayList<Column> columnList) {
        routes.forEach((route) -> convertImmutableStoredRouteAndTrailheadIntoCliTable(route, columnList));
    }

    private static void convertImmutableStoredRouteAndTrailheadIntoCliTable(ImmutableStoredRouteAndTrailhead route, ArrayList<Column> columnList) {
        ArrayList<String> routeData = new ArrayList<>();
        routeData.add(route.getRouteName());
        routeData.add(route.getMountainName());
        routeData.add(String.valueOf(route.getGrade()));
        routeData.add(String.valueOf(route.getTotalGain()));
        routeData.add(String.valueOf(route.getRouteLength()));
        routeData.add(route.getRouteUrl());
        System.out.format(cliDataFormatter(columnList), routeData.toArray());
    }

    private static String cliDataFormatter(ArrayList<Column> columnList) {
        StringJoiner formatDataStringJoiner = new StringJoiner("");
        columnList.forEach((column) -> formatDataStringJoiner.add(column.getFormatString() + column.getFormatRegex()));
        formatDataStringJoiner.add("\n");
        return formatDataStringJoiner.toString();
    }

    private static void createDifferenceString(ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
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

        System.out.print(differencesStringJoiner);
    }

    private static void snowRouteDiff (StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if  (route0.getIsSnowRoute() != route1.getIsSnowRoute()) {
            differencesStringJoiner.add("Snow Route: " + route0.getIsSnowRoute() + ", " + route1.getIsSnowRoute() + "\n");
        }
    }

    private static void standardRouteDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getIsStandardRoute() != route1.getIsStandardRoute()) {
            differencesStringJoiner.add("Standard Route: " + route0.getIsStandardRoute() + ", " + route1.getIsStandardRoute());
        }
    }

    private static void gradeDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getGrade() != route1.getGrade()) {
            differencesStringJoiner.add("Grade: " + route0.getGrade() + ", " + route1.getGrade());
        }
    }

    private static void gradeQualityDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getGradeQuality().equals(route1.getGradeQuality())) {
            differencesStringJoiner.add("Grade Quality: " + route0.getGradeQuality() + ", " + route1.getGradeQuality());
        }
    }

    private static void startElevationDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getStartElevation() != route1.getStartElevation()) {
            differencesStringJoiner.add("Start Elevation: " + route0.getStartElevation() + ", " + route1.getStartElevation());
        }
    }

    private static void summitElevationDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getSummitElevation() != route1.getSummitElevation()) {
            differencesStringJoiner.add("Summit Elevation: " + route0.getSummitElevation() + ", " + route1.getSummitElevation());
        }
    }

    private static void totalGainDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getTotalGain() != route1.getTotalGain()) {
            differencesStringJoiner.add("Total Gain: " + route0.getTotalGain() + ", " + route1.getTotalGain());
        }
    }

    private static void routeLengthDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getRouteLength() != route1.getRouteLength()) {
            differencesStringJoiner.add("Route Length: " + route0.getRouteLength() + ", " + route1.getRouteLength());
        }
    }

    private static void exposureDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getExposure().equals(route1.getExposure())) {
            differencesStringJoiner.add("Exposure: " + route0.getExposure() + ", " + route1.getExposure());
        }
    }

    private static void rockfallPotentialDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getRockfallPotential().equals(route1.getRockfallPotential())) {
            differencesStringJoiner.add("Rockfall Potential: " + route0.getRockfallPotential() + ", " + route1.getRockfallPotential());
        }
    }

    private static void routeFindingDiff (StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getRouteFinding().equals(route1.getRouteFinding())) {
            differencesStringJoiner.add("Route Finding: " + route0.getRouteFinding() + ", " + route1.getRouteFinding());
        }
    }

    private static void commitmentDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getCommitment().equals(route1.getCommitment())) {
            differencesStringJoiner.add("Commitment: " + route0.getCommitment() + ", " + route1.getCommitment());
        }
    }

    private static void hasMultipleRoutesDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getHasMultipleRoutes() != route1.getHasMultipleRoutes()) {
            differencesStringJoiner.add("Multiple Routes: " + route0.getHasMultipleRoutes() + ", " + route1.getHasMultipleRoutes());
        }
    }

    private static void trailheadDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getTrailhead().equals(route1.getTrailhead())) {
            differencesStringJoiner.add("Trailhead: " + route0.getTrailhead()+ ", \n" + route1.getTrailhead());
        }
    }
}
