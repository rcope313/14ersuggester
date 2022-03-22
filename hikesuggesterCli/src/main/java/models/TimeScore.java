package models;

import database.models.ImmutableStoredRoute;

public class TimeScore {
    private final ImmutableStoredRoute route;
    private final int timeIndex;
    private final int score;

    public TimeScore(ImmutableStoredRoute route, int timeIndex, int score) {
        this.route = route;
        this.timeIndex = timeIndex;
        this.score = score;
    }

    public ImmutableStoredRoute getRoute() {
        return route;
    }

    public int getTimeIndex() {
        return timeIndex;
    }

    public int getScore() {
        return score;
    }
}
