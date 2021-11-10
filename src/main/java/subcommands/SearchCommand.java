package subcommands;

import picocli.CommandLine;

import java.util.ArrayList;

@CommandLine.Command(name = "search", mixinStandardHelpOptions = true)
public class SearchCommand implements Runnable {

    public String executeQuery;

    @CommandLine.Option(names = {"--m", "-mountainnames"}, split = ", ",
            description = "Search by name of mountain. If searching by more than one mountain name, split list with ', '")
    public ArrayList<String> mountainNames;

    @CommandLine.Option(names = {"--r", "-routenames"}, split = ", ",
            description = "Search by name of route. If searching by more than one route name, split list with ', '")
    public ArrayList<String> routeNames;

    @CommandLine.Option(names = {"--sn", "-issnowroute"},
            description = "Search if route is a snow route only")
    public boolean isSnowRoute = true;

    @CommandLine.Option(names = {"--st", "-standardroute"},
            description = "Search if route is a standard route only")
    public boolean isStandardRoute = true;

    @CommandLine.Option(names = {"--g", "-grades"}, split = ", ",
            description = "Search by grade of route. If searching by more than one grade, split list with ', '")
    public ArrayList<Integer> grades;

    @CommandLine.Option(names = {"--t", "-trailheads"}, split = ", ",
            description = "Search by trailhead name. If searching by more than one trailhead, split list with ', '")
    public ArrayList<String> trailheads;

    @CommandLine.Option(names = {"--ste", "-startelevation"},
            description = "Search by start elevation")
    public int startElevation;

    @CommandLine.Option(names = {"--sue", "-summitlevation"},
            description = "Search by summit elevation")
    public int summitElevation;

    @CommandLine.Option(names = {"--tg", "-totalgain"},
            description = "Search by total gain")
    public int totalGain;

    @CommandLine.Option(names = {"--rl", "-routelength"},
            description = "Search by length of route")
    public double routeLength;

    @CommandLine.Option(names = {"--ex", "-exposure"},
            description = "Search by exposure. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'")
    public String exposure;

    @CommandLine.Option(names = {"--rp", "-rockpotential"},
            description = "Search by rockfall potential. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'")
    public String rockPotential;

    @CommandLine.Option(names = {"--rf", "-routefinding"},
            description = "Search by route finding. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'")
    public String routeFinding;

    @CommandLine.Option(names = {"--ct", "-commitment"},
            description = "Search by commitment. " +
                    "Valid strings include: 'Low', 'Moderate', 'Considerable', 'High', and 'Extreme'")
    public String commitment;

    @CommandLine.Option(names = {"--mr", "-mulitpleroutes"},
            description = "Search if there are multiple routes of varied elevation gain and route length. " +
                    "Meaning, the approach road may have winter closures or road difficulty significantly increases. " +
                    "This will be noticed as a '0' in the route length and total gain columns.")
    public boolean isMultipleRoutes = true;

    @CommandLine.Option(names = {"--ru", "-routeurls"}, split = ", ",
            description = "Search by route url. If searching by more than one url, split list with ', '")
    public ArrayList<String> routeUrls;

    @CommandLine.Option(names = {"--tu", "-trailheadurls"}, split = ", ",
            description = "Search by route url. If searching by more than one url, split list with ', '")
    public ArrayList<String> trailheadUrls;

    @CommandLine.Option(names = {"--tc", "-coordinates"},
            description = "Search by trailhead coordinates. Example syntax: '39.60866, -105.79999'")
    public ArrayList<String> trailheadCoordinates;

    @CommandLine.Option(names = {"--rdi", "-roaddifficulty"},
            description = "Search by road difficulty. Road difficulty ranges from 0 through 6.")
    public ArrayList<Integer> roadDifficulties;

    @CommandLine.Option(names = {"-v", "-verbose"},
            description = "Verbose. Result output will yield all table columns, rather than just Mountain Name, Route Name, and URL")
    public boolean verbose = false;

    @CommandLine.Option(names = {"-q", "-query"}, interactive = true,
            description = "Write MySql query. Tables name: hikesuggester.fourtneer_routes, hikesuggester.trailheads")
    public String optionQuery;




    @Override
    public void run() {
        useCommitment();
        System.out.print(executeQuery);

    }


    public void useCommitment() {
        if (commitment != null) {
            executeQuery = executeQuery + commitment;

        }

    }


}
