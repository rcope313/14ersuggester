package mysql;

import models.FourteenerRoute;
import utility.Utils;
import webscraper.FourteenerRouteScraper;
import java.sql.*;
import java.util.ArrayList;



public class InitFourteenerRotueDataLoad {
    static final String TABLE = "fourteener_routes";

    public static void main(String[] args) throws Exception {

        ArrayList<FourteenerRoute> routes = FourteenerRouteScraper.createListOfFourteenerRoutes();
        insertAllFourteenerRoutesIntoTable(routes);

        System.out.print("Nice work!");


    }

    private static void insertAllFourteenerRoutesIntoTable(ArrayList<FourteenerRoute> fourteenerRoutes) {

        fourteenerRoutes.forEach((route) -> {
            try {
                insertAFourteenerRouteIntoTable(route);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    private static void insertAFourteenerRouteIntoTable(FourteenerRoute route) throws SQLException {

        if (route == null) {

        } else {

            updateForSqlSyntax(route);

            try {
                String sql = "INSERT INTO " + TABLE + " VALUES (" +
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

                MySqlConnection.createStatement().execute(sql);
                System.out.println("ENTRY CREATED \n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");
                System.out.print("Fourteener Route Id: " + route.getFourteenerRouteId() + "\n");

            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.print("DUPLICATE ENTRY IGNORED \n");
                System.out.print("Route Name: " + route.getRouteName() + "\n");
                System.out.print("Fourteener Route Id: " + route.getFourteenerRouteId() + "\n");

            }
        }
    }

    private static FourteenerRoute updateForSqlSyntax (FourteenerRoute route) {

        if (route.getMountainName().contains("'")) {
            route.setMountainName(Utils.insertApostrophe(route.getMountainName()));
        }
        if (route.getRouteName().contains("'")) {
            route.setRouteName(Utils.insertApostrophe(route.getRouteName()));
        }
        if (route.getTrailhead().contains("'")) {
            route.setTrailhead(Utils.insertApostrophe(route.getTrailhead()));
        }

        return route;

    }

}


