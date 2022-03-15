package database.models;

import org.immutables.value.Value;

import java.util.ArrayList;

@Value.Immutable
public abstract class AbstractSearchQuery {
    public abstract ArrayList<String> getMountainNames();
    public abstract ArrayList<String> getRouteNames();
    public abstract boolean getIsStandardRoute();
    public abstract boolean getIsSnowRoute();
    public abstract ArrayList<Integer> getGrades();
    public abstract ArrayList<String> getGradeQualities();
    public abstract ArrayList<String> getTrailheads();
    public abstract int getStartElevation();
    public abstract int getSummitElevation();
    public abstract int getTotalGain();
    public abstract double getRouteLength();
    public abstract String getExposure();
    public abstract String getRockfallPotential();
    public abstract String getRouteFinding();
    public abstract String getCommitment();
    public abstract boolean getHasMultipleRoutes();
    public abstract ArrayList<String> getRouteUrls();
    public abstract ArrayList<String> getRrailheadCoordinates();
    public abstract ArrayList<Integer> getRoadDifficulties();
    public abstract ArrayList<String> getTrailheadUrls();
}
