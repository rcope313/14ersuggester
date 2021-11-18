package mysql.query;


import models.HikeSuggesterDatabase;

import java.util.ArrayList;
import java.util.Arrays;

// create an index
public class MySqlCompareQuery {
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
        MySqlCompareQuery query = new MySqlCompareQuery();
        query.setRouteUrls(new ArrayList<>(Arrays.asList("aurl", "anotherurl")));
        System.out.print(query.createMySqlSyntax());
    }




    public void viewMySqlTableWithWeeklyUpdate()  {
        if (isVerbose()) {
            viewMySqlTableVerboseWithWeeklyUpdate();
        } else {
            viewMySqlTableNonVerboseWithWeeklyUpdate();
        }

    }

    private void viewMySqlTableNonVerboseWithWeeklyUpdate() {
    }

    private void viewMySqlTableVerboseWithWeeklyUpdate() {

    }

    private ArrayList<String> checkDifferences () {

        return null;

    }

    private String createMySqlSyntax () {
        if (getRouteUrls() == null) {
            return
                    "SELECT * " +
                            "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                            " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                            " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME  +
                            " WHERE " + HikeSuggesterDatabase.MOUNTAIN_NAME + " IN ('" + getMountainName1() + "', '" + getRouteName1() + "')" +
                            " AND " + HikeSuggesterDatabase.ROUTE_NAME + " IN ('" + getMountainName2() + "', '" + getRouteName2() + "');";

        } else {
            return
                    "SELECT * " +
                            "FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES +
                            " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS +
                            " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME +
                            " WHERE " + HikeSuggesterDatabase.ROUTE_URL + " IN ('" + getRouteUrls().get(0) + "', '" + getRouteUrls().get(1) + "');";


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

    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }
}
