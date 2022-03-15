package database.models;

import org.immutables.value.Value;
import java.util.ArrayList;
import java.util.Optional;

@Value.Immutable
public abstract class AbstractCompareQuery {
    public abstract String getMountainName1();
    public abstract String getRouteName1();
    public abstract String getMountainName2();
    public abstract String getRouteName2();
    public abstract Optional<ArrayList<String>> getRouteUrls();
}
