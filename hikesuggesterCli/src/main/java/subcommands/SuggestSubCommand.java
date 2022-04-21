package subcommands;

import console.CliSuggestOutput;
import database.dao.RoutesTrailheadsDao;
import database.models.DatabaseConnection;
import database.models.SearchQuery;
import picocli.CommandLine;
import webscraper.MountainForecastScraper;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;


@CommandLine.Command(name = "suggest", mixinStandardHelpOptions = true)
public class SuggestSubCommand implements Runnable {

    @CommandLine.Option(names = {"-m", "--mountainnames"}, arity = "1..*",
            description = "Search by name of mountain. If searching by more than one mountain name, split list with space only.")
    public String[] mountainNames;
    @CommandLine.Option(names = {"-r", "--routenames"}, arity = "1..*",
            description = "Search by name of route. If searching by more than one route name, split list with space only.")
    public String[] routeNames;
    @CommandLine.Option(names = {"-s", "--standardroute"},
            description = "Search if route is a standard route only.")
    public Optional<Boolean> isStandardRoute;
    @CommandLine.Option(names = {"--snowroute"},
            description = "Search if route is a snow route only.")
    public Optional<Boolean> isSnowRoute;
    @CommandLine.Option(names = {"--grades"}, arity = "1..*",
            description = "Search by grade of route. If searching by more than one grade, split list with space only..")
    public Integer[] grades;
    @CommandLine.Option(names = {"--gradequalities"}, arity = "1..*",
            description = "Search by grade qualities of route. Valid options include 'easy', 'difficult', and an empty string." +
                    "If searching by more than one quality, split list with space only.")
    public String[] gradeQualities;
    @CommandLine.Option(names = {"--trailheads"}, arity = "1..*",
            description = "Search by trailhead name. If searching by more than one trailhead, split list with space only..")
    public String[] trailheads;
    @CommandLine.Option(names = {"--startelevation"},
            description = "Search by start elevation. Result will yield all results greater than or equal to input.")
    public Optional<Integer> startElevation;
    @CommandLine.Option(names = {"--summitelevation"},
            description = "Search by summit elevation. Result will yield all results greater than or equal to input.")
    public Optional<Integer> summitElevation;
    @CommandLine.Option(names = {"--totalgain"},
            description = "Search by total gain. Result will yield all results greater than or equal to input.")
    public Optional<Integer> totalGain;
    @CommandLine.Option(names = {"--routelength"},
            description = "Search by length of route. Result will yield all results greater than or equal to input.")
    public Optional<Double> routeLength;
    @CommandLine.Option(names = {"--exposure"},
            description = "Search by exposure. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String> exposure;
    @CommandLine.Option(names = {"--rockfallpotential"},
            description = "Search by rockfall potential. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String>  rockfallPotential;
    @CommandLine.Option(names = {"--routefinding"},
            description = "Search by route finding. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String>  routeFinding;
    @CommandLine.Option(names = {"--commitment"},
            description = "Search by commitment. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String>  commitment;
    @CommandLine.Option(names = {"--mulitpleroutes"},
            description = "Search if there are multiple routes of varied elevation gain and route length. " +
                    "Meaning, the approach road may have winter closures or such that the road difficulty significantly increases. " +
                    "This will be noted as a '0' in the route length and total gain columns.")
    public Optional<Boolean> hasMultipleRoutes;
    @CommandLine.Option(names = {"--routeurls"}, arity = "1..*",
            description = "Search by route url. If searching by more than one url, split list with space only.")
    public String[] routeUrls;
    @CommandLine.Option(names = {"--roaddifficulty"},
            description = "Search by road difficulty. Road difficulty ranges from 0 through 6.")
    public Optional<Integer> roadDifficulty;
    @CommandLine.Option(names = {"--trailheadurls"}, arity = "1..*",
            description = "Search by trailhead url. If searching by more than one url, split list with space only.")
    public String[] trailheadUrls;

    @Override
    public void run() {
        CliSuggestOutput output = null;
        try {
            output = new CliSuggestOutput(new RoutesTrailheadsDao(new DatabaseConnection().getConnection()), new MountainForecastScraper());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        assert output != null;
        output.buildCliTable(setSearchQuery());
    }

    private SearchQuery setSearchQuery() {
        SearchQuery query = new SearchQuery();
        if (mountainNames != null) {
            query.setMountainNames(new ArrayList<>(Arrays.asList(mountainNames)));
        }
        if (routeNames != null) {
            query.setRouteNames(new ArrayList<>(Arrays.asList(routeNames)));
        }
        if (grades != null) {
            query.setGrades(new ArrayList<>(Arrays.asList(grades)));
        }
        if (gradeQualities != null) {
            query.setGradeQualities(new ArrayList<>(Arrays.asList(gradeQualities)));
        }
        if (trailheads != null) {
            query.setTrailheads(new ArrayList<>(Arrays.asList(trailheads)));
        }
        if (routeUrls != null) {
            query.setRouteUrls(new ArrayList<>(Arrays.asList(routeUrls)));
        }
        if (trailheadUrls != null) {
            query.setTrailheadUrls(new ArrayList<>(Arrays.asList(trailheadUrls)));
        }
        startElevation.ifPresent(query::setStartElevation);
        summitElevation.ifPresent(query::setSummitElevation);
        totalGain.ifPresent(query::setTotalGain);
        routeLength.ifPresent(query::setRouteLength);
        exposure.ifPresent(query::setExposure);
        rockfallPotential.ifPresent(query::setRockfallPotential);
        routeFinding.ifPresent(query::setRouteFinding);
        commitment.ifPresent(query::setCommitment);
        hasMultipleRoutes.ifPresent(query::setHasMultipleRoutes);
        isSnowRoute.ifPresent(query::setSnowRoute);
        isStandardRoute.ifPresent(query::setStandardRoute);
        roadDifficulty.ifPresent(query::setRoadDifficulty);

        return query;
    }

}
