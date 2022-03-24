package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableSearchQuery;
import database.models.ImmutableStoredRouteAndTrailhead;

import java.util.ArrayList;

public class CliSearchOutput extends CliOutput{

    public static void buildCliTable(ImmutableSearchQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = RoutesTrailheadsDao.get(query);
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
    }
}
