package models;

public class MountainForecast {
    private final String time;
    private final String abbreviatedForecast;
    private final int temp;
    private final String detailedForecast;
    private final String hourlyWeatherForecastUrl;

    public MountainForecast(String time, String abbreviatedForecast, int temp, String detailedForecast, String hourlyWeatherForecastUrl) {
        this.time = time;
        this.abbreviatedForecast = abbreviatedForecast;
        this.temp = temp;
        this.detailedForecast = detailedForecast;
        this.hourlyWeatherForecastUrl = hourlyWeatherForecastUrl;
    }

    public MountainForecast(String time, int temp, String detailedForecast) {
        this.time = time;
        this.temp = temp;
        this.detailedForecast = detailedForecast;
        this.abbreviatedForecast = "";
        this.hourlyWeatherForecastUrl = "";
    }

    public String getTime() {
        return time;
    }

    public String getAbbreviatedForecast() {
        return abbreviatedForecast;
    }

    public int getTemp() {
        return temp;
    }

    public String getDetailedForecast() {
        return detailedForecast;
    }

    public String getHourlyWeatherForecastUrl() {
        return hourlyWeatherForecastUrl;
    }
}

