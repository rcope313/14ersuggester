package webscraper;

import models.MountainForecast;
import org.junit.Test;
import java.time.LocalDateTime;

public class MountainForecastScraperTest {
    MountainForecast forecastJanuary11th4pm, forecastJanuary11th5pm;
    LocalDateTime january11th4pm, january11th5pm;

    void initData() {
        january11th4pm = LocalDateTime.of(2022, 1, 11, 16, 0, 0);
        january11th5pm = LocalDateTime.of(2022, 1, 11, 17, 0, 0);

        forecastJanuary11th4pm =
                new MountainForecast(
                        january11th4pm,
                        28,
                        16,
                        16,
                        300,
                        29,
                        14,
                        0,
                        0);

        forecastJanuary11th5pm =
                new MountainForecast(
                        january11th4pm,
                        23,
                        11,
                        13,
                        290,
                        36,
                        5,
                        0,
                        0);
    }

    @Test
    public void itBuildsAMountainForecast() {
        this.initData();
    }


}
