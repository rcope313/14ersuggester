package models;

public class MountainForecast {
    private String shortTermForecastBase;
    private String shortTermForecastMid;
    private String shortTermForecastSummit;
    private String longTermForecastBase;
    private String longTermForecastMid;
    private String longTermForecastSummit;
    private String url;

    public String getShortTermForecastBase() {
        return shortTermForecastBase;
    }

    public void setShortTermForecastBase(String shortTermForecastBase) {
        this.shortTermForecastBase = shortTermForecastBase;
    }

    public String getShortTermForecastMid() {
        return shortTermForecastMid;
    }

    public void setShortTermForecastMid(String shortTermForecastMid) {
        this.shortTermForecastMid = shortTermForecastMid;
    }

    public String getShortTermForecastSummit() {
        return shortTermForecastSummit;
    }

    public void setShortTermForecastSummit(String shortTermForecastSummit) {
        this.shortTermForecastSummit = shortTermForecastSummit;
    }

    public String getLongTermForecastBase() {
        return longTermForecastBase;
    }

    public void setLongTermForecastBase(String longTermForecastBase) {
        this.longTermForecastBase = longTermForecastBase;
    }

    public String getLongTermForecastMid() {
        return longTermForecastMid;
    }

    public void setLongTermForecastMid(String longTermForecastMid) {
        this.longTermForecastMid = longTermForecastMid;
    }

    public String getLongTermForecastSummit() {
        return longTermForecastSummit;
    }

    public void setLongTermForecastSummit(String longTermForecastSummit) {
        this.longTermForecastSummit = longTermForecastSummit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
