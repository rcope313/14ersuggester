package tester;

import mysql.MySqlSearchQuery;

import java.util.ArrayList;
import java.util.Arrays;

public class TestMySqlSearchQuery {

    MySqlSearchQuery q0, q1, q2, q3, q4, q5, q6;
    String str0, str1, str2, str3, str4, str5, str6;

    public void initData() {
        q0 = new MySqlSearchQuery();
        q1 = new MySqlSearchQuery();
            q1.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
        q2 = new MySqlSearchQuery();
            q2.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
            q2.setStandardRoute(true);
        q3 = new MySqlSearchQuery();
            q3.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
            q3.setStandardRoute(true);
            q3.setTotalGain(5000);
            q3.setRouteLength(4.5);
            q3.setVerbose(true);
        q4 = new MySqlSearchQuery();
            q4.setRouteNames (new ArrayList<>(Arrays.asList("Cassin Ridge", "EM Greenman")));
            q4.setSnowRoute(true);
        q5 = new MySqlSearchQuery();
            q5.setStartElevation(8000);
            q5.setSummitElevation(14000);
        q6 = new MySqlSearchQuery();
            q6.setExposure("low");
            q6.setRouteFinding("low");
            q6.setRockfallPotential("low");
            q6.setCommitment("low");


        str0 = "SELECT fourteener_routes.routeName, fourteener_routes.mountainName, fourteener_routes.url " +
                "FROM hike_suggester.fourteener_routes " +
                "WHERE fourteenerRouteId > 1                  ;";
        str1 = "SELECT fourteener_routes.routeName, fourteener_routes.mountainName, fourteener_routes.url " + "" +
                "FROM hike_suggester.fourteener_routes " +
                "WHERE fourteenerRouteId > 1 " +
                "AND fourteener_routes.mountainName = 'Mt. Washington' " +
                "AND fourteener_routes.mountainName = 'Mt. Adams' " +
                "AND fourteener_routes.mountainName = 'Mt. Monroe'                 ;";
        str2 = "SELECT fourteener_routes.routeName, fourteener_routes.mountainName, fourteener_routes.url " + "" +
                "FROM hike_suggester.fourteener_routes " +
                "WHERE fourteenerRouteId > 1 " +
                "AND fourteener_routes.mountainName = 'Mt. Washington' " +
                "AND fourteener_routes.mountainName = 'Mt. Adams' " +
                "AND fourteener_routes.mountainName = 'Mt. Monroe' " +
                "  AND fourteener_routes.isStandardRoute = true              ;";
        str3 = "SELECT * " + "" +
                "FROM hike_suggester.fourteener_routes " +
                "WHERE fourteenerRouteId > 1 " +
                "AND fourteener_routes.mountainName = 'Mt. Washington' " +
                "AND fourteener_routes.mountainName = 'Mt. Adams' " +
                "AND fourteener_routes.mountainName = 'Mt. Monroe' " +
                "  AND fourteener_routes.isStandardRoute = true    " +
                "AND fourteener_routes.totalGain = 5000 AND fourteener_routes.routeLength = 4.5         ;";
        str4 = "SELECT fourteener_routes.routeName, fourteener_routes.mountainName, fourteener_routes.url " + "" +
                "FROM hike_suggester.fourteener_routes " +
                "WHERE fourteenerRouteId > 1 " +
                " AND fourteener_routes.routeName = 'Cassin Ridge' " +
                " AND fourteener_routes.routeName = 'EM Greenman' " +
                " AND fourteener_routes.isSnowRoute = true               ;";
        str5 = "SELECT fourteener_routes.routeName, fourteener_routes.mountainName, fourteener_routes.url " + "" +
                "FROM hike_suggester.fourteener_routes " +
                "WHERE fourteenerRouteId > 1 " +
                "     AND fourteener_routes.startElevation = 8000 " +
                "AND fourteener_routes.summitElevation = 14000           ;";
        str6 = "SELECT fourteener_routes.routeName, fourteener_routes.mountainName, fourteener_routes.url " + "" +
                "FROM hike_suggester.fourteener_routes " +
                "WHERE fourteenerRouteId > 1 " +
                "         AND fourteener_routes.exposure = 'low' " +
                "AND fourteener_routes.rockfallPotential = 'low' " +
                "AND fourteener_routes.routeFinding = 'low' " +
                "AND fourteener_routes.commitment = 'low'     ;";

    }

    void testCreateSearchQuery (Tester t) {
        this.initData();
        t.checkExpect(q0.createSearchQuery(), str0);
        t.checkExpect(q1.createSearchQuery(), str1);
        t.checkExpect(q2.createSearchQuery(), str2);
        t.checkExpect(q3.createSearchQuery(), str3);
        t.checkExpect(q4.createSearchQuery(), str4);
        t.checkExpect(q5.createSearchQuery(), str5);
        t.checkExpect(q6.createSearchQuery(), str6);
    }
}
