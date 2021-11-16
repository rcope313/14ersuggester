package mysql.update;

import models.FourteenerRoute;
import mysql.MySqlConnection;
import webscraper.FourteenerRouteScraper;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


public class UpdateFourteenerRoutes {


    public static void main (String[] args) throws Exception {

        updateAllRowsByTrailheadUpdateDateRouteLength();
        System.out.print("Fingers crossed");

    }


    static void updateARowByAllColumns (FourteenerRoute route) throws ParseException, SQLException {

        String sql = mySqlSyntaxUpdateAllColumns(route);
        MySqlConnection.createStatement().execute(sql);


    }

    static String mySqlSyntaxUpdateAllColumns (FourteenerRoute route) throws ParseException {

        if (route == null) {
            return "SELECT * FROM fourteener_routes";
        } else {
            return
                    "UPDATE fourteener_routes \n" +
                            "SET routeName = '" + route.getRouteName() + "', \n" +
                            "mountainName = '" + route.getMountainName() + "', \n" +
                            "isSnowRoute = " + route.isSnowRoute() + ", \n" +
                            "isStandardRoute = " + route.isStandardRoute() + ", \n" +
                            "grade = " + route.getGradeQuality().getGrade() + ", \n" +
                            "gradeQuality = '" + route.getGradeQuality().getQuality() + "', \n" +
                            "startElevation = " + route.getStartElevation() + ", \n" +
                            "summitElevation = " + route.getSummitElevation() + ", \n" +
                            "totalGain = " + route.getTotalGain() + ", \n" +
                            "routeLength = " + route.getRouteLength() + ", \n" +
                            "rockfallPotential = '" + route.getRockfallPotential() + "', \n" +
                            "routeFinding = '" + route.getRouteFinding() + "', \n" +
                            "commitment = '" + route.getCommitment() + "', \n" +
                            "hasMultipleRoutes = " + route.hasMultipleRoutes() + ", \n" +
                            "trailhead = '" + route.getTrailhead() + "', \n" +
                            "updateDate = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE fourteener_routes.url = '" + route.getUrl() + "';";

        }
    }



    static void updateAllRowsByTrailheadUpdateDateRouteLength () throws Exception {

        ArrayList<FourteenerRoute> routes = FourteenerRouteScraper.createListOfFourteenerRoutes();

        routes.forEach((route) -> {
            try {
                MySqlConnection.createStatement().execute(mySqlSyntaxUpdateTrailheadUpdateDateRouteLength(route));
            } catch (SQLException | ParseException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    static String mySqlSyntaxUpdateTrailheadUpdateDateRouteLength (FourteenerRoute route) throws ParseException {

        if (route == null) {
            return "SELECT * FROM fourteener_routes";
        } else {
            return
                    "UPDATE fourteener_routes \n" +
                            "SET trailhead = '" + route.getTrailhead() + "', \n" +
                            "routeLength = '" + route.getRouteLength() + "', \n" +
                            "updateDate = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE fourteener_routes.url = '" + route.getUrl() + "';";

        }
    }


}
