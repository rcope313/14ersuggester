package subcommands;

import picocli.CommandLine;

@CommandLine.Command(name = "compare", mixinStandardHelpOptions = true)
public class CompareSubCommand implements Runnable {

    @CommandLine.Parameters(index = "0", description = "First argument, mountain name")
    String mountainName1;

    @CommandLine.Parameters(index = "1", description = "First argument, route name")
    String routeName1;

    @CommandLine.Parameters(index = "2", description = "Second argument, mountain name")
    String mountainName2;

    @CommandLine.Parameters(index = "3", description = "Second argument, route name")
    String routeName2;

    @CommandLine.Option(names = {"-v", "--verbose"},
            description = "Verbose. Result output will yield all table columns, rather than only differences in arguments")
    public boolean verbose = false;

    @CommandLine.Option(names = {"-u", "--url"}, arity = "1..2",
            description = "Rather than entering in two seperate Route and Mountain Names, enter two urls to compare.")
    public String[] routeUrls;

    @CommandLine.Option(names = {"-q", "--query"}, interactive = true,
            description = "Write MySql query. Tables name: hikesuggester.fourtneer_routes, hikesuggester.trailheads")
    public String query;


    @Override
    public void run() {

    }
}
