package dao;

import database.models.HikeSuggesterDatabase;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FourteenerRoutesDaoTest {
    Connection con = DriverManager.getConnection("jdbc:h2:~/" + HikeSuggesterDatabase.HIKE_SUGGESTER);

    public FourteenerRoutesDaoTest() throws SQLException {
    }

    @Test
    public void itBuildsGetQuery() {

    }

    @Test
    public void itBuildsCompareQuery() {

    }

    @Test
    public void itBuildsSearchQuery() {

    }
}
