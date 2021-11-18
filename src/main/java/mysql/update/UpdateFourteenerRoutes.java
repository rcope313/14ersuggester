package mysql.update;

import models.FourteenerRoute;
import models.HikeSuggesterDatabase;
import mysql.MySqlConnection;
import utility.Utils;
import webscraper.FourteenerRouteScraper;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;


public class UpdateFourteenerRoutes {


    public static void weeklyUpdate (String strDate, String routeUrl) throws ParseException, SQLException, IOException {

        if (Utils.checkDateWeekly(strDate)) {

            FourteenerRoute updatedRoute = FourteenerRouteScraper.scrapeFourteener(routeUrl);
            String sql = mySqlSyntaxWeeklyUpdate(updatedRoute);
            MySqlConnection.createStatement().execute(sql);

        }


    }

    static String mySqlSyntaxWeeklyUpdate(FourteenerRoute route) throws ParseException {

        if (route == null) {
            return "SELECT * FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES;
        } else {
            return
                    "UPDATE " + HikeSuggesterDatabase.FOURTEENER_ROUTES + "\n" +
                            "SET " + HikeSuggesterDatabase.ROUTE_NAME + " = '" + route.getRouteName() + "', \n" +
                            HikeSuggesterDatabase.MOUNTAIN_NAME + " = '" + route.getMountainName() + "', \n" +
                            HikeSuggesterDatabase.IS_SNOW_ROUTE + " = " + route.isSnowRoute() + ", \n" +
                            HikeSuggesterDatabase.IS_STANDARD_ROUTE + " = " + route.isStandardRoute() + ", \n" +
                            HikeSuggesterDatabase.GRADE + " = " + route.getGradeQuality().getGrade() + ", \n" +
                            HikeSuggesterDatabase.GRADE_QUALITY + " = '" + route.getGradeQuality().getQuality() + "', \n" +
                            HikeSuggesterDatabase.START_ELEVATION + " = " + route.getStartElevation() + ", \n" +
                            HikeSuggesterDatabase.SUMMIT_ELEVATION + " = " + route.getSummitElevation() + ", \n" +
                            HikeSuggesterDatabase.TOTAL_GAIN + " = " + route.getTotalGain() + ", \n" +
                            HikeSuggesterDatabase.ROUTE_LENGTH + " = " + route.getRouteLength() + ", \n" +
                            HikeSuggesterDatabase.ROCKFALL_POTENTIAL + " = '" + route.getRockfallPotential() + "', \n" +
                            HikeSuggesterDatabase.ROUTE_FINDING + " = '" + route.getRouteFinding() + "', \n" +
                            HikeSuggesterDatabase.COMMITMENT + " = '" + route.getCommitment() + "', \n" +
                            HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES + " = " + route.hasMultipleRoutes() + ", \n" +
                            HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = '" + route.getTrailhead() + "', \n" +
                            HikeSuggesterDatabase.ROUTE_UPDATE_DATE + " = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE " + HikeSuggesterDatabase.ROUTE_URL + " = '" + route.getUrl() + "';";

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
            return "SELECT * FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES;
        } else {
            return
                    "UPDATE " + HikeSuggesterDatabase.FOURTEENER_ROUTES + "\n" +
                            "SET " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = '" + route.getTrailhead() + "', \n" +
                            HikeSuggesterDatabase.ROUTE_LENGTH + " = '" + route.getRouteLength() + "', \n" +
                            HikeSuggesterDatabase.ROUTE_UPDATE_DATE + " = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE " + HikeSuggesterDatabase.ROUTE_URL + " = '" + route.getUrl() + "';";

        }
    }


}
