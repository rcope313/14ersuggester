package database.load;

import models.FourteenerRoute;
import models.HikeSuggesterDatabase;
import database.DatabaseConnection;
import webscraper.FourteenerRouteScraper;

import java.sql.SQLException;
import java.util.ArrayList;

public class InitFourteenerRouteDataLoad {

    private static void insertAllFourteenerRoutesIntoTable() throws Exception {
        ArrayList<FourteenerRoute> fourteenerRoutes = FourteenerRouteScraper.createListOfFourteenerRoutes();
        fourteenerRoutes.forEach((route) -> {
            try {
                insertFourteenerRouteIntoTable(route);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private static void insertFourteenerRouteIntoTable(FourteenerRoute route) throws SQLException {
        if (route != null) {
            updateFourteenerRouteWithCorrectSqlSyntax(route);
            try {
                DatabaseConnection.createStatement().execute(buildSqlQuery(route));
                System.out.println("ENTRY CREATED \n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");
                System.out.print("Fourteener Route Id: " + route.getFourteenerRouteId() + "\n");

            } catch (Exception e) {
                System.out.print("DUPLICATE ENTRY IGNORED \n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");
                System.out.print("Fourteener Route Id: " + route.getFourteenerRouteId() + "\n");
            }
        }
    }

    private static String buildSqlQuery(FourteenerRoute route) {
        return "INSERT INTO " + HikeSuggesterDatabase.FOURTEENER_ROUTES + " VALUES (" +
                route.getFourteenerRouteId() + ", " +
                "'" + route.getRouteName() + "', " +
                "'" + route.getMountainName() + "', " +
                route.isSnowRoute() + ", " +
                route.isStandardRoute() + ", " +
                route.getGradeQuality().getGrade() + ", " +
                "'" + route.getGradeQuality().getQuality() + "', " +
                route.getStartElevation() + ", " +
                route.getSummitElevation() + ", " +
                route.getTotalGain() + ", " +
                route.getRouteLength() + ", " +
                "'" + route.getExposure() + "', " +
                "'" + route.getRockfallPotential() + "', " +
                "'" + route.getRouteFinding() + "', " +
                "'" + route.getCommitment() + "', " +
                route.hasMultipleRoutes() + ", " +
                "'" + route.getUrl() + "', " +
                "'" + route.getTrailhead() + "'" +
                ")";
    }

    private static void updateFourteenerRouteWithCorrectSqlSyntax(FourteenerRoute route) {
        if (route.getMountainName().contains("'")) {
            route.setMountainName(LoadUtils.insertApostrophe(route.getMountainName()));
        }
        if (route.getRouteName().contains("'")) {
            route.setRouteName(LoadUtils.insertApostrophe(route.getRouteName()));
        }
        if (route.getTrailhead().contains("'")) {
            route.setTrailhead(LoadUtils.insertApostrophe(route.getTrailhead()));
        }
    }
}
