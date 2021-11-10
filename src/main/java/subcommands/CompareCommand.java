package subcommands;

import picocli.CommandLine;

@CommandLine.Command(name = "compare", mixinStandardHelpOptions = true)
public class CompareCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "First argument, mountain name")
    String mountainNameA;
    @CommandLine.Parameters(index = "1", description = "Fire argument, route name")
    String routeNameA;
    @CommandLine.Parameters(index = "2", description = "Second argument, mountain name")
    String mountainNameB;
    @CommandLine.Parameters(index = "3", description = "Second argument, route name")
    String routeNameB;
    @CommandLine.Option(names = {"-v", "--verbose"},
            description = "Verbose. Result output will yield all table columns, rather than only differences in arguments")
    public boolean verbose = false;
    @CommandLine.Option(names = {"-q", "--query"}, interactive = true,
            description = "Write MySql query. Tables name: hikesuggester.fourtneer_routes, hikesuggester.trailheads")
    public String query;


    @Override
    public void run() {

    }
}
