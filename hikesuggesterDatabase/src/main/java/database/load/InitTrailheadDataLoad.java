package database.load;

import models.HikeSuggesterDatabase;
import models.Trailhead;
import database.DatabaseConnection;
import utility.Utils;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
            trailhead.setName(Utils.insertApostrophe(trailhead.getName()));
        }

        if (trailhead.getRoadDescription().contains("'")) {
            trailhead.setRoadDescription(Utils.insertApostrophe(trailhead.getRoadDescription()));
        }

        if (trailhead.getWinterAccess().contains("'")) {
            trailhead.setWinterAccess(Utils.insertApostrophe(trailhead.getWinterAccess()));
        }

        return trailhead;
    }

}

