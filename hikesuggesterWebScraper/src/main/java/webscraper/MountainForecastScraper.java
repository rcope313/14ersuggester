package webscraper;

import models.FourteenerRoute;
import models.MountainForecast;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.xerces.dom.DeferredElementImpl;
import org.assertj.core.util.VisibleForTesting;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MountainForecastScraper {
    private final Document xmlDocument;
    private final FourteenerRoute route;

    public MountainForecastScraper(FourteenerRoute route) throws Exception {
        this.route = route;
        this.xmlDocument = buildXMLDocumentFromFourteenerRoute();
    }

    private Document buildXMLDocumentFromFourteenerRoute() throws Exception {
        URL url = new URL
                (getNOAAXmlHourlyWeatherForecast
                        (getNOAAHourlyWeatherForecast
                            (getNOAASevenDayWeatherForecast())));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(url.openStream());
        document.getDocumentElement().normalize();
        return document;
    }

    @VisibleForTesting
    String getNOAASevenDayWeatherForecast() throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(this.getRoute().getUrl()).get();
        String unescapedXml = StringEscapeUtils.unescapeXml(doc.select("a:contains(NOAA Forecast)").get(0).attributes().toString());
        return unescapedXml.substring(24,unescapedXml.length()-1);
    }

    @VisibleForTesting
    static String getNOAAHourlyWeatherForecast(String url) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        String unescapedXml = StringEscapeUtils.unescapeXml(doc.select("a[href]:contains(Hourly)").get(1).attributes().toString());
        return "https://forecast.weather.gov/" + unescapedXml.substring(7,unescapedXml.length()-1);
    }

    @VisibleForTesting
    static String getNOAAXmlHourlyWeatherForecast(String url) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        String unescapedXml =StringEscapeUtils.unescapeXml(doc.select("a[href*=digitalDWML]").get(0).attributes().toString());
        return "https:" + unescapedXml.substring(7,unescapedXml.length()-1);
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

    public Document getXmlDocument() {
        return xmlDocument;
    }

    public FourteenerRoute getRoute() {
        return route;
    }
}
