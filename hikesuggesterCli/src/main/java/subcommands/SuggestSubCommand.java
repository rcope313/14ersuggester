package subcommands;

import console.CliSuggestOutput;
import database.dao.RoutesTrailheadsDao;
import database.models.DatabaseConnection;
import database.models.SearchQuery;
import picocli.CommandLine;
import webscraper.MountainForecastScraper;

import java.util.ArrayList;
import java.util.Arrays;

@CommandLine.Command(name = "suggest", mixinStandardHelpOptions = true)
public class SuggestSubCommand implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;
    @CommandLine.Option(names = {"-v", "-verbose"},
            description = "Verbose. Result output will yield all table columns, rather than just Mountain Name, Route Name, and URL.")
    public boolean verbose = false;
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
        CliSuggestOutput output = new CliSuggestOutput(new RoutesTrailheadsDao(new DatabaseConnection()), new MountainForecastScraper());
        output.buildCliTable(setSearchQuery());
    }

    private SearchQuery setSearchQuery() {
        SearchQuery query = new SearchQuery();
        query.setMountainNames(new ArrayList<>(Arrays.asList(mountainNames)));
        query.setRouteNames(new ArrayList<>(Arrays.asList(routeNames)));
        query.setStandardRoute(isStandardRoute);
        query.setSnowRoute(isSnowRoute);
        query.setGrades(new ArrayList<>(Arrays.asList(grades)));
        query.setGradeQualities(new ArrayList<>(Arrays.asList(gradeQualities)));
        query.setTrailheads(new ArrayList<>(Arrays.asList(trailheads)));
        query.setStartElevation(startElevation);
        query.setSummitElevation(summitElevation);
        query.setTotalGain(totalGain);
        query.setRouteLength(routeLength);
        query.setExposure(exposure);
        query.setRockfallPotential(rockfallPotential);
        query.setRouteFinding(routeFinding);
        query.setCommitment(commitment);
        query.setHasMultipleRoutes(hasMultipleRoutes);
        query.setRouteUrls(new ArrayList<>(Arrays.asList(routeUrls)));
        query.setRoadDifficulties(new ArrayList<>(Arrays.asList(roadDifficulties)));
        query.setTrailheadUrls(new ArrayList<>(Arrays.asList(trailheadUrls)));
        return query;
    }

}