package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.MountainForecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.ArrayList;

public class MountainForecastScraper {
    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(MountainForecastScraper.class);

    public ArrayList<MountainForecast> buildListOfMountainForecast(String url) throws IOException {
        final HtmlPage page = webClient.getPage(url);

        return new ArrayList<>();
    }

    public MountainForecast buildAMountainForecast(HtmlDivision div) {
        String time;
        String abbreviatedForecast;
        int temp;
        String detailedForecast;
        String hourlyWeatherForecastUrl;

        time = scrapeTime();
        abbreviatedForecast = scrapeAbbreviatedForecast();
        temp = scrapeTemp();
        detailedForecast = scrapeDetailedForecast();
        hourlyWeatherForecastUrl = scrapeHourlyWeatherForecastUrl();

        return new MountainForecast(time, abbreviatedForecast, temp, detailedForecast, hourlyWeatherForecastUrl);
    }

    private String scrapeTime() {
        return "";
    }

    private String scrapeAbbreviatedForecast() {
        return "";
    }

    private int scrapeTemp() {
        return 0;
    }

    private String scrapeDetailedForecast() {
        return "";
    }

    private String scrapeHourlyWeatherForecastUrl() {
        return "";
    }

}
