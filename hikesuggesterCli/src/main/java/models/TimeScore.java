package models;

public class TimeScore {
    private final RouteForecast routeForecast;
    private final int timeIndex;
    private final int score;

    public TimeScore(RouteForecast routeForecast, int timeIndex, int score) {
        this.routeForecast = routeForecast;
        this.timeIndex = timeIndex;
        this.score = score;
    }

    public RouteForecast getRouteForecast() {
        return routeForecast;
    }

    public int getTimeIndex() {
        return timeIndex;
    }

    public int getScore() {
        return score;
    }

}
