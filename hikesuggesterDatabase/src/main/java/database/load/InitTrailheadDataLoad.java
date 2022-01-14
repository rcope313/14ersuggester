package database.load;

import models.HikeSuggesterDatabase;
import models.Trailhead;
import database.DatabaseConnection;
import java.sql.SQLException;
import java.util.ArrayList;


public class InitTrailheadDataLoad {


    private static void insertAllTrailheadsIntoTable (ArrayList<Trailhead> trailheads) {

        trailheads.forEach((trailhead) -> {
            try {
                insertATrailheadIntoTable(trailhead);
            } catch (SQLException throwables) {

            }
        });

    }

    private static void insertATrailheadIntoTable(Trailhead trailhead) throws SQLException {

        updateForSqlSyntax(trailhead);

        try {
            String sql = "INSERT INTO " + HikeSuggesterDatabase.TRAILHEADS + " VALUES (" +
                    trailhead.getTrailheadId() + ", " +
                    "'" + trailhead.getName() + "', " +
                    "'" + trailhead.getCoordinates() + "', " +
                    trailhead.getRoadDifficulty() + ", " +
                    "'" + trailhead.getRoadDescription() + "', " +
                    "'" + trailhead.getWinterAccess() + "', " +
                    "'" + trailhead.getUrl() + "'" +
                    ")";

            DatabaseConnection.createStatement().execute(sql);
            System.out.println("ENTRY CREATED \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
            System.out.print("Trailhead Id: " + trailhead.getTrailheadId() + "\n");

        } catch (Exception e) {
            System.out.print("DUPLICATE ENTRY IGNORED  \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
            System.out.print("Trailhead Id: " + trailhead.getTrailheadId() + "\n");

        }


    }

    private static Trailhead updateForSqlSyntax(Trailhead trailhead) {

        if (trailhead.getName().contains("'")) {
            trailhead.setName(LoadUtils.insertApostrophe(trailhead.getName()));
        }

        if (trailhead.getRoadDescription().contains("'")) {
            trailhead.setRoadDescription(LoadUtils.insertApostrophe(trailhead.getRoadDescription()));
        }

        if (trailhead.getWinterAccess().contains("'")) {
            trailhead.setWinterAccess(LoadUtils.insertApostrophe(trailhead.getWinterAccess()));
        }

        return trailhead;
    }

}

