package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.SearchQuery;

import java.util.ArrayList;

public class CliSearchOutput extends CliOutput{

    public static void buildCliTable(SearchQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = RoutesTrailheadsDao.get(query);
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
    }
}
