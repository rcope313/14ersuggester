package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.SearchQuery;

import java.util.ArrayList;

public class CliSearchOutput extends CliOutput{
    private final RoutesTrailheadsDao dao;

    public CliSearchOutput(RoutesTrailheadsDao dao) {
        this.dao = dao;
    }

    public void buildCliTable(SearchQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = dao.get(query);
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
    }
}
