package database.dao;

import database.models.ImmutableFetchedTrailhead;
import database.models.ImmutableStoredTrailhead;
import database.models.HikeSuggesterDatabase;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TrailheadsDao extends Dao {

    public TrailheadsDao(Connection conn) {
        super(conn);
    }

    public void insert(ImmutableFetchedTrailhead trailhead) {
        try {
            conn.createStatement().execute(insertQuery(trailhead));
            System.out.println("ENTRY CREATED \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
        } catch (Exception e) {
            System.out.print("DUPLICATE ENTRY IGNORED  \n");
            System.out.print("Trailhead Name: " + trailhead.getName() + "\n");
        }
    }

    public ImmutableStoredTrailhead get(ImmutableFetchedTrailhead trailhead) {
        String getQuery = getQuery(trailhead);
        ImmutableStoredTrailhead storedTrailhead = null;
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(getQuery);
            while (rs.next()) {
                storedTrailhead = buildImmutableStoredTrailhead(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (storedTrailhead == null) {
            throw new IllegalStateException("Trailhead not in database.");
        }
        return storedTrailhead;
    }

    private static String insertQuery(ImmutableFetchedTrailhead trailhead) {
        return "INSERT INTO " +
                HikeSuggesterDatabase.getColumnNamesTrailheadsTable()  +
                " VALUES ('" +
                trailhead.getName() + "', '" +
                trailhead.getCoordinates() + "', " +
                trailhead.getRoadDifficulty() + ", '" +
                trailhead.getRoadDescription() + "', '" +
                trailhead.getWinterAccess() + "', '" +
                trailhead.getUrl() + "', '" +
                java.time.LocalDate.now() + "')";
    }

    private static String getQuery(ImmutableFetchedTrailhead trailhead) {
        return "SELECT *" +
                " FROM " + HikeSuggesterDatabase.TRAILHEADS +
                " WHERE " + HikeSuggesterDatabase.TRAILHEAD_URL + " = '" + trailhead.getUrl() + "' ";
    }

    private static ImmutableStoredTrailhead buildImmutableStoredTrailhead(ResultSet rs) throws SQLException {
        return ImmutableStoredTrailhead.builder()
                .id(rs.getInt(HikeSuggesterDatabase.TRAILHEAD_ID))
                .name(rs.getString(HikeSuggesterDatabase.TRAILHEAD_NAME))
                .coordinates(rs.getString(HikeSuggesterDatabase.COORDINATES))
                .roadDifficulty(rs.getInt(HikeSuggesterDatabase.ROAD_DIFFICULTY))
                .roadDescription(rs.getString(HikeSuggesterDatabase.ROAD_DESCRIPTION))
                .winterAccess(rs.getString(HikeSuggesterDatabase.WINTER_ACCESS))
                .url(rs.getString(HikeSuggesterDatabase.TRAILHEAD_URL))
                .updateDate(rs.getString(HikeSuggesterDatabase.TRAILHEAD_UPDATE_DATE))
                .build();
    }
}
