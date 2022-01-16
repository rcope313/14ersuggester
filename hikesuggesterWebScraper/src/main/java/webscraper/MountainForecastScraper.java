package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.MountainForecast;
import org.apache.xerces.dom.DeferredElementImpl;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.assertj.core.util.VisibleForTesting;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MountainForecastScraper {
    private final URL url;
    private final Document xmlDocument;
    private final static WebClient webClient = new WebClient();

    public MountainForecastScraper(URL url) throws Exception {
        this.url = url;
        this.xmlDocument = buildXMLDocumentFromUrl();
    }

    private static String getHourlyWeatherForecastXMLFileUrl() throws IOException {
        final HtmlPage page = webClient.getPage("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&unit=0&lg=english&FcstType=graphical");
        final HtmlAnchor anchor = (HtmlAnchor) page.getByXPath("//a").get(10);
        return anchor.getPage().toString().substring(9);
    }

    public ArrayList<MountainForecast> buildMountainForecasts() throws XPathExpressionException {
        ArrayList<MountainForecast> mountainForecasts = new ArrayList<>();

        ArrayList<String> dateList = parseElements("//time-layout", "[start-valid-time: null]");
        ArrayList<String> windSpeedList = parseElements("//wind-speed[@type='sustained']", "[value: null]");
        ArrayList<String> cloudCoverList = parseElements("//cloud-amount", "[value: null]");
        ArrayList<String> probOfPrecipList = parseElements("//probability-of-precipitation", "[value: null]");
        ArrayList<String> humidityList = parseElements("//humidity", "[value: null]");
        ArrayList<String> windDirectionList = parseElements("//direction", "[value: null]");
        ArrayList<String> tempList = parseElements("//temperature[@type='hourly']", "[value: null]");
        ArrayList<String> precipAmountList = parseElements("//hourly-qpf", "[value: null]");
        ArrayList<String> windChillList = parseElements("//temperature[@type='wind chill']", "[value: null]");

        for (int idx = 0; idx < tempList.size(); idx ++) {
            mountainForecasts.add(
                    new MountainForecast(
                    dateList.get(idx), tempList.get(idx), windChillList.get(idx),
                    windSpeedList.get(idx), windDirectionList.get(idx),
                    humidityList.get(idx), cloudCoverList.get(idx),
                    probOfPrecipList.get(idx), precipAmountList.get(idx)));
        }
        return mountainForecasts;
    }

    @VisibleForTesting
    ArrayList<String> parseElements(String expression, String element) throws XPathExpressionException {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        DeferredElementImpl parentElement = (DeferredElementImpl) xPath.compile(expression).evaluate(getXmlDocument(), XPathConstants.NODE);
        Node aNode = parentElement.getFirstChild();
        ArrayList<String> elementList = new ArrayList<>();

        while (aNode.getNextSibling() != null) {
            if (aNode.toString().equals(element)) {
                elementList.add(aNode.getTextContent());
            }
            aNode = aNode.getNextSibling();
        }
        return elementList;
    }

    private Document buildXMLDocumentFromUrl() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(getUrl().openStream());
        document.getDocumentElement().normalize();
        return document;
    }

    public URL getUrl() {
        return url;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }
}
