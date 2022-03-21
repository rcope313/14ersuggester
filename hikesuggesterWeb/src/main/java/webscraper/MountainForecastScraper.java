package webscraper;

import database.models.ImmutableMountainForecast;
import database.models.ImmutableStoredRoute;
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
    private final static String NOAA_HYPERLINK = "a:contains(NOAA Forecast)";
    private final static String HOURLY_WEATHER_FORECAST_HYPERLINK =  "a[href]:contains(Hourly)";
    private final static String DWML_HYPERLINK = "a[href*=digitalDWML]";
    private final static String TIME_LAYOUT_EXP  = "//time-layout";
    private final static String WIND_SPEED_EXP = "//wind-speed[@type='sustained']";
    private final static String CLOUD_AMOUNT_EXP = "//cloud-amount";
    private final static String PRECIP_PROB_EXP = "//probability-of-precipitation";
    private final static String HUMIDITY_EXP = "//humidity";
    private final static String WIND_DIRECTION_EXP = "//direction";
    private final static String HOURLY_TEMP_EXP = "//temperature[@type='hourly']";
    private final static String HOURLY_QPF_EXP = "//hourly-qpf";
    private final static String WIND_CHILL_EXP = "//temperature[@type='wind chill']";
    private final static String NULL_VALUE_ELEMENT = "[value: null]";
    private final static String START_TIME_VALUE_ELEMENT = "[start-valid-time: null]";

    public ArrayList<ImmutableMountainForecast> buildMountainForecasts(ImmutableStoredRoute route) throws Exception {
        ArrayList<ImmutableMountainForecast> mountainForecasts = new ArrayList<>();
        Document noaaXmlHourlyWeatherForecast = buildXMLDocumentFromFourteenerRoute(route);
        ArrayList<String> dateList = parseElements(TIME_LAYOUT_EXP, START_TIME_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> windSpeedList = parseElements(WIND_SPEED_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> cloudCoverList = parseElements(CLOUD_AMOUNT_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> probOfPrecipList = parseElements(PRECIP_PROB_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> humidityList = parseElements(HUMIDITY_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> windDirectionList = parseElements(WIND_DIRECTION_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> tempList = parseElements(HOURLY_TEMP_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> precipAmountList = parseElements(HOURLY_QPF_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);
        ArrayList<String> windChillList = parseElements(WIND_CHILL_EXP, NULL_VALUE_ELEMENT, noaaXmlHourlyWeatherForecast);

        for (int idx = 0; idx < tempList.size(); idx ++) {
            ImmutableMountainForecast forecast = ImmutableMountainForecast.builder()
                    .date(dateList.get(idx))
                    .windSpeed(windSpeedList.get(idx))
                    .cloudCover(cloudCoverList.get(idx))
                    .precipProbability(probOfPrecipList.get(idx))
                    .humidity(humidityList.get(idx))
                    .windDirection(windDirectionList.get(idx))
                    .temperature(tempList.get(idx))
                    .precipAmount(precipAmountList.get(idx))
                    .windChill(windChillList.get(idx))
                    .build();
            mountainForecasts.add(forecast);
        }
        return mountainForecasts;
    }

    private Document buildXMLDocumentFromFourteenerRoute(ImmutableStoredRoute route) throws Exception {
        URL url = new URL(getNOAADwmlHourlyWeatherForecast(getNOAAHourlyWeatherForecast(getNOAASevenDayWeatherForecast(route))));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(url.openStream());
        document.getDocumentElement().normalize();
        return document;
    }

    private String getNOAASevenDayWeatherForecast(ImmutableStoredRoute route) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(route.getUrl()).get();
        String unescapedXml = StringEscapeUtils.unescapeXml(doc.select(NOAA_HYPERLINK).get(0).attributes().toString());
        return unescapedXml.substring(24,unescapedXml.length()-1);
    }

    private static String getNOAAHourlyWeatherForecast(String url) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        String unescapedXml = StringEscapeUtils.unescapeXml(doc.select(HOURLY_WEATHER_FORECAST_HYPERLINK).get(1).attributes().toString());
        return "https://forecast.weather.gov/" + unescapedXml.substring(7,unescapedXml.length()-1);
    }

    private static String getNOAADwmlHourlyWeatherForecast(String url) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        String unescapedXml =StringEscapeUtils.unescapeXml(doc.select(DWML_HYPERLINK).get(0).attributes().toString());
        return "https:" + unescapedXml.substring(7,unescapedXml.length()-1);
    }

    private ArrayList<String> parseElements(String expression, String element, Document doc) throws XPathExpressionException {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        DeferredElementImpl parentElement = (DeferredElementImpl) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
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
}
