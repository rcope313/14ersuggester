package database.models;

import org.immutables.value.Value;

@Value.Immutable
public abstract class AbstractFetchedTrailhead {
    public abstract String getName();
    public abstract String getCoordinates();
    public abstract int getRoadDifficulty();
    public abstract String getRoadDescription();
    public abstract String getWinterAccess();
    public abstract String getUrl();
}
