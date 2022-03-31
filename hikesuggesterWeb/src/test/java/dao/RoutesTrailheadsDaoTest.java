package dao;

import database.dao.RoutesTrailheadsDao;
import database.models.SearchQuery;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class RoutesTrailheadsDaoTest {
    SearchQuery q0, q1, q2, q3, q4, q5, q6, q7, q8, q9;
    String str0, str1, str2, str3, str4, str5, str6, str7, str8, str9;
    Connection conn;


    @Before
    public void getConnection() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:myDb", "sa", "sa");
    }

    @Before
    public void initData() {
        q0 = new SearchQuery();
        q1 = new SearchQuery();
            q1.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
        q2 = new SearchQuery();
            q2.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
            q2.setStandardRoute(true);
        q3 = new SearchQuery();
            q3.setMountainNames(new ArrayList<>(Arrays.asList("Mt. Washington", "Mt. Adams", "Mt. Monroe")));
            q3.setStandardRoute(true);
            q3.setTotalGain(5000);
            q3.setRouteLength(4.5);
        q4 = new SearchQuery();
            q4.setRouteNames (new ArrayList<>(Arrays.asList("Cassin Ridge", "EM Greenman")));
            q4.setSnowRoute(true);
        q5 = new SearchQuery();
            q5.setStartElevation(8000);
            q5.setSummitElevation(14000);
        q6 = new SearchQuery();
            q6.setExposure("low");
            q6.setRouteFinding("low");
            q6.setRockfallPotential("low");
            q6.setCommitment("low");
        q7 = new SearchQuery();
            q7.setGrades(new ArrayList<>(Arrays.asList(1,2,3)));
            q7.setGradeQualities(new ArrayList<>(Arrays.asList("Easy", "Difficult")));
        q8 = new SearchQuery();
            q8.setRoadDifficulties(new ArrayList<>(Arrays.asList(1,2,3)));
        q9 = new SearchQuery();
            q9.setTrailheadUrls(new ArrayList<>(Arrays.asList("https://www.14ers.com/php14ers/trailheadsview.php?thparm=sw01", "https://www.14ers.com/php14ers/trailheadsview.php?thparm=sw03")));
            q9.setRouteUrls(new ArrayList<>(Arrays.asList("https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=elbe2")));


        str0 = "SELECT * FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads" +
                " ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0                    ;";
        str1 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0" +
                " AND fourteener_routes.mountainName IN ('Mt. Washington', 'Mt. Adams', 'Mt. Monroe')                    ;";
        str2 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0" +
                " AND fourteener_routes.mountainName IN ('Mt. Washington', 'Mt. Adams', 'Mt. Monroe')   " +
                " AND fourteener_routes.isStandardRoute = true                ;";
        str3 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0" +
                " AND fourteener_routes.mountainName IN ('Mt. Washington', 'Mt. Adams', 'Mt. Monroe')   " +
                " AND fourteener_routes.isStandardRoute = true     " +
                " AND fourteener_routes.totalGain >= 5000" +
                " AND fourteener_routes.routeLength >= 4.5         ;";
        str4 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0 " +
                " AND fourteener_routes.routeName IN ('Cassin Ridge', 'EM Greenman') " +
                " AND fourteener_routes.isSnowRoute = true                 ;";
        str5 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0       " +
                " AND fourteener_routes.startElevation >= 8000" +
                " AND fourteener_routes.summitElevation >= 14000           ;";
        str6 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0           " +
                " AND fourteener_routes.exposure = 'low'" +
                " AND fourteener_routes.rockfallPotential = 'low'" +
                " AND fourteener_routes.routeFinding = 'low'" +
                " AND fourteener_routes.commitment = 'low'     ;";
        str7 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0    " +
                " AND fourteener_routes.grade IN (1, 2, 3) " +
                " AND fourteener_routes.gradeQuality IN ('Easy', 'Difficult')               ;";
        str8 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0                 " +
                " AND trailheads.coordinates IN ('39.15177, -106.41918', '39.06788, -106.50515') " +
                " AND trailheads.roadDifficulty IN (1, 2, 3)  ;";

        str9 = "SELECT *" +
                " FROM fourteener_routes" +
                " LEFT OUTER JOIN trailheads ON fourteener_routes.trailhead = trailheads.name" +
                " WHERE fourteener_routes.fourteenerRouteId >= 0                " +
                " AND fourteener_routes.url IN ('https://www.14ers.com/route.php?route=elbe1', 'https://www.14ers.com/route.php?route=elbe2')   " +
                " AND trailheads.url IN ('https://www.14ers.com/php14ers/trailheadsview.php?thparm=sw01', 'https://www.14ers.com/php14ers/trailheadsview.php?thparm=sw03') ;";
    }

    @Test
    public void itInsertsRouteIntoDatabase() {
        RoutesTrailheadsDao dao = new RoutesTrailheadsDao(conn);

    }

    @Test
    public void itGetsRouteFromDatabase() {

    }

    @Test
    public void itUpdatesRouteInDatabase() {

    }

    @Test
    public void itBuildsCompareQuery() {

    }

    @Test
    public void itBuildsSearchQuery() {

    }
}
