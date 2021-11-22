package mysql.query;

import models.CliColumn;
import models.HikeSuggesterDatabase;
import java.util.ArrayList;

public class MySqlLiteralQuery extends MySqlQuery{
    private boolean isVerbose = false;
    private String query = null;

    public static void main (String[] args) {
        MySqlLiteralQuery literalQuery = new MySqlLiteralQuery();
        literalQuery.setQuery("WHERE fourteener_routes.mountainName IN ('Mt. Elbert', 'Mt. Massive')");
        literalQuery.setVerbose(true);

        String literalQuerySyntax = literalQuery.createMySqlSyntaxForLiteralQuery();
        ArrayList<CliColumn> cliColumnFields;

        if (literalQuery.isVerbose()) {
            cliColumnFields = literalQuery.designateCliColumnFieldsVerbose();
        } else {
            cliColumnFields = literalQuery.designateCliColumnFieldsNonVerbose();

        }
        literalQuery.viewCliTable(cliColumnFields, literalQuerySyntax);


    }

    public String createMySqlSyntaxForLiteralQuery() {
        return "SELECT *" +
                " FROM " + HikeSuggesterDatabase.FOURTEENER_ROUTES  +
                " LEFT OUTER JOIN " + HikeSuggesterDatabase.TRAILHEADS  +
                " ON " + HikeSuggesterDatabase.ROUTE_TRAILHEAD + " = " + HikeSuggesterDatabase.TRAILHEAD_NAME + " " +
                getQuery();

    }





    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
