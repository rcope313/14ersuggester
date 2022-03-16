package subcommands;

import database.dao.Dao;
import database.models.AbstractCompareQuery;
import models.CliColumn;
import models.FourteenerRoute;
import models.GradeQuality;
import models.HikeSuggesterDatabase;
import picocli.CommandLine;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringJoiner;

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
        AbstractCompareQuery compareQuery = setCompareQuery();
        viewCliTableCompareSubCommand(compareQuery);
    }

    private AbstractCompareQuery setCompareQuery() {
//        AbstractCompareQuery compareQuery = new AbstractCompareQuery();
//        compareQuery.setMountainName1(mountainName1);
//        compareQuery.setRouteName1(routeName1);
//        compareQuery.setMountainName2(mountainName2);
//        compareQuery.setRouteName2(routeName2);
//        compareQuery.setRouteUrls(convertArrayToArrayList(routeUrls));
        return null;
    }

    private void viewCliTableCompareSubCommand(AbstractCompareQuery compareQuery)  {
//        ArrayList<CliColumn> cliColumnFields = designateCliColumnFieldsGeneral();
//        String querySyntax = compareQuery.createQuerySyntax();
//
//        ArrayList<FourteenerRoute> routeList = createFourteenerRoutesFromCliInput(querySyntax);
//        String differenceString = compareQuery.createDifferenceString(routeList.get(0), routeList.get(1));
//
//        buildCliTableHeaders(cliColumnFields);
//        inputDataIntoCliTable(querySyntax, cliColumnFields, differenceString);
    }

    private ArrayList<FourteenerRoute> createFourteenerRoutesFromCliInput(String query) {
        ArrayList<FourteenerRoute> fourteenerRoutes = new ArrayList<>();
        try (Statement stmt = Dao.createStatement()) {
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
        try (Statement stmt = Dao.createStatement()) {
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

    public String createDifferenceString (FourteenerRoute route0, FourteenerRoute route1) {
        StringJoiner differencesStringJoiner = new StringJoiner("\n");

        snowRouteDiff(differencesStringJoiner, route0, route1);
        standardRouteDiff(differencesStringJoiner, route0, route1);
        gradeDiff(differencesStringJoiner, route0, route1);
        gradeQualityDiff(differencesStringJoiner, route0, route1);
        startElevationDiff(differencesStringJoiner, route0, route1);
        summitElevationDiff(differencesStringJoiner, route0, route1);
        totalGainDiff(differencesStringJoiner, route0, route1);
        routeLengthDiff(differencesStringJoiner, route0, route1);
        exposureDiff(differencesStringJoiner, route0, route1);
        rockfallPotentialDiff(differencesStringJoiner, route0, route1);
        routeFindingDiff(differencesStringJoiner, route0, route1);
        commitmentDiff(differencesStringJoiner, route0, route1);
        hasMultipleRoutesDiff(differencesStringJoiner, route0, route1);
        trailheadDiff(differencesStringJoiner, route0, route1);

        return differencesStringJoiner.toString();
    }

    private void snowRouteDiff (StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if  (route0.isSnowRoute() != route1.isSnowRoute()) {
            differencesStringJoiner.add("Snow Route: " + route0.isSnowRoute() + ", " + route1.isSnowRoute() + "\n");
        }
    }

    private void standardRouteDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.isStandardRoute() != route1.isStandardRoute()) {
            differencesStringJoiner.add("Standard Route: " + route0.isStandardRoute() + ", " + route1.isStandardRoute());
        }
    }

    private void gradeDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getGradeQuality().getGrade() != route1.getGradeQuality().getGrade()) {
            differencesStringJoiner.add("Grade: " + route0.getGradeQuality().getGrade() + ", " + route1.getGradeQuality().getGrade());
        }
    }

    private void gradeQualityDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getGradeQuality().getQuality().equals(route1.getGradeQuality().getQuality())) {
            differencesStringJoiner.add("Grade Quality: " + route0.getGradeQuality().getQuality() + ", " + route1.getGradeQuality().getQuality());
        }
    }

    private void startElevationDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getStartElevation() != route1.getStartElevation()) {
            differencesStringJoiner.add("Start Elevation: " + route0.getStartElevation() + ", " + route1.getStartElevation());
        }
    }

    private void summitElevationDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getSummitElevation() != route1.getSummitElevation()) {
            differencesStringJoiner.add("Summit Elevation: " + route0.getSummitElevation() + ", " + route1.getSummitElevation());
        }
    }

    private void totalGainDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getTotalGain() != route1.getTotalGain()) {
            differencesStringJoiner.add("Total Gain: " + route0.getTotalGain() + ", " + route1.getTotalGain());
        }
    }

    private void routeLengthDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.getRouteLength() != route1.getRouteLength()) {
            differencesStringJoiner.add("Route Length: " + route0.getRouteLength() + ", " + route1.getRouteLength());
        }
    }

    private void exposureDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getExposure().equals(route1.getExposure())) {
            differencesStringJoiner.add("Exposure: " + route0.getExposure() + ", " + route1.getExposure());
        }
    }

    private void rockfallPotentialDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getRockfallPotential().equals(route1.getRockfallPotential())) {
            differencesStringJoiner.add("Rockfall Potential: " + route0.getRockfallPotential() + ", " + route1.getRockfallPotential());
        }
    }

    private void routeFindingDiff (StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getRouteFinding().equals(route1.getRouteFinding())) {
            differencesStringJoiner.add("Route Finding: " + route0.getRouteFinding() + ", " + route1.getRouteFinding());
        }
    }

    private void commitmentDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getCommitment().equals(route1.getCommitment())) {
            differencesStringJoiner.add("Commitment: " + route0.getCommitment() + ", " + route1.getCommitment());
        }
    }

    private void hasMultipleRoutesDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (route0.hasMultipleRoutes() != route1.hasMultipleRoutes()) {
            differencesStringJoiner.add("Multiple Routes: " + route0.hasMultipleRoutes() + ", " + route1.hasMultipleRoutes());
        }
    }

    private void trailheadDiff(StringJoiner differencesStringJoiner, FourteenerRoute route0, FourteenerRoute route1) {
        if (!route0.getTrailhead().equals(route1.getTrailhead())) {
            differencesStringJoiner.add("Trailhead: " + route0.getTrailhead()+ ", \n" + route1.getTrailhead());
        }
    }

    private void checkFieldsOfMySqlCompareQuery () {
//        if (getMountainName1() != null &&
//                getRouteName1() != null &&
//                getMountainName2() != null &&
//                getRouteName2() != null &&
//                getRouteUrls() != null) {
//
//            throw new IllegalStateException("Option fields cannot be entered alongside parameter fields");
//        }
    }
}