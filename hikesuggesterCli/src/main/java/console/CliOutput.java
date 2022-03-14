package console;

import database.DatabaseConnection;
import database.query.Query;
import database.query.SearchQuery;
import models.CliColumn;
import models.CliColumnDesign;
import models.HikeSuggesterDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public abstract class CliOutput {

    public abstract void buildCliTable(Query query);

    ArrayList<CliColumn> designateCliColumnFields() {
        ArrayList<CliColumn> cliColumnFields = new ArrayList<>();
        cliColumnFields.add(CliColumnDesign.MOUNTAIN_NAME);
        cliColumnFields.add(CliColumnDesign.ROUTE_NAME);
        cliColumnFields.add(CliColumnDesign.GRADE);
        cliColumnFields.add(CliColumnDesign.TOTAL_GAIN);
        cliColumnFields.add(CliColumnDesign.ROUTE_LENGTH);
        cliColumnFields.add(CliColumnDesign.ROUTE_URL);
        return cliColumnFields;
    }

    void buildCliTableHeaders(ArrayList<CliColumn> cliColumnFields) {
        ArrayList<String> cliTableHeaders = new ArrayList<>();
        cliColumnFields.forEach((cliColumn) -> cliTableHeaders.add(cliColumn.getHikeSuggesterCliColumn()));
        var cliTableHeadersArray = cliTableHeaders.toArray();
        System.out.format(buildCliHeaderFormatter(cliColumnFields), cliTableHeadersArray);
    }

    private static String buildCliHeaderFormatter(ArrayList<CliColumn> cliColumnFields) {
        StringJoiner formatHeaderStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatHeaderStringJoiner.add(cliColumn.getFormatString() + "s"));
        formatHeaderStringJoiner.add("\n");
        return formatHeaderStringJoiner.toString();
    }

    void inputDataIntoCliTable(String querySyntax, ArrayList<CliColumn> cliColumnFields) {
        try (Statement stmt = DatabaseConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(querySyntax);
            while (rs.next()) {
                ArrayList<Object> columnDataList = new ArrayList<>();
                for (CliColumn cliColumnField : cliColumnFields) {
                    columnDataList.add(getResultSetValue(rs, cliColumnField));
                }
                var columnDataArray = columnDataList.toArray();
                System.out.format(buildCliDataFormatter(cliColumnFields), columnDataArray);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Object getResultSetValue(ResultSet rs, CliColumn cliColumn) throws SQLException {
        if (cliColumn.getDatabaseColumn() == null ||
                cliColumn.getDatabaseColumn().equals(HikeSuggesterDatabase.ROUTE_UPDATE_DATE)) {
            return "";
        }
        if (cliColumn.getFormatRegex().equals("s")) {
            return rs.getString(cliColumn.getDatabaseColumn());
        }
        else if (cliColumn.getDatabaseColumn().equals(HikeSuggesterDatabase.IS_SNOW_ROUTE) ||
                cliColumn.getDatabaseColumn().equals(HikeSuggesterDatabase.IS_STANDARD_ROUTE) ||
                cliColumn.getDatabaseColumn().equals(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES)) {
            return rs.getInt(cliColumn.getDatabaseColumn());
        } else if (cliColumn.getFormatRegex().equals("d")) {
            return rs.getInt(cliColumn.getDatabaseColumn());
        }
        else if (cliColumn.getFormatRegex().equals("f")) {
            return rs.getDouble(cliColumn.getDatabaseColumn());
        } else {
            throw new IllegalArgumentException("Unsupported column type.");
        }
    }

    static String buildCliDataFormatter(ArrayList<CliColumn> cliColumnFields) {
        StringJoiner formatDataStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatDataStringJoiner.add(cliColumn.getFormatString() + cliColumn.getFormatRegex()));
        formatDataStringJoiner.add("\n");
        return formatDataStringJoiner.toString();
    }

    ArrayList<Integer> convertArrayToArrayList (Integer[] array) {
        if (array == null) {
            return null;
        } else {
            return new ArrayList<>(Arrays.asList(array));
        }
    }

    ArrayList<String> convertArrayToArrayList (String[] array) {
        if (array == null) {
            return null;
        } else {
            return new ArrayList<>(Arrays.asList(array));
        }
    }
}
