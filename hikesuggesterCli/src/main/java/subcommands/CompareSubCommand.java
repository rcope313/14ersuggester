package subcommands;

import console.CliCompareOutput;
import console.CliSearchOutput;
import database.dao.RoutesTrailheadsDao;
import database.models.CompareQuery;
import database.models.DatabaseConnection;
import picocli.CommandLine;
import java.util.ArrayList;
import java.util.Arrays;

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
    @CommandLine.Option(names = {"-u", "--url"}, arity = "1..2",
            description = "Rather than entering in two separate Route and Mountain Names, enter two urls to compare.")
    public String[] routeUrls;

    @Override
    public void run() {
        CliCompareOutput output = new CliCompareOutput(new RoutesTrailheadsDao(new DatabaseConnection()));
        output.buildCliTable(setCompareQuery());
    }

    private CompareQuery setCompareQuery() {
        CompareQuery query = new CompareQuery();
        query.setMountainName1(mountainName1);
        query.setRouteName1(routeName1);
        query.setMountainName2(mountainName2);
        query.setRouteName2(routeName2);
        query.setRouteUrls(new ArrayList<>(Arrays.asList(routeUrls)));
        return query;
    }
}
