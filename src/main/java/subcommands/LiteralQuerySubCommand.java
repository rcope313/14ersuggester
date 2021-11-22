package subcommands;

import models.CliColumn;
import mysql.query.MySqlCompareQuery;
import mysql.query.MySqlLiteralQuery;
import picocli.CommandLine;
import utility.Utils;

import java.util.ArrayList;

@CommandLine.Command(name = "query", mixinStandardHelpOptions = true)
public class LiteralQuerySubCommand implements Runnable {

    @CommandLine.Parameters(interactive = true,
            description = "Write MySql query. Tables name: hikesuggester.fourtneer_routes, hikesuggester.trailheads")
    public String query;
    @CommandLine.Option(names = {"-v", "-verbose"},
            description = "Verbose. Result output will yield all table columns, rather than just Mountain Name, Route Name, and URL.")
    public boolean verbose = false;


    @Override
    public void run() {

        MySqlLiteralQuery literalQuery = setLiteralQuery();

        String literalQuerySyntax = literalQuery.createMySqlSyntaxForLiteralQuery();
        ArrayList<CliColumn> cliColumnFields;

        if (literalQuery.isVerbose()) {
            cliColumnFields = literalQuery.designateCliColumnFieldsVerbose();
        } else {
            cliColumnFields = literalQuery.designateCliColumnFieldsNonVerbose();

        }
        literalQuery.viewCliTable(cliColumnFields, literalQuerySyntax);

    }

    private MySqlLiteralQuery setLiteralQuery() {

        MySqlLiteralQuery literalQuery = new MySqlLiteralQuery();

        literalQuery.setQuery(query);
        literalQuery.setVerbose(verbose);

        return literalQuery;


    }


}
