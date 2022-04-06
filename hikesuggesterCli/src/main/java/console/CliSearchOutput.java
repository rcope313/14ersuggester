package console;

import database.dao.RoutesTrailheadsDao;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

public class CliSearchOutput extends CliOutput{
    private final RoutesTrailheadsDao dao;
    final private static Logger LOG = LoggerFactory.getLogger(CliSearchOutput.class);

    public CliSearchOutput(RoutesTrailheadsDao dao) {
        this.dao = dao;
    }

    public void buildCliTable(SearchQuery query) {
        buildCliTableHeaders(designateColumnFields());
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = dao.get(query);
        routes.forEach((route) -> {
            try {
                dao.update(route);
                LOG.info("Updated {}", route.getRouteUrl());
            } catch (Exception e) {
                LOG.warn("Unable to update {}", route.getRouteUrl());
            }
        });
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
    }
}
