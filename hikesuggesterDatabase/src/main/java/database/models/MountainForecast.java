package database.models;

public class MountainForecast {
    private final String date;
    private final String temperature;
    private final String windChill;
    private final String windSpeed;
    private final String windDirection;
    private final String humidity;
    private final String cloudCover;
    private final String precipProbability;
    private final String precipAmount;

    public MountainForecast(String date, String temperature, String windChill, String windSpeed, String windDirection, String humidity, String cloudCover, String precipProbability, String precipAmount) {
        this.date = date;
        this.temperature = temperature;
        this.windChill = windChill;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.humidity = humidity;
        this.cloudCover = cloudCover;
        this.precipProbability = precipProbability;
        this.precipAmount = precipAmount;
    }

    public String getDate() {
        return date;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWindChill() {
        return windChill;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getCloudCover() {
        return cloudCover;
    }

    public String getPrecipProbability() {
        return precipProbability;
    }

    public String getPrecipAmount() {
        return precipAmount;
    }
}
