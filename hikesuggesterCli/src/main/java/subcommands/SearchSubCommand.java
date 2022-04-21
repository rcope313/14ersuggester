package subcommands;

import console.CliSearchOutput;
import database.dao.RoutesTrailheadsDao;
import database.models.DatabaseConnection;
import database.models.SearchQuery;
import org.assertj.core.util.VisibleForTesting;
import picocli.CommandLine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

@CommandLine.Command(name = "search", mixinStandardHelpOptions = true)
public class SearchSubCommand implements Runnable {
    @CommandLine.Option(names = {"--m", "-mountainnames"}, split = ",",
            description = "Search by name of mountain. If searching by more than one mountain name, split list with ','.")
    public String[] mountainNames;
    @CommandLine.Option(names = {"--r", "-routenames"}, split = ", ",
            description = "Search by name of route. If searching by more than one route name, split list with ','.")
    public String[] routeNames;
    @CommandLine.Option(names = {"--st", "-standardroute"},
            description = "Search if route is a standard route only.")
    public Optional<Boolean> isStandardRoute;
    @CommandLine.Option(names = {"--sn", "-snowroute"},
            description = "Search if route is a snow route only.")
    public Optional<Boolean> isSnowRoute;
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
    public Optional<Integer> startElevation;
    @CommandLine.Option(names = {"--sue", "-summitelevation"},
            description = "Search by summit elevation. Result will yield all results greater than or equal to input.")
    public Optional<Integer> summitElevation;
    @CommandLine.Option(names = {"--tg", "-totalgain"},
            description = "Search by total gain. Result will yield all results greater than or equal to input.")
    public Optional<Integer> totalGain;
    @CommandLine.Option(names = {"--rl", "-routelength"},
            description = "Search by length of route. Result will yield all results greater than or equal to input.")
    public Optional<Double> routeLength;
    @CommandLine.Option(names = {"--ex", "-exposure"},
            description = "Search by exposure. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String> exposure;
    @CommandLine.Option(names = {"--rp", "-rockfallpotential"},
            description = "Search by rockfall potential. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String>  rockfallPotential;
    @CommandLine.Option(names = {"--rf", "-routefinding"},
            description = "Search by route finding. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String>  routeFinding;
    @CommandLine.Option(names = {"--ct", "-commitment"},
            description = "Search by commitment. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'.")
    public Optional<String>  commitment;
    @CommandLine.Option(names = {"--mr", "-mulitpleroutes"},
            description = "Search if there are multiple routes of varied elevation gain and route length. " +
                    "Meaning, the approach road may have winter closures or such that the road difficulty significantly increases. " +
                    "This will be noted as a '0' in the route length and total gain columns.")
    public Optional<Boolean> hasMultipleRoutes;
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
        CliSearchOutput output = null;
        try {
            output = new CliSearchOutput(new RoutesTrailheadsDao(new DatabaseConnection().getConnection()));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        assert output != null;
        output.buildCliTable(setSearchQuery());
    }

    @VisibleForTesting
    SearchQuery setSearchQuery() {
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
        if (roadDifficulties != null) {
            query.setRoadDifficulties(new ArrayList<>(Arrays.asList(roadDifficulties)));
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
        return query;
    }

}
