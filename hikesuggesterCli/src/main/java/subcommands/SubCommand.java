package subcommands;

import database.DatabaseConnection;
import database.query.CompareQuery;
import database.query.Query;
import database.query.SearchQuery;
import models.CliColumn;
import models.CliColumnDesign;
import models.FourteenerRoute;
import models.HikeSuggesterDatabase;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;

public abstract class SubCommand {

    public void viewCliTableCompareSubCommand (CompareQuery query)  {

        ArrayList<CliColumn> cliColumnFields = designateCliColumnFieldsGeneral();
        String querySyntax = query.createQuerySyntax();

        ArrayList<FourteenerRoute> routeList = query.createFourteenerRoutesFromCliInput(querySyntax);
        String differenceString = query.createDifferenceString(routeList.get(0), routeList.get(1));

        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(querySyntax, cliColumnFields, differenceString);

    }

    public void viewCliTableSearchSubCommand (SearchQuery query)  {

        ArrayList<CliColumn> cliColumnFields;

        if (query.isVerbose()) {
            cliColumnFields = designateCliColumnFieldsVerbose(query);
        } else {
            cliColumnFields = designateCliColumnFieldsGeneral();
        }

        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(query.createQuerySyntax(), cliColumnFields);

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

    public ArrayList<CliColumn> designateCliColumnFieldsVerbose(Query query) {
        ArrayList<CliColumn> cliColumnFields = new ArrayList<>();
        cliColumnFields.add(CliColumnDesign.MOUNTAIN_NAME);
        cliColumnFields.add(CliColumnDesign.ROUTE_NAME);

        if (query.isSnowRoute()) { cliColumnFields.add(CliColumnDesign.SNOW_ROUTE); }
        if (query.isStandardRoute()) { cliColumnFields.add(CliColumnDesign.STANDARD_ROUTE); }
        if (query.getGradeQualities() != null) { cliColumnFields.add(CliColumnDesign.GRADE_QUALITY); }
        if (query.getStartElevation() != 0) { cliColumnFields.add(CliColumnDesign.START_ELEVATION); }
        if (query.getSummitElevation() != 0) { cliColumnFields.add(CliColumnDesign.SUMMIT_ELEVATION); }
        if (query.getExposure() != null) { cliColumnFields.add(CliColumnDesign.EXPOSURE); }
        if (query.getRockfallPotential() != null) { cliColumnFields.add(CliColumnDesign.ROCKFALL_POTENTIAL); }
        if (query.getRouteFinding() != null) { cliColumnFields.add(CliColumnDesign.ROUTE_FINDING); }
        if (query.getCommitment() != null) { cliColumnFields.add(CliColumnDesign.COMMITMENT); }
        if (query.isHasMultipleRoutes()) { cliColumnFields.add(CliColumnDesign.MULTIPLE_ROUTES); }
        if (query.getTrailheads() != null) { cliColumnFields.add(CliColumnDesign.TRAILHEAD); }

        cliColumnFields.add(CliColumnDesign.GRADE);
        cliColumnFields.add(CliColumnDesign.TOTAL_GAIN);
        cliColumnFields.add(CliColumnDesign.ROUTE_LENGTH);
        cliColumnFields.add(CliColumnDesign.ROUTE_URL);


        return cliColumnFields;

    }

    private void buildCliTableHeaders (ArrayList<CliColumn> cliColumnFields) {

        ArrayList<String> cliTableHeaders = new ArrayList<>();
        cliColumnFields.forEach((cliColumn) -> cliTableHeaders.add(cliColumn.hikeSuggesterCliColumn));
        var cliTableHeadersArray = cliTableHeaders.toArray();

        System.out.format(buildCliHeaderFormatter(cliColumnFields), cliTableHeadersArray);


    }

    private static String buildCliHeaderFormatter (ArrayList<CliColumn> cliColumnFields) {
        StringJoiner formatHeaderStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatHeaderStringJoiner.add(cliColumn.formatString + "s"));
        formatHeaderStringJoiner.add("\n");

        return formatHeaderStringJoiner.toString();

    }

    private void inputDataIntoCliTable (String querySyntax, ArrayList<CliColumn> cliColumnFields, String differenceString) {

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

        System.out.print("\n" + "Differences" + "\n");
        System.out.print(differenceString + "\n" + "\n");



    }

    private void inputDataIntoCliTable (String querySyntax, ArrayList<CliColumn> cliColumnFields) {

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

    private static String buildCliDataFormatter (ArrayList<CliColumn> cliColumnFields) {
        StringJoiner formatDataStringJoiner = new StringJoiner("");
        cliColumnFields.forEach((cliColumn) -> formatDataStringJoiner.add(cliColumn.formatString + cliColumn.formatRegex));
        formatDataStringJoiner.add("\n");

        return formatDataStringJoiner.toString();

    }





}
