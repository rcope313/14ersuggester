package database.dao;

import models.HikeSuggesterDatabase;
import models.Trailhead;
import webscraper.TrailheadScraper;
import java.sql.SQLException;
import java.util.ArrayList;

public class InitTrailheadDataLoad {

    private static void insertAllTrailheadsIntoTable () throws Exception {
        ArrayList<Trailhead> trailheads = TrailheadScraper.createListOfTrailheads();
        trailheads.forEach((trailhead) -> {
            try {
                insertATrailheadIntoTable(trailhead);
            } catch (SQLException ignored) {
            }
        });
    }

    private static void insertATrailheadIntoTable(Trailhead trailhead) throws SQLException {
        updateTrailheadWithCorrectSqlSyntax(trailhead);
        try {
            DatabaseConnection.createStatement().execute(buildSqlQuery(trailhead));
            System.out.println("ENTRY CREATED \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
            System.out.print("Trailhead Id: " + trailhead.getTrailheadId() + "\n");

        } catch (Exception e) {
            System.out.print("DUPLICATE ENTRY IGNORED  \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
            System.out.print("Trailhead Id: " + trailhead.getTrailheadId() + "\n");
        }
    }

    private static String buildSqlQuery(Trailhead trailhead) {
        return "INSERT INTO " + HikeSuggesterDatabase.TRAILHEADS + " VALUES (" +
                trailhead.getTrailheadId() + ", " +
                "'" + trailhead.getName() + "', " +
                "'" + trailhead.getCoordinates() + "', " +
                trailhead.getRoadDifficulty() + ", " +
                "'" + trailhead.getRoadDescription() + "', " +
                "'" + trailhead.getWinterAccess() + "', " +
                "'" + trailhead.getUrl() + "'" +
                ")";
    }

    private static void updateTrailheadWithCorrectSqlSyntax(Trailhead trailhead) {
        if (trailhead.getName().contains("'")) {
            trailhead.setName(LoadUtils.insertApostrophe(trailhead.getName()));
        }
        if (trailhead.getRoadDescription().contains("'")) {
            trailhead.setRoadDescription(LoadUtils.insertApostrophe(trailhead.getRoadDescription()));
        }
        if (trailhead.getWinterAccess().contains("'")) {
            trailhead.setWinterAccess(LoadUtils.insertApostrophe(trailhead.getWinterAccess()));
        }
    }
}
