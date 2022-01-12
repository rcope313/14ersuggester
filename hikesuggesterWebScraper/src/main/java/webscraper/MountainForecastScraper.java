package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.MountainForecast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MountainForecastScraper {
    final private static WebClient webClient = new WebClient();
    final private static Logger LOG = LoggerFactory.getLogger(MountainForecastScraper.class);

    public static void main (String[] args) throws IOException, SAXException, ParserConfigurationException {
        buildAMountainForecast("/Users/rachelcope/Documents/hikesuggester/hikesuggesterWebScraper/src/main/resources/mountainforecast.xml");
    }

    public ArrayList<MountainForecast> buildListOfMountainForecast(String url) throws IOException {
        final HtmlPage page = webClient.getPage(url);
        return new ArrayList<>();
    }

    public static void buildAMountainForecast(String pathName) throws ParserConfigurationException, IOException, SAXException {
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        //Build Document
        Document document = builder.parse(new File(pathName));

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

        //Here comes the root node
        Element root = document.getDocumentElement();
        System.out.println(root.getNodeName());
    }

    private LocalDateTime parseDate() {
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
