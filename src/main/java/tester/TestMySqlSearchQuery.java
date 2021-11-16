//package tester;
//
//import mysql.query.MySqlSearchQuery;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class TestMySqlSearchQuery {
//
//    MySqlSearchQuery q0, q1, q2, q3, q4, q5, q6, q7, q8, q9, q10;
//    String str0, str1, str2, str3, str4, str5, str6, str7, str8, str9, str10;
//
//    public void initData() {
//        q0 = new MySqlSearchQuery();
//        q1 = new MySqlSearchQuery();
//            q1.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
//        q2 = new MySqlSearchQuery();
//            q2.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
//            q2.setStandardRoute(true);
//        q3 = new MySqlSearchQuery();
//            q3.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
//            q3.setStandardRoute(true);
//            q3.setTotalGain(5000);
//            q3.setRouteLength(4.5);
//            q3.setVerbose(true);
//        q4 = new MySqlSearchQuery();
//            q4.setRouteNames (new ArrayList<>(Arrays.asList("Cassin Ridge", "EM Greenman")));
//            q4.setSnowRoute(true);
//        q5 = new MySqlSearchQuery();
//            q5.setStartElevation(8000);
//            q5.setSummitElevation(14000);
//        q6 = new MySqlSearchQuery();
//            q6.setExposure("low");
//            q6.setRouteFinding("low");
//            q6.setRockfallPotential("low");
//            q6.setCommitment("low");
//        q7 = new MySqlSearchQuery();
//            q7.setGrades(new ArrayList<>(Arrays.asList(1,2,3)));
//            q7.setGradeQualities(new ArrayList<>(Arrays.asList("Easy", "Difficult")));
//        q8 = new MySqlSearchQuery();
//            q8.setTrailheadCoordinates(new ArrayList<>(Arrays.asList("39.15177, -106.41918 ", "39.06788, -106.50515 ")));
//            q8.setRoadDifficulties(new ArrayList<>(Arrays.asList(1,2,3)));
//        q9 = new MySqlSearchQuery();
//            q9.setTrailheadUrls(new ArrayList<>(Arrays.asList("/trailheadsview.php?thparm=sw01", "/trailheadsview.php?thparm=sw03")));
//            q9.setRouteUrls(new ArrayList<>(Arrays.asList("https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=elbe2")));
//        q10 = new MySqlSearchQuery();
//            q10.setQuery("WHERE fourteenerRouteId >= 0;");
//
//
//
//        str0 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0                    ;";
//        str1 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0 " +
//                "AND fourteener_routes.mountainName IN ('Mt. Washington', 'Mt. Adams', 'Mt. Monroe')                    ;";
//        str2 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0 " +
//                "AND fourteener_routes.mountainName IN ('Mt. Washington', 'Mt. Adams', 'Mt. Monroe')    " +
//                "AND fourteener_routes.isStandardRoute = true                ;";
//        str3 = "SELECT * " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0 " +
//                "AND fourteener_routes.mountainName IN ('Mt. Washington', 'Mt. Adams', 'Mt. Monroe')    " +
//                "AND fourteener_routes.isStandardRoute = true      " +
//                "AND fourteener_routes.totalGain >= 5000 " +
//                "AND fourteener_routes.routeLength >= 4.5         ;";
//        str4 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0  " +
//                "AND fourteener_routes.routeName IN ('Cassin Ridge', 'EM Greenman')  " +
//                "AND fourteener_routes.isSnowRoute = true                 ;";
//        str5 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0        " +
//                "AND fourteener_routes.startElevation >= 8000 " +
//                "AND fourteener_routes.summitElevation >= 14000           ;";
//        str6 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0            " +
//                "AND fourteener_routes.exposure = 'low' " +
//                "AND fourteener_routes.rockfallPotential = 'low' " +
//                "AND fourteener_routes.routeFinding = 'low' " +
//                "AND fourteener_routes.commitment = 'low'     ;";
//        str7 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0     " +
//                "AND fourteener_routes.grade IN (1, 2, 3)  " +
//                "AND fourteener_routes.gradeQuality IN ('Easy', 'Difficult')               ;";
//        str8 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0                  " +
//                "AND trailheads.coordinates IN ('39.15177, -106.41918 ', '39.06788, -106.50515 ')  " +
//                "AND trailheads.roadDifficulty IN (1, 2, 3)  ;";
//
//        str9 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0                 " +
//                "AND fourteener_routes.url IN ('https://www.14ers.com/route.php?route=elbe1', 'https://www.14ers.com/route.php?route=elbe2')    " +
//                "AND trailheads.url IN ('/trailheadsview.php?thparm=sw01', '/trailheadsview.php?thparm=sw03') ;";
//        str10 = "SELECT fourteener_routes.routeName, " +
//                "fourteener_routes.mountainName, fourteener_routes.totalGain, " +
//                "fourteener_routes.routeLength, fourteener_routes.url " +
//                "FROM fourteener_routes " +
//                "LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name " +
//                "WHERE fourteenerRouteId >= 0;";
//
//
//    }
//
//    void testCreateSearchQuery (Tester t) {
//        this.initData();
//        t.checkExpect(q0.createSearchQuery(), str0);
//        t.checkExpect(q1.createSearchQuery(), str1);
//        t.checkExpect(q2.createSearchQuery(), str2);
//        t.checkExpect(q3.createSearchQuery(), str3);
//        t.checkExpect(q4.createSearchQuery(), str4);
//        t.checkExpect(q5.createSearchQuery(), str5);
//        t.checkExpect(q6.createSearchQuery(), str6);
//        t.checkExpect(q7.createSearchQuery(), str7);
//        t.checkExpect(q8.createSearchQuery(), str8);
//        t.checkExpect(q9.createSearchQuery(), str9);
//        t.checkExpect(q10.createSearchQuery(), str10);
//
//    }
//}
