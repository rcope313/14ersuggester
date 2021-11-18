package mysql.update;

import models.FourteenerRoute;
import models.Trailhead;
import mysql.MySqlConnection;
import utility.Utils;
import webscraper.FourteenerRouteScraper;
import webscraper.TrailheadScraper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;

public class UpdateTrailheads {

    public static void main (String[] args) throws Exception {

        updateAllRowsByUpdateDateCoordinates();
        System.out.print("Fingers crossed");

    }


    public static void weeklyUpdate (String strDate, String trailheadUrl) throws Exception {

        if (Utils.checkDateWeekly(strDate)) {

            Trailhead updatedTrailhead = TrailheadScraper.scrapeTrailhead(trailheadUrl);
            String sql = mySqlSyntaxWeeklyUpdate(updatedTrailhead);
            MySqlConnection.createStatement().execute(sql);

        }


    }

    static String mySqlSyntaxWeeklyUpdate(Trailhead trailhead) throws ParseException {

        if (trailhead == null) {
            return "SELECT * FROM trailheads";
        } else {
            return
                    "UPDATE hikesuggester.trailheads \n" +
                            "SET trailheads.name = '" + trailhead.getName() + "', \n" +
                            "trailheads.coordinates = '" + trailhead.getCoordinates() + "', \n" +
                            "trailheads.roadDifficulty= " + trailhead.getRoadDifficulty() + ", \n" +
                            "trailheads.roadDescription = '" + trailhead.getRoadDescription() + "', \n" +
                            "trailheads.winterAccess = '" + trailhead.getWinterAccess() + "', \n" +
                            "trailheads.updateDate = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE trailheads.url = '" + trailhead.getUrl() + "';";

        }
    }

    static void updateAllRowsByUpdateDateCoordinates () throws Exception {

        ArrayList<Trailhead> trailheads = TrailheadScraper.createListOfTrailheads();

        trailheads.forEach((trailhead) -> {
            try {
                MySqlConnection.createStatement().execute(mySqlSyntaxUpdateDateCoordinates(trailhead));
            } catch (SQLException | ParseException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    static String mySqlSyntaxUpdateDateCoordinates (Trailhead trailhead) throws ParseException {

        if (trailhead == null) {
            return "SELECT * FROM trailheads";
        } else {
            return
                    "UPDATE trailheads \n" +
                            "SET trailheads.coordinates = '" + trailhead.getCoordinates() + "', \n" +
                            "trailheads.updateDate = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE trailheads.url = '" + trailhead.getUrl() + "';";

        }
    }

    static void updateAllRowsByUrl () throws Exception {

        ArrayList<Trailhead> trailheads = TrailheadScraper.createListOfTrailheads();

        trailheads.forEach((trailhead) -> {
            try {
                MySqlConnection.createStatement().execute(mySqlSyntaxUpdateUrl(trailhead));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    static String mySqlSyntaxUpdateUrl (Trailhead trailhead) {

        if (trailhead == null) {
            return "SELECT * FROM trailheads";
        } else {
            return
                    "UPDATE trailheads \n" +
                            "SET trailheads.url = '" + trailhead.getUrl() + "' \n" +
                            "WHERE trailheads.trailheadId = " + trailhead.getTrailheadId() + ";";

        }
    }








}
