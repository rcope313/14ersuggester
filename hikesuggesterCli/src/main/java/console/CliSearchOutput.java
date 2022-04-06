package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.SearchQuery;
import java.util.ArrayList;
import java.util.logging.Level;

public class CliSearchOutput extends CliOutput{
    private final RoutesTrailheadsDao dao;

    public CliSearchOutput(RoutesTrailheadsDao dao) {
        this.dao = dao;
    }

    public void buildCliTable(SearchQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = dao.get(query);
        routes.forEach((route) -> {
            try {
                dao.update(route);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
    }
}
