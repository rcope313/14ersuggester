package database.update;

import models.HikeSuggesterDatabase;
import models.Trailhead;
import database.DatabaseConnection;
import utility.Utils;
import webscraper.TrailheadScraper;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

public class UpdateTrailheads {

    public static void weeklyUpdate (String strDate, String trailheadUrl) throws Exception {
        if (UpdateUtils.checkDateWeekly(strDate)) {
            Trailhead updatedTrailhead = TrailheadScraper.scrapeTrailhead(trailheadUrl);
            String sql = mySqlSyntaxWeeklyUpdate(updatedTrailhead);
            DatabaseConnection.createStatement().execute(sql);
        }
    }

    static String mySqlSyntaxWeeklyUpdate(Trailhead trailhead) throws ParseException {

        if (trailhead == null) {
            return "SELECT * FROM " + HikeSuggesterDatabase.TRAILHEADS;
        } else {
            return
                    "UPDATE " + HikeSuggesterDatabase.TRAILHEADS + "\n" +
                            "SET " + HikeSuggesterDatabase.TRAILHEAD_NAME + " = '" + trailhead.getName() + "', \n" +
                            HikeSuggesterDatabase.COORDINATES + " = '" + trailhead.getCoordinates() + "', \n" +
                            HikeSuggesterDatabase.ROAD_DIFFICULTY + " = " + trailhead.getRoadDifficulty() + ", \n" +
                            HikeSuggesterDatabase.ROAD_DESCRIPTION + " = '" + trailhead.getRoadDescription() + "', \n" +
                            HikeSuggesterDatabase.WINTER_ACCESS + " = '" + trailhead.getWinterAccess() + "', \n" +
                            HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE + " = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE " + HikeSuggesterDatabase.TRAILHEAD_URL + " = '" + trailhead.getUrl() + "';";

        }
    }

    static void updateAllRowsByUpdateDateCoordinates () throws Exception {

        ArrayList<Trailhead> trailheads = TrailheadScraper.createListOfTrailheads();

        trailheads.forEach((trailhead) -> {
            try {
                DatabaseConnection.createStatement().execute(mySqlSyntaxUpdateDateCoordinates(trailhead));
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        });

    }

    static String mySqlSyntaxUpdateDateCoordinates (Trailhead trailhead) throws ParseException {

        if (trailhead == null) {
            return "SELECT * FROM " + HikeSuggesterDatabase.TRAILHEADS;
        } else {
            return
                    "UPDATE " + HikeSuggesterDatabase.TRAILHEADS  + "\n" +
                            "SET " + HikeSuggesterDatabase.COORDINATES + " = '" + trailhead.getCoordinates() + "', \n" +
                            HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE + " = '" + java.time.LocalDate.now() + "' \n" +
                            "WHERE " + HikeSuggesterDatabase.TRAILHEAD_URL + " = '" + trailhead.getUrl() + "';";

        }
    }

    static void updateAllRowsByUrl () throws Exception {

        ArrayList<Trailhead> trailheads = TrailheadScraper.createListOfTrailheads();

        trailheads.forEach((trailhead) -> {
            try {
                DatabaseConnection.createStatement().execute(mySqlSyntaxUpdateUrl(trailhead));
            } catch (Exception throwables) {
                throwables.printStackTrace();
            }
        });

    }

    static String mySqlSyntaxUpdateUrl (Trailhead trailhead) {

        if (trailhead == null) {
            return "SELECT * FROM " + HikeSuggesterDatabase.TRAILHEADS;
        } else {
            return
                    "UPDATE " + HikeSuggesterDatabase.TRAILHEADS + "\n" +
                            "SET " + HikeSuggesterDatabase.TRAILHEAD_URL + " = '" + trailhead.getUrl() + "' \n" +
                            "WHERE " + HikeSuggesterDatabase.TRAILHEAD_ID + " = " + trailhead.getTrailheadId() + ";";

        }
    }
}
