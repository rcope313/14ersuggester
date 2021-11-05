package subcommands;

import picocli.CommandLine;

@CommandLine.Command(name = "query", mixinStandardHelpOptions = true)
public class Query implements Runnable {

    @CommandLine.Parameters(interactive = true,
            description = "Write MySql query. Tables name: hikesuggester.fourtneer_routes, hikesuggester.trailheads")
    String query;


    @Override
    public void run() {

    }
}
