package database.models;

import org.immutables.value.Value;

@Value.Immutable
public abstract class AbstractStoredRoute {
    public abstract String getMountainName();
    public abstract String getRouteName();
    public abstract boolean getIsSnowRoute();
    public abstract boolean getIsStandardRoute();
    public abstract int getGrade();
    public abstract String getGradeQuality();
    public abstract String getTrailhead();
    public abstract int getStartElevation();
    public abstract int getSummitElevation();
    public abstract int getTotalGain();
    public abstract double getRouteLength();
    public abstract String getExposure();
    public abstract String getRockfallPotential();
    public abstract String getRouteFinding();
    public abstract String getCommitment();
    public abstract boolean getHasMultipleRoutes();
    public abstract String getUrl();
}
