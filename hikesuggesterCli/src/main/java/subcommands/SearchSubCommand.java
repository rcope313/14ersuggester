package subcommands;

import database.DatabaseConnection;
import database.query.SearchQuery;
import models.CliColumn;
import models.CliColumnDesign;
import picocli.CommandLine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

@CommandLine.Command(name = "search", mixinStandardHelpOptions = true)
public class SearchSubCommand extends SubCommand implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @CommandLine.Option(names = {"-v", "-verbose"},
            description = "Verbose. Result output will yield all table columns, rather than just Mountain Name, Route Name, and URL.")
    public boolean verbose = false;
    @CommandLine.Option(names = {"-q", "--query"}, interactive = true,
            description = "Write MySql query. Begin with WHERE statement.")
    public String optionQuery;
    @CommandLine.Option(names = {"--m", "-mountainnames"}, split = ",",
            description = "Search by name of mountain. If searching by more than one mountain name, split list with ','.")
    public String[] mountainNames;
    @CommandLine.Option(names = {"--r", "-routenames"}, split = ", ",
            description = "Search by name of route. If searching by more than one route name, split list with ','.")
    public String[] routeNames;
    @CommandLine.Option(names = {"--st", "-standardroute"},
            description = "Search if route is a standard route only.")
    public boolean isStandardRoute = false;
    @CommandLine.Option(names = {"--sn", "-snowroute"},
            description = "Search if route is a snow route only.")
    public boolean isSnowRoute = false;
    @CommandLine.Option(names = {"--g", "-grades"}, split = ",",
            description = "Search by grade of route. If searching by more than one grade, split list with ','.")
    public Integer[] grades;
    @CommandLine.Option(names = {"--gq", "-gradeQualities"}, split = ", ",
            description = "Search by grade qualities of route. Valid options include 'easy', 'difficult', and ''. " +
                    "If searching by more than one grade, split list with ','.")
    public String[] gradeQualities;
    @CommandLine.Option(names = {"--t", "-trailheads"}, split = ", ",
            description = "Search by trailhead name. If searching by more than one trailhead, split list with ','.")
    public String[] trailheads;
    @CommandLine.Option(names = {"--ste", "-startelevation"},
            description = "Search by start elevation. Result will yield all results greater than or equal to input.")
    public int startElevation;
    @CommandLine.Option(names = {"--sue", "-summitelevation"},
            description = "Search by summit elevation. Result will yield all results greater than or equal to input.")
    public int summitElevation;
    @CommandLine.Option(names = {"--tg", "-totalgain"},
            description = "Search by total gain. Result will yield all results greater than or equal to input.")
    public int totalGain;
    @CommandLine.Option(names = {"--rl", "-routelength"},
            description = "Search by length of route. Result will yield all results greater than or equal to input.")
    public double routeLength;
    @CommandLine.Option(names = {"--ex", "-exposure"},
            description = "Search by exposure. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public String exposure;
    @CommandLine.Option(names = {"--rp", "-rockfallpotential"},
            description = "Search by rockfall potential. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public String rockfallPotential;
    @CommandLine.Option(names = {"--rf", "-routefinding"},
            description = "Search by route finding. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public String routeFinding;
    @CommandLine.Option(names = {"--ct", "-commitment"},
            description = "Search by commitment. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public String commitment;
    @CommandLine.Option(names = {"--mr", "-mulitpleroutes"},
            description = "Search if there are multiple routes of varied elevation gain and route length. " +
                    "Meaning, the approach road may have winter closures or such that the road difficulty significantly increases. " +
                    "This will be noted as a '0' in the route length and total gain columns.")
    public boolean hasMultipleRoutes = false;
    @CommandLine.Option(names = {"--ru", "-routeurls"}, split = ", ",
            description = "Search by route url. If searching by more than one url, split list with ','.")
    public String[] routeUrls;
    @CommandLine.Option(names = {"--tc", "-coordinates"},
            description = "Search by trailhead coordinates. Example syntax: '39.60866, -105.79999'")
    public String[] trailheadCoordinates;
    @CommandLine.Option(names = {"--rdi", "-roaddifficulties"},
            description = "Search by road difficulty. Road difficulty ranges from 0 through 6.")
    public Integer[] roadDifficulties;
    @CommandLine.Option(names = {"--tu", "-trailheadurls"}, split = ", ",
            description = "Search by trailhead url. If searching by more than one url, split list with ','.")
    public String[] trailheadUrls;

    @Override
    public void run() {
//        spec.commandLine().getOut().println(setSearchQuery().createQuerySyntax());
//        ** use to create tests
        SearchQuery searchQuery = setSearchQuery();
        viewCliTableSearchSubCommand(searchQuery);
    }
    
    private void viewCliTableSearchSubCommand(SearchQuery query)  {
        ArrayList<CliColumn> cliColumnFields;
        if (query.isVerbose()) {
            cliColumnFields = designateCliColumnFieldsVerbose(query);
        } else {
            cliColumnFields = designateCliColumnFieldsGeneral();
        }
        buildCliTableHeaders(cliColumnFields);
        inputDataIntoCliTable(query.createQuerySyntax(), cliColumnFields);
    }

    private ArrayList<CliColumn> designateCliColumnFieldsVerbose(SearchQuery query) {
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

    private void inputDataIntoCliTable(String querySyntax, ArrayList<CliColumn> cliColumnFields) {
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
    
    private SearchQuery setSearchQuery() {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setVerbose(verbose);
        searchQuery.setQuery(optionQuery);
        searchQuery.setMountainNames(convertArrayToArrayList(mountainNames));
        searchQuery.setRouteNames(convertArrayToArrayList(routeNames));
        searchQuery.setStandardRoute(isStandardRoute);
        searchQuery.setSnowRoute(isSnowRoute);
        searchQuery.setGrades(convertArrayToArrayList(grades));
        searchQuery.setGradeQualities(convertArrayToArrayList(gradeQualities));
        searchQuery.setTrailheads(convertArrayToArrayList(trailheads));
        searchQuery.setStartElevation(startElevation);
        searchQuery.setSummitElevation(summitElevation);
        searchQuery.setTotalGain(totalGain);
        searchQuery.setRouteLength(routeLength);
        searchQuery.setExposure(exposure);
        searchQuery.setRockfallPotential(rockfallPotential);
        searchQuery.setRouteFinding(routeFinding);
        searchQuery.setCommitment(commitment);
        searchQuery.setHasMultipleRoutes(hasMultipleRoutes);
        searchQuery.setRouteUrls(convertArrayToArrayList(routeUrls));
        searchQuery.setTrailheadCoordinates(convertArrayToArrayList(trailheadCoordinates));
        searchQuery.setRoadDifficulties(convertArrayToArrayList(roadDifficulties));
        searchQuery.setTrailheadUrls(convertArrayToArrayList(trailheadUrls));
        return searchQuery;
    }
}
