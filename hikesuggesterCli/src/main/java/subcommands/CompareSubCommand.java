package subcommands;

import console.CliCompareOutput;
import console.CliSearchOutput;
import database.dao.RoutesTrailheadsDao;
import database.models.CompareQuery;
import database.models.DatabaseConnection;
import picocli.CommandLine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@CommandLine.Command(name = "compare", mixinStandardHelpOptions = true)
public class CompareSubCommand implements Runnable {
    @CommandLine.Option(names = {"-m1", "--mountain1"}, description = "First argument, mountain name")
    Optional<String> mountainName1;
    @CommandLine.Option(names = {"-r1", "--route1"}, description = "First argument, route name")
    Optional<String> routeName1;
    @CommandLine.Option(names = {"-m2", "--mountain2"}, description = "Second argument, mountain name")
    Optional<String> mountainName2;
    @CommandLine.Option(names = {"-r2", "--route2"}, description = "Second argument, route name")
    Optional<String> routeName2;
    @CommandLine.Option(names = {"-u", "--url"}, arity = "1..2",
            description = "Rather than entering in two separate Route and Mountain Names, enter two urls to compare.")
    public String[] routeUrls;

    @Override
    public void run() {
        CliCompareOutput output = null;
        try {
            output = new CliCompareOutput(new RoutesTrailheadsDao(DatabaseConnection.getConnection()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        output.buildCliTable(setCompareQuery());
    }

    private CompareQuery setCompareQuery() {
        CompareQuery query = new CompareQuery();
        if (routeUrls != null) {
            query.setRouteUrls(new ArrayList<>(Arrays.asList(routeUrls)));
        } else {
            mountainName1.ifPresent(query::setMountainName1);
            mountainName2.ifPresent(query::setMountainName2);
            routeName1.ifPresent(query::setRouteName1);
            routeName2.ifPresent(query::setRouteName1);

        }
        return query;
    }
}
