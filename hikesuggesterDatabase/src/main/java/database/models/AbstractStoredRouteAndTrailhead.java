package database.models;

import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public abstract class AbstractStoredRouteAndTrailhead {
    public abstract int getRouteId();
    public abstract String getMountainName();
    public abstract String getRouteName();
    public abstract boolean getIsSnowRoute();
    public abstract boolean getIsStandardRoute();
    public abstract int getGrade();
    public abstract String getGradeQuality();
    public abstract int getStartElevation();
    public abstract int getSummitElevation();
    public abstract int getTotalGain();
    public abstract double getRouteLength();
    public abstract String getExposure();
    public abstract String getRockfallPotential();
    public abstract String getRouteFinding();
    public abstract String getCommitment();
    public abstract boolean getHasMultipleRoutes();
    public abstract String getRouteUrl();
    public abstract String getRouteUpdateDate();
    public abstract String getTrailhead();
    public abstract Optional<Integer> getTrailheadId();
    public abstract Optional<String> getTrailheadCoordinates();
    public abstract Optional<Integer> getRoadDifficulty();
    public abstract Optional<String> getRoadDescription();
    public abstract Optional<String> getWinterAccess();
    public abstract Optional<String> getTrailheadUrl();
    public abstract Optional<String> getTrailheadUpdateDate();
}
