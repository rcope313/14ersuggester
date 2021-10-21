package mysql;

import models.FourteenerRoute;
import webscraper.WebScraper;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;

public class GetConnection {
    static final String DB_URL = "jdbc:mysql://localhost/hike_suggester";
    static final String USER = "root";
    static final String PASS = "root1234";
    static final String TABLE_NAME = "fourteener_route";


    public static void main(String[] args) {
        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement())  {

            FourteenerRoute elb1 = new WebScraper().scrapeFourteener("/route.php?route=elbe1", new HashSet<>());



            // Execute a query
            System.out.println("Inserting records into the table" + TABLE_NAME +  "...");
            String sql = "INSERT INTO " + TABLE_NAME + "VALUES (" +
                    elb1.getRouteName() + ", " +
                    elb1.getMountainName() + ", " +
                    elb1.isSnowRouteOnly() + ", " +
                    elb1.isSnowRouteOnly() + ", " +


            ")";
            stmt.executeUpdate(sql);


            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

