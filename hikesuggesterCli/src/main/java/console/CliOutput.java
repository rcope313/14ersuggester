package console;

import database.dao.DatabaseConnection;
import models.Column;
import models.HikeSuggesterDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public abstract class CliOutput {

//    public abstract void buildCliTable(Query query);

    ArrayList<Column> designateCliColumnFields() {
        ArrayList<Column> columnList = new ArrayList<>();
        columnList.add(Column.MOUNTAIN_NAME);
        columnList.add(Column.ROUTE_NAME);
        columnList.add(Column.GRADE);
        columnList.add(Column.TOTAL_GAIN);
        columnList.add(Column.ROUTE_LENGTH);
        columnList.add(Column.ROUTE_URL);
        return columnList;
    }

    void buildCliTableHeaders(ArrayList<Column> columnList) {
        ArrayList<String> cliTableHeaders = new ArrayList<>();
        columnList.forEach((column) -> cliTableHeaders.add(column.getCliColumn()));
        Object[] cliTableHeadersArray = cliTableHeaders.toArray();
        System.out.format(buildCliHeaderFormatter(columnList), cliTableHeadersArray);
    }

    private static String buildCliHeaderFormatter(ArrayList<Column> columnList) {
        StringJoiner formatHeaderStringJoiner = new StringJoiner("");
        columnList.forEach((column) -> formatHeaderStringJoiner.add(column.getFormatString() + "s"));
        formatHeaderStringJoiner.add("\n");
        return formatHeaderStringJoiner.toString();
    }

    void inputDataIntoCliTable(String querySyntax, ArrayList<Column> columnList) {
        try (Statement stmt = DatabaseConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(querySyntax);
            while (rs.next()) {
                ArrayList<Object> columnDataList = new ArrayList<>();
                for (Column column : columnList) {
                    columnDataList.add(getResultSetValue(rs, column));
                }
                var columnDataArray = columnDataList.toArray();
                System.out.format(buildCliDataFormatter(columnList), columnDataArray);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Object getResultSetValue(ResultSet rs, Column column) throws SQLException {
        if (column.getDatabaseColumn() == null ||
            column.getDatabaseColumn().equals(HikeSuggesterDatabase.ROUTE_UPDATE_DATE)) {
            return "";
        }
        if (column.getFormatRegex().equals("s")) {
            return rs.getString(column.getDatabaseColumn());
        }
        else if (column.getDatabaseColumn().equals(HikeSuggesterDatabase.IS_SNOW_ROUTE) ||
                 column.getDatabaseColumn().equals(HikeSuggesterDatabase.IS_STANDARD_ROUTE) ||
                 column.getDatabaseColumn().equals(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES)) {
            return rs.getInt(column.getDatabaseColumn());
        } else if (column.getFormatRegex().equals("d")) {
            return rs.getInt(column.getDatabaseColumn());
        }
        else if (column.getFormatRegex().equals("f")) {
            return rs.getDouble(column.getDatabaseColumn());
        } else {
            throw new IllegalArgumentException("Unsupported column type.");
        }
    }

    static String buildCliDataFormatter(ArrayList<Column> columnList) {
        StringJoiner formatDataStringJoiner = new StringJoiner("");
        columnList.forEach((column) -> formatDataStringJoiner.add(column.getFormatString() + column.getFormatRegex()));
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
