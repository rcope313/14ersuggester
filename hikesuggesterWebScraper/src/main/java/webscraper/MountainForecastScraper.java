package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.MountainForecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MountainForecastScraper {
    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(MountainForecastScraper.class);

    public ArrayList<MountainForecast> buildListOfMountainForecast(String url) throws IOException {
        final HtmlPage page = webClient.getPage(url);
        return new ArrayList<>();
    }

    public MountainForecast buildAMountainForecast() {
         Date date = parseDate();
         int temperature = parseTemperature();
         int windChill = parseWindChill();
         int windSpeed = parseWindSpeed();
         int windDirection = parseWindDirection();
         int humidity = parseHumidity();
         int cloudCover = parseCloudCover();
         int precipProbability = parsePrecipProbability();
         int precipAmount = paresePrecipAmount();

         return new MountainForecast(date, temperature, windChill, windSpeed, windDirection, humidity, cloudCover, precipProbability, precipAmount);
    }

    private Date parseDate() {
        return null;
    }

    private int parseTemperature() {
        return 0;
    }

    private int parseWindChill() {
        return 0;
    }

    private int parseWindSpeed() {
        return 0;
    }

    private int parseWindDirection() {
        return 0;
    }

    private int parseHumidity() {
        return 0;
    }

    private int parseCloudCover() {
        return 0;
    }

    private int parsePrecipProbability() {
        return 0;
    }

    private int paresePrecipAmount() {
        return 0;
    }
}
