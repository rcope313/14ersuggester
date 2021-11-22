package mysql.query;

import models.*;
import mysql.MySqlConnection;
import mysql.update.UpdateFourteenerRoutes;
import mysql.update.UpdateTrailheads;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class MySqlQuery {



    public void weeklyTrailheadUpdate(String strDate, Trailhead trailhead) throws Exception {
        UpdateTrailheads.weeklyUpdate(strDate, trailhead.getUrl());
    }

    public void weeklyFourteenerRouteUpdate(String strDate, FourteenerRoute route) throws Exception {
        UpdateFourteenerRoutes.weeklyUpdate(strDate, route.getUrl());
    }



    public void viewCliTable(ArrayList<CliColumn> cliColumnFields, String querySyntax )  {
        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(querySyntax, cliColumnFields);

    }

    private void buildCliTableHeaders(ArrayList<CliColumn> cliColumnFields) {


        ArrayList<String> columnNamesList = new ArrayList<>();
        cliColumnFields.forEach((cliColumn) -> columnNamesList.add(cliColumn.hikeSuggesterCliColumn));
        var columnNamesArray = columnNamesList.toArray();

        StringJoiner formatHeaderStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatHeaderStringJoiner.add(cliColumn.formatString + "s"));
        formatHeaderStringJoiner.add("\n");

        System.out.format(formatHeaderStringJoiner.toString(), columnNamesArray);


    }

    private void inputDataIntoCliTable (String compareQueryMySqlSyntax, ArrayList<CliColumn> cliColumnFields) {

        StringJoiner formatDataStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatDataStringJoiner.add(cliColumn.formatString + cliColumn.formatRegex));
        formatDataStringJoiner.add("\n");

        try (Statement stmt = MySqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(compareQueryMySqlSyntax);

            while (rs.next()) {
                ArrayList<Object> columnDataList = new ArrayList<>();
                cliColumnFields.forEach((cliColumn) -> {
                    try {
                        columnDataList.add(getResultSetValue(rs, cliColumn));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });


                var columnDataArray = columnDataList.toArray();
                System.out.format(formatDataStringJoiner.toString(), columnDataArray);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getResultSetValue (ResultSet rs, CliColumn cliColumn) throws SQLException {

        if (cliColumn.databaseColumn.equals(HikeSuggesterDatabase.ROUTE_UPDATE_DATE)) {

        }


        if (cliColumn.formatRegex.equals("s")) {
            return rs.getString(cliColumn.databaseColumn);
        }

        else if (cliColumn.databaseColumn.equals(HikeSuggesterDatabase.IS_SNOW_ROUTE) ||
                cliColumn.databaseColumn.equals(HikeSuggesterDatabase.IS_STANDARD_ROUTE) ||
                cliColumn.databaseColumn.equals(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES)) {

            return rs.getInt(cliColumn.databaseColumn);

        } else if (cliColumn.formatRegex.equals("d")) {
            return rs.getInt(cliColumn.databaseColumn);

        }

        else if (cliColumn.formatRegex.equals("f")) {
            return rs.getDouble(cliColumn.databaseColumn);

        } else {
            throw new IllegalArgumentException("Unsupported column type.");

        }


    }



    public ArrayList<CliColumn> designateCliColumnFieldsNonVerbose() {
        ArrayList<CliColumn> cliColumnFields = new ArrayList<>();
        cliColumnFields.add(CliColumnDesign.MOUNTAIN_NAME);
        cliColumnFields.add(CliColumnDesign.ROUTE_NAME);
        cliColumnFields.add(CliColumnDesign.GRADE);
        cliColumnFields.add(CliColumnDesign.TOTAL_GAIN);
        cliColumnFields.add(CliColumnDesign.ROUTE_LENGTH);
        cliColumnFields.add(CliColumnDesign.ROUTE_URL);


        return cliColumnFields;
    }

    public ArrayList<CliColumn> designateCliColumnFieldsVerbose() {
        ArrayList<CliColumn> cliColumnFields = new ArrayList<>();
        cliColumnFields.add(CliColumnDesign.MOUNTAIN_NAME);
        cliColumnFields.add(CliColumnDesign.ROUTE_NAME);
        cliColumnFields.add(CliColumnDesign.SNOW_ROUTE);
        cliColumnFields.add(CliColumnDesign.STANDARD_ROUTE);
        cliColumnFields.add(CliColumnDesign.GRADE);
        cliColumnFields.add(CliColumnDesign.GRADE_QUALITY);
        cliColumnFields.add(CliColumnDesign.START_ELEVATION);
        cliColumnFields.add(CliColumnDesign.SUMMIT_ELEVATION);
        cliColumnFields.add(CliColumnDesign.TOTAL_GAIN);
        cliColumnFields.add(CliColumnDesign.ROUTE_LENGTH);
        cliColumnFields.add(CliColumnDesign.EXPOSURE);
        cliColumnFields.add(CliColumnDesign.ROCKFALL_POTENTIAL);
        cliColumnFields.add(CliColumnDesign.ROUTE_FINDING);
        cliColumnFields.add(CliColumnDesign.COMMITMENT);
        cliColumnFields.add(CliColumnDesign.MULTIPLE_ROUTES);
        cliColumnFields.add(CliColumnDesign.TRAILHEAD);
        cliColumnFields.add(CliColumnDesign.ROUTE_URL);


        return cliColumnFields;

    }





























}
