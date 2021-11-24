package subcommands;

import models.CliColumn;
import models.FourteenerRoute;
import mysql.query.MySqlCompareQuery;
import picocli.CommandLine;
import utility.Utils;

import java.util.ArrayList;

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

        MySqlCompareQuery compareQuery = setCompareQuery();

        String compareQuerySyntax = compareQuery.createMySqlSyntaxForCompareQuery();
        ArrayList<FourteenerRoute> routeList = compareQuery.createFourteenerRoutesFromCliInput(compareQuerySyntax);
        String differenceString = compareQuery.createDifferenceString(routeList.get(0), routeList.get(1));

        compareQuery.viewCliTable
                (compareQuery.designateCliColumnFieldsGeneral(),
                compareQuerySyntax,
                differenceString);

    }

    private MySqlCompareQuery setCompareQuery() {

        MySqlCompareQuery compareQuery = new MySqlCompareQuery();

        compareQuery.setMountainName1(mountainName1);
        compareQuery.setRouteName1(routeName1);
        compareQuery.setMountainName2(mountainName2);
        compareQuery.setRouteName2(routeName2);
        compareQuery.setRouteUrls(Utils.convertArrayToArrayList(routeUrls));


        return compareQuery;


    }
}
