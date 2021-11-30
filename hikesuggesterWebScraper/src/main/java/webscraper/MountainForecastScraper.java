package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import models.MountainForecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class MountainForecastScraper {

    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(MountainForecastScraper.class);



    public static void main (String[] args) throws IOException {
        var result = new MountainForecastScraper().scrapeMountainForecast("Mt. Evans");

    }


    public MountainForecast scrapeMountainForecast (String mountainName) throws IOException {
        MountainForecast resultMountainForecast = new MountainForecast();
        String mountainForecastUrl = getMountainForecastUrl(mountainName);
        resultMountainForecast.setUrl(mountainForecastUrl);

        webClient.getOptions().setThrowExceptionOnScriptError(false);

        final HtmlPage page = webClient.getPage("https://www.mountain-forecast.com/peaks/"
                + mountainForecastUrl + "/forecasts/4350");

//        HtmlButton elevationButton = (HtmlButton) page.getByXPath("//button[@class='forecast-table-elevation__dropdown']").get(0);
        List<HtmlButton> buttons = page.getByXPath("//button");
        List<HtmlSelect> selects = page.getByXPath("//select");


        HtmlParagraph paragraph0 = (HtmlParagraph) page.getByXPath("//p[@class='location-summary__text']").get(0);
        HtmlParagraph paragraph1 = (HtmlParagraph) page.getByXPath("//p[@class='location-summary__text']").get(1);

        buttons.forEach((button) -> {
            try {
                button.click();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });





        return resultMountainForecast;
    }

    private String getMountainForecastUrl (String mountainName) {
        if (mountainName.substring(0,2).equals("Mt")) {
            return "Mount" + "-" + mountainName.split(". ")[1];
        }
        else {
            return mountainName;
        }

    }
}
