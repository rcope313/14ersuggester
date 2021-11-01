package mysql;

import models.FourteenerRoute;
import webscraper.FourteenerRouteScraper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;

public class GetConnection {
    static final String DB_URL = "jdbc:mysql://localhost/hike_suggester";
    static final String USER = "root";
    static final String PASS = "root1234";
    static final String TABLE_NAME = "fourteener_route";


    public static void main(String[] args) throws SQLException, IOException {

    }

    public void addFourteenerToDatabase (FourteenerRoute route) {
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement())  {

            System.out.println("Inserting records into the table" + TABLE_NAME +  "...");
            String sql = "INSERT INTO " + TABLE_NAME + " VALUES (" +
                    "'" + route.getRouteName() + "', " +
                    "'" + route.getMountainName() + "', " +
                    route.isSnowRouteOnly() + ", " +
                    route.isStandardRoute() + ", " +
                    route.getGradeQuality().getGrade() + ", " +
                    "'" + route.getGradeQuality().getQuality() + "', " +
                    "'" + route.getTrailhead() + "', " +
                    route.getStartElevation() + ", " +
                    route.getSummitElevation() + ", " +
                    route.getTotalGain() + ", " +
                    "'" + route.getRouteLength() + "', " +
                    "'" + route.getExposure() + "', " +
                    "'" + route.getRockfallPotential() + "', " +
                    "'" + route.getRouteFinding() + "', " +
                    "'" + route.getCommitment() + "', " +
                    route.isMultipleRoutes() + ", " +
                    "'" + route.getUrl() + "'" +
                    ")";

            stmt.executeUpdate(sql);
            System.out.println("Created record in given database...");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

