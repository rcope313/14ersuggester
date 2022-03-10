package subcommands;

import database.DatabaseConnection;
import database.query.CompareQuery;
import models.CliColumn;
import models.FourteenerRoute;
import models.GradeQuality;
import models.HikeSuggesterDatabase;
import picocli.CommandLine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

@CommandLine.Command(name = "compare", mixinStandardHelpOptions = true)
public class CompareSubCommand extends SubCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "First argument, mountain name")
    String mountainName1;
    @CommandLine.Parameters(index = "1", description = "First argument, route name")
    String routeName1;
    @CommandLine.Parameters(index = "2", description = "Second argument, mountain name")
    String mountainName2;
    @CommandLine.Parameters(index = "3", description = "Second argument, route name")
    String routeName2;
    @CommandLine.Option(names = {"-u", "--url"}, arity = "1..2",
            description = "Rather than entering in two separate Route and Mountain Names, enter two urls to compare.")
    public String[] routeUrls;

    @Override
    public void run() {
        CompareQuery compareQuery = setCompareQuery();
        viewCliTableCompareSubCommand(compareQuery);
    }

    private CompareQuery setCompareQuery() {
        CompareQuery compareQuery = new CompareQuery();
        compareQuery.setMountainName1(mountainName1);
        compareQuery.setRouteName1(routeName1);
        compareQuery.setMountainName2(mountainName2);
        compareQuery.setRouteName2(routeName2);
        compareQuery.setRouteUrls(convertArrayToArrayList(routeUrls));
        return compareQuery;
    }

    private void viewCliTableCompareSubCommand(CompareQuery compareQuery)  {
        ArrayList<CliColumn> cliColumnFields = designateCliColumnFieldsGeneral();
        String querySyntax = compareQuery.createQuerySyntax();

        ArrayList<FourteenerRoute> routeList = createFourteenerRoutesFromCliInput(querySyntax);
        String differenceString = compareQuery.createDifferenceString(routeList.get(0), routeList.get(1));

        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(querySyntax, cliColumnFields, differenceString);
    }

    private ArrayList<FourteenerRoute> createFourteenerRoutesFromCliInput(String query) {
        ArrayList<FourteenerRoute> fourteenerRoutes = new ArrayList<>();
        try (Statement stmt = DatabaseConnection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                FourteenerRoute currentRoute = new FourteenerRoute();
                GradeQuality gradeQuality = new GradeQuality();
                currentRoute.setFourteenerRouteId(rs.getInt(HikeSuggesterDatabase.FOURTEENER_ROUTE_ID));
                currentRoute.setRouteName(rs.getString(HikeSuggesterDatabase.ROUTE_NAME));
                currentRoute.setMountainName(rs.getString(HikeSuggesterDatabase.MOUNTAIN_NAME));
                currentRoute.setSnowRoute(convertIntToBoolean(rs.getInt(HikeSuggesterDatabase.IS_SNOW_ROUTE)));
                currentRoute.setStandardRoute(convertIntToBoolean(rs.getInt(HikeSuggesterDatabase.IS_STANDARD_ROUTE)));
                gradeQuality.setGrade(rs.getInt(HikeSuggesterDatabase.GRADE));
                gradeQuality.setQuality(rs.getString(HikeSuggesterDatabase.GRADE_QUALITY));
                currentRoute.setGradeQuality(gradeQuality);
                currentRoute.setStartElevation(rs.getInt(HikeSuggesterDatabase.START_ELEVATION));
                currentRoute.setSummitElevation(rs.getInt(HikeSuggesterDatabase.SUMMIT_ELEVATION));
                currentRoute.setTotalGain(rs.getInt(HikeSuggesterDatabase.TOTAL_GAIN));
                currentRoute.setRouteLength(rs.getDouble(HikeSuggesterDatabase.ROUTE_LENGTH));
                currentRoute.setExposure(rs.getString(HikeSuggesterDatabase.EXPOSURE));
                currentRoute.setRockfallPotential(rs.getString(HikeSuggesterDatabase.ROCKFALL_POTENTIAL));
                currentRoute.setRouteFinding(rs.getString(HikeSuggesterDatabase.ROUTE_FINDING));
                currentRoute.setCommitment(rs.getString(HikeSuggesterDatabase.COMMITMENT));
                currentRoute.setHasMultipleRoutes(convertIntToBoolean(rs.getInt(HikeSuggesterDatabase.HAS_MULTIPLE_ROUTES)));
                currentRoute.setUrl(rs.getString(HikeSuggesterDatabase.ROUTE_URL));
                currentRoute.setTrailhead(rs.getString(HikeSuggesterDatabase.ROUTE_TRAILHEAD));
                fourteenerRoutes.add(currentRoute);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fourteenerRoutes.size() > 2) {
            throw new IllegalStateException("Results yielded more than two Fourteener Routes (the two mountains entered share the " +
                    "same route names). Please enter 14er url of intended routes/mountains.");
        } if (fourteenerRoutes.size() < 2) {
            throw new IllegalStateException("Results yielded less than two Fourteener Routes. Please check the spelling of the Mountain Name and " +
                    "the Route Name.");
        }
        return fourteenerRoutes;
    }

    private void inputDataIntoCliTable(String querySyntax, ArrayList<CliColumn> cliColumnFields, String differenceString) {
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

    private static boolean convertIntToBoolean (int x) {
        if (x == 0) {
            return false;
        }
        if (x == 1) {
            return true;
        } else {
            throw new IllegalArgumentException("Value neither 0 or 1");
        }
    }
}