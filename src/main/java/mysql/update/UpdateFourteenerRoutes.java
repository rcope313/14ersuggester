package mysql.update;

import models.FourteenerRoute;
import mysql.MySqlConnection;
import utility.Utils;
import webscraper.FourteenerRouteScraper;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;


public class UpdateFourteenerRoutes {


    public static void main (String[] args) throws Exception {

        updateAllRowsByTrailheadUpdateDateRouteLength();
        System.out.print("Fingers crossed");

    }

    public static void weeklyUpdate (String strDate, String routeUrl) throws ParseException, SQLException, IOException {

        if (Utils.checkDateWeekly(strDate)) {

            FourteenerRoute updatedRoute = FourteenerRouteScraper.scrapeFourteener(routeUrl);
            String sql = mySqlSyntaxWeeklyUpdate(updatedRoute);
            MySqlConnection.createStatement().execute(sql);

        }


    }

    static String mySqlSyntaxWeeklyUpdate(FourteenerRoute route) throws ParseException {

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
                            "fourteener_routes.trailhead = '" + route.getTrailhead() + "', \n" +
                            "fourteener_routes.updateDate = '" + java.time.LocalDate.now() + "' \n" +
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
