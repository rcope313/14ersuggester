package mysql.query;

import models.*;
import mysql.MySqlConnection;
import picocli.CommandLine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class MySqlQuery {


    public void viewCliTable(ArrayList<CliColumn> cliColumnFields, String querySyntax, String differenceString)  {
        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(querySyntax, cliColumnFields, differenceString);

    }

    public void viewCliTable(ArrayList<CliColumn> cliColumnFields, String querySyntax)  {
        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(querySyntax, cliColumnFields);

    }

    private void buildCliTableHeaders(ArrayList<CliColumn> cliColumnFields) {

        ArrayList<String> cliTableHeaders = new ArrayList<>();
        cliColumnFields.forEach((cliColumn) -> cliTableHeaders.add(cliColumn.hikeSuggesterCliColumn));
        var cliTableHeadersArray = cliTableHeaders.toArray();

        System.out.format(buildCliHeaderFormatter(cliColumnFields), cliTableHeadersArray);


    }

    private void inputDataIntoCliTable (String compareQueryMySqlSyntax, ArrayList<CliColumn> cliColumnFields, String differenceString) {

        try (Statement stmt = MySqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(compareQueryMySqlSyntax);

            while (rs.next()) {

                ArrayList<Object> columnDataList = new ArrayList<>();

                for (int idx = 0; idx < cliColumnFields.size(); idx ++) {
                    columnDataList.add(getResultSetValue(rs, cliColumnFields.get(idx)));

                }

                var columnDataArray = columnDataList.toArray();
                System.out.format(buildCliDataFormatter(cliColumnFields), columnDataArray);


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String differences = CommandLine.Help.Ansi.AUTO.string("@|bold,red,underline Differences|@");
        System.out.print("\n" + differences + "\n");
        System.out.print(differenceString + "\n" + "\n");



    }

    private void inputDataIntoCliTable (String compareQueryMySqlSyntax, ArrayList<CliColumn> cliColumnFields) {

        try (Statement stmt = MySqlConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(compareQueryMySqlSyntax);

            while (rs.next()) {

                ArrayList<Object> columnDataList = new ArrayList<>();

                for (int idx = 0; idx < cliColumnFields.size(); idx ++) {
                    columnDataList.add(getResultSetValue(rs, cliColumnFields.get(idx)));

                }

                var columnDataArray = columnDataList.toArray();
                System.out.format(buildCliDataFormatter(cliColumnFields), columnDataArray);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private static String buildCliDataFormatter(ArrayList<CliColumn> cliColumnFields) {
        StringJoiner formatDataStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatDataStringJoiner.add(cliColumn.formatString + cliColumn.formatRegex));
        formatDataStringJoiner.add("\n");

        return formatDataStringJoiner.toString();

    }

    private static String buildCliHeaderFormatter(ArrayList<CliColumn> cliColumnFields) {
        StringJoiner formatHeaderStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatHeaderStringJoiner.add(cliColumn.formatString + "s"));
        formatHeaderStringJoiner.add("\n");

        return formatHeaderStringJoiner.toString();

    }

    private static Object getResultSetValue (ResultSet rs, CliColumn cliColumn) throws SQLException {

        if (cliColumn.databaseColumn == null ||
            cliColumn.databaseColumn.equals(HikeSuggesterDatabase.ROUTE_UPDATE_DATE)) {
            return "";

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

    public ArrayList<CliColumn> designateCliColumnFieldsGeneral() {
        ArrayList<CliColumn> cliColumnFields = new ArrayList<>();
        cliColumnFields.add(CliColumnDesign.MOUNTAIN_NAME);
        cliColumnFields.add(CliColumnDesign.ROUTE_NAME);
        cliColumnFields.add(CliColumnDesign.GRADE);
        cliColumnFields.add(CliColumnDesign.TOTAL_GAIN);
        cliColumnFields.add(CliColumnDesign.ROUTE_LENGTH);
        cliColumnFields.add(CliColumnDesign.ROUTE_URL);


        return cliColumnFields;
    }































}
