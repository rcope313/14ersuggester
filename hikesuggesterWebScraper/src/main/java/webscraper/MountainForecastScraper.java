package webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import models.MountainForecast;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.assertj.core.util.VisibleForTesting;
import org.w3c.dom.Document;
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
    private final String fileName;
    private final URL url;
    private final Document xmlDocument;
    private final static WebClient webClient = new WebClient();

    public MountainForecastScraper(URL url) throws Exception {
        this.fileName = null;
        this.url = url;
        this.xmlDocument = buildXMLDocumentFromUrl();
    }

    @VisibleForTesting
    MountainForecastScraper(String fileName) throws Exception {
        this.fileName = fileName;
        this.url = null;
        this.xmlDocument = buildXMLDocumentFromFile();
    }

    private static String getHourlyWeatherForecastXMLFileUrl() throws IOException {
        final HtmlPage page = webClient.getPage("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&unit=0&lg=english&FcstType=graphical");
        final HtmlAnchor anchor = (HtmlAnchor) page.getByXPath("//a").get(10);
        return anchor.getPage().toString().substring(9);
    }

    public ArrayList<MountainForecast> buildMountainForecasts() throws XPathExpressionException {
        ArrayList<MountainForecast> mountainForecasts = new ArrayList<>();

        String[] dateArray = parseElements(getXmlDocument(), "//time-layout");
        String[] windSpeedArray = parseElements(getXmlDocument(), "//wind-speed[@type='sustained']");
        String[] cloudCoverArray = parseElements(getXmlDocument(), "//cloud-amount");
        String[] probOfPrecipArray = parseElements(getXmlDocument(), "//probability-of-precipitation");
        String[] humidityArray = parseElements(getXmlDocument(), "//humidity");
        String[] windDirectionArray = parseElements(getXmlDocument(), "//direction");
        String[] tempArray = parseElements(getXmlDocument(), "//temperature[@type='hourly']");
        String[] precipAmountArray = parseElements(getXmlDocument(), "//hourly-qpf");
        String[] windChillArray = parseElements(getXmlDocument(), "//temperature[@type='wind chill']");
        int idx = 1;
        int dateIdx = 2;

        while (idx < windChillArray.length) {
            mountainForecasts.add(buildAMountainForecast(idx, dateIdx, dateArray, windSpeedArray, cloudCoverArray, probOfPrecipArray,
                    humidityArray, windDirectionArray, tempArray, precipAmountArray, windChillArray));
            idx++;
            dateIdx += 2;
        }
        return mountainForecasts;
    }

    @VisibleForTesting
    MountainForecast buildAMountainForecast(int idx, int dateIdx, String[] dateArray,String[] windSpeedArray, String[] cloudCoverArray,
                                            String[] probOfPrecipArray, String[] humidityArray, String[] windDirectionArray,
                                            String[] tempArray, String[] precipAmountArray, String[] windChillArray) {
        return new MountainForecast(
                    dateArray[dateIdx], Integer.parseInt(tempArray[idx]), Integer.parseInt(windChillArray[idx]),
                    Integer.parseInt(windSpeedArray[idx]), Integer.parseInt(windDirectionArray[idx]),
                    Integer.parseInt(humidityArray[idx]), Integer.parseInt(cloudCoverArray[idx]),
                    Integer.parseInt(probOfPrecipArray[idx]), Double.parseDouble(precipAmountArray[idx]));
    }

    private Document buildXMLDocumentFromUrl() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(getUrl().openStream());
        document.getDocumentElement().normalize();
        return document;
    }

    private Document buildXMLDocumentFromFile() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(getFileName()));
        document.getDocumentElement().normalize();
        return document;
    }

    @VisibleForTesting
    String[] parseElements(Document document, String expression) throws XPathExpressionException {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        NodeList nodelist = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        return ((DTMNodeList) nodelist).getDTMIterator().toString().split("\n");
    }

    private String getFileName() {
        return fileName;
    }

    public URL getUrl() {
        return url;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }
}
