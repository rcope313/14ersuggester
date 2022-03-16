package console;

import database.dao.FourteenerRoutesDao;
import database.models.ImmutableCompareQuery;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRoute;
import models.Column;
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
}
