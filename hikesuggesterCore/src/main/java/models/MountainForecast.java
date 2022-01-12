package models;

import java.time.LocalDateTime;
import java.util.Date;

public class MountainForecast {
    private final LocalDateTime date;
    private final int temperature;
    private final int windChill;
    private final int windSpeed;
    private final int windDirection;
    private final int humidity;
    private final int cloudCover;
    private final int precipProbability;
    private final int precipAmount;

    public MountainForecast(LocalDateTime date, int temperature, int windChill, int windSpeed, int windDirection, int humidity, int cloudCover, int precipProbability, int precipAmount) {
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

    public LocalDateTime getDate() {
        return date;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getWindChill() {
        return windChill;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getCloudCover() {
        return cloudCover;
    }

    public int getPrecipProbability() {
        return precipProbability;
    }

    public int getPrecipAmount() {
        return precipAmount;
    }
}
