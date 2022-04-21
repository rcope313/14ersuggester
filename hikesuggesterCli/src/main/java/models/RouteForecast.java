package models;

import database.models.ImmutableMountainForecast;
import database.models.ImmutableStoredRouteAndTrailhead;

import java.util.List;

public class RouteForecast {
    private final ImmutableStoredRouteAndTrailhead route;
    private final List<ImmutableMountainForecast> sevenDayForecast;

    public RouteForecast(ImmutableStoredRouteAndTrailhead route, List<ImmutableMountainForecast> sevenDayForecast) {
        this.route = route;
        this.sevenDayForecast = sevenDayForecast;
    }

    public ImmutableStoredRouteAndTrailhead getRoute() {
        return route;
    }

    public List<ImmutableMountainForecast> getSevenDayForecast() {
        return sevenDayForecast;
    }

    public boolean isHighConsequence() {
        assert route != null;
        return  route.getGrade() >= 3 ||
                route.getCommitment().equals("High") || route.getExposure().equals("High") || route.getRockfallPotential().equals("High") || route.getRouteFinding().equals("High") ||
                route.getCommitment().equals("Extreme") || route.getExposure().equals("Extreme") || route.getRockfallPotential().equals("Extreme") || route.getRouteFinding().equals("Extreme") ||
                route.getIsSnowRoute();
    }
}
