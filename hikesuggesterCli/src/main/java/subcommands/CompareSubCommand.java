package subcommands;

import console.CliOutput;
import database.models.ImmutableCompareQuery;
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
        CliOutput.buildCliTable(setCompareQuery());
    }

    private ImmutableCompareQuery setCompareQuery() {
        return ImmutableCompareQuery.builder()
                .mountainName1(mountainName1)
                .routeName1(routeName1)
                .mountainName2(mountainName2)
                .routeName2(routeName2)
                .routeUrls(new ArrayList<>(Arrays.asList(routeUrls)))
                .build();
    }
}
