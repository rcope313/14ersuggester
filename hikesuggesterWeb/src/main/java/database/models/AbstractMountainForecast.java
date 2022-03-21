package database.models;

import org.immutables.value.Value;

@Value.Immutable
public abstract class AbstractMountainForecast {
    public abstract String getDate();
    public abstract String getTemperature();
    public abstract String getWindChill();
    public abstract String getWindSpeed();
    public abstract String getWindDirection();
    public abstract String getHumidity();
    public abstract String getCloudCover();
    public abstract String getPrecipProbability();
    public abstract String getPrecipAmount();
}
