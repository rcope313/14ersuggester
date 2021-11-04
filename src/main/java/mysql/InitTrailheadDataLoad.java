package mysql;

import models.MySqlStatement;
import models.Trailhead;
import utility.Utils;
import webscraper.TrailheadScraper;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;


public class InitTrailheadDataLoad {

    static final String TABLE = "hike_suggester.trailheads";

    public static void main(String[] args) throws Exception {

        ArrayList<Trailhead> trailheads = TrailheadScraper.createListOfTrailheads();
        insertAllTrailheadsIntoTable(trailheads);



    }

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
            String sql = "INSERT INTO " + TABLE + " VALUES (" +
                    trailhead.getTrailheadId() + ", " +
                    "'" + trailhead.getName() + "', " +
                    "'" + trailhead.getCoordinates() + "', " +
                    trailhead.getRoadDifficulty() + ", " +
                    "'" + trailhead.getRoadDescription() + "', " +
                    "'" + trailhead.getWinterAccess() + "', " +
                    "'" + trailhead.getUrl() + "'" +
                    ")";

            MySqlStatement.createStatement().execute(sql);
            System.out.println("ENTRY CREATED \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
            System.out.print("Trailhead Id: " + trailhead.getTrailheadId() + "\n");

        } catch (SQLIntegrityConstraintViolationException e) {
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

