package subcommands;

import console.CliCompareOutput;
import database.dao.RoutesTrailheadsDao;
import database.models.CompareQuery;
import database.models.DatabaseConnection;
import picocli.CommandLine;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@CommandLine.Command(name = "compare", mixinStandardHelpOptions = true)
public class CompareSubCommand implements Runnable {
    @CommandLine.Option(names = {"--mountain1"}, description = "First argument, mountain name")
    Optional<String> mountainName1;
    @CommandLine.Option(names = {"--route1"}, description = "First argument, route name")
    Optional<String> routeName1;
    @CommandLine.Option(names = {"--mountain2"}, description = "Second argument, mountain name")
    Optional<String> mountainName2;
    @CommandLine.Option(names = {"--route2"}, description = "Second argument, route name")
    Optional<String> routeName2;
    @CommandLine.Option(names = {"--url"}, arity = "1..2",
            description = "Rather than entering in two separate Route and Mountain Names, enter two urls to compare.")
    public String[] routeUrls;

    @Override
    public void run() {
        CliCompareOutput output = null;
        try {
            output = new CliCompareOutput(new RoutesTrailheadsDao(new DatabaseConnection().getConnection()));
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        assert output != null;
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
