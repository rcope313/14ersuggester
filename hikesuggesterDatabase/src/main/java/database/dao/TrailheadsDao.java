package database.dao;

import database.models.ImmutableStoredTrailhead;
import models.HikeSuggesterDatabase;

public class TrailheadsDao {

    public static void insert(ImmutableStoredTrailhead trailhead) {
        try {
            DatabaseConnection.createStatement().execute(insertQuery(trailhead));
            System.out.println("ENTRY CREATED \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
        } catch (Exception e) {
            System.out.print("DUPLICATE ENTRY IGNORED  \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
        }
    }

    private static String insertQuery(ImmutableStoredTrailhead trailhead) {
        return "INSERT INTO " + HikeSuggesterDatabase.TRAILHEADS + " VALUES ('" +
                trailhead.getName() + "', '" +
                trailhead.getCoordinates() + "', " +
                trailhead.getRoadDifficulty() + ", '" +
                trailhead.getRoadDescription() + "', '" +
                trailhead.getWinterAccess() + "', '" +
                trailhead.getUrl() + "')";
    }
}
