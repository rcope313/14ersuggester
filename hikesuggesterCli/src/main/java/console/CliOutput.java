package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableCompareQuery;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRouteAndTrailhead;
import models.Column;
import webscraper.FourteenerRouteScraper;

import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class CliOutput {

    static ArrayList<Column> designateColumnFields() {
        ArrayList<Column> columnFields = new ArrayList<>();
        columnFields.add(Column.MOUNTAIN_NAME);
        columnFields.add(Column.ROUTE_NAME);
        columnFields.add(Column.GRADE);
        columnFields.add(Column.TOTAL_GAIN);
        columnFields.add(Column.ROUTE_LENGTH);
        columnFields.add(Column.ROUTE_URL);
        return columnFields;
    }

    static void buildCliTableHeaders(ArrayList<Column> columnList) {
        ArrayList<String> cliTableHeaders = new ArrayList<>();
        columnList.forEach((column) -> cliTableHeaders.add(column.getCliColumn()));
        Object[] cliTableHeadersArray = cliTableHeaders.toArray();
        System.out.format(cliHeaderFormatter(columnList), cliTableHeadersArray);
    }

    static String cliHeaderFormatter(ArrayList<Column> columnList) {
        StringJoiner formatHeaderStringJoiner = new StringJoiner("");
        columnList.forEach((column) -> formatHeaderStringJoiner.add(column.getFormatString() + "s"));
        formatHeaderStringJoiner.add("\n");
        return formatHeaderStringJoiner.toString();
    }

    static void inputImmutableStoredRouteAndTrailheadsIntoCliTable(ArrayList<ImmutableStoredRouteAndTrailhead> routes, ArrayList<Column> columnList) {
        routes.forEach((route) -> convertImmutableStoredRouteAndTrailheadIntoCliTable(route, columnList));
    }

    static void convertImmutableStoredRouteAndTrailheadIntoCliTable(ImmutableStoredRouteAndTrailhead route, ArrayList<Column> columnList) {
        ArrayList<String> routeData = new ArrayList<>();
        routeData.add(route.getRouteName());
        routeData.add(route.getMountainName());
        routeData.add(String.valueOf(route.getGrade()));
        routeData.add(String.valueOf(route.getTotalGain()));
        routeData.add(String.valueOf(route.getRouteLength()));
        routeData.add(route.getRouteUrl());
        System.out.format(cliDataFormatter(columnList), routeData.toArray());
    }

    static String cliDataFormatter(ArrayList<Column> columnList) {
        StringJoiner formatDataStringJoiner = new StringJoiner("");
        columnList.forEach((column) -> formatDataStringJoiner.add(column.getFormatString() + column.getFormatRegex()));
        formatDataStringJoiner.add("\n");
        return formatDataStringJoiner.toString();
    }

}
