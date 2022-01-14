package webscraper;

import models.MountainForecast;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MountainForecastScraper {
    private final String pathName;
    private final Document xmlDocument;

    public MountainForecastScraper(String pathName) throws ParserConfigurationException, IOException, SAXException {
        this.pathName = pathName;
        this.xmlDocument = buildXMLDocument();
    }

    public void buildAMountainForecasts() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        ArrayList<MountainForecast> mountainForecasts = new ArrayList<>();

        String[] dateArray = retrieveStartValidTimeOnly(parseElements(getXmlDocument(), "//time-layout"));
        String[] windSpeedArray = parseElements(getXmlDocument(), "//wind-speed[@type='sustained']");
        String[] cloudCoverArray = parseElements(getXmlDocument(), "//cloud-amount");
        String[] probOfPrecipArray = parseElements(getXmlDocument(), "//probability-of-precipitation");
        String[] humidityArray = parseElements(getXmlDocument(), "//humidity");
        String[] windDirectionArray = parseElements(getXmlDocument(), "//direction");
        String[] tempArray = parseElements(getXmlDocument(), "//temperature[@type='hourly']");
        String[] precipAmountArray = parseElements(getXmlDocument(), "//hourly-qpf");
        String[] windChillArray = parseElements(getXmlDocument(), "//temperature[@type='wind chill']");

        for (int idx = 0; idx < windChillArray.length; idx++) {
            MountainForecast mountainForecast = new MountainForecast(
                            dateArray[idx], Integer.parseInt(tempArray[idx]), Integer.parseInt(windChillArray[idx]),
                            Integer.parseInt(windSpeedArray[idx]), Integer.parseInt(windDirectionArray[idx]),
                            Integer.parseInt(humidityArray[idx]), Integer.parseInt(cloudCoverArray[idx]),
                            Integer.parseInt(probOfPrecipArray[idx]), Integer.parseInt(precipAmountArray[idx]));
            mountainForecasts.add(mountainForecast);
        }
    }

    private Document buildXMLDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(getPathName()));
        document.getDocumentElement().normalize();
        return document;
    }

    private String[] parseElements(Document document, String expression) throws XPathExpressionException {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        NodeList nodelist = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        return nodelist.toString().split("\n");
    }

    private static String[] retrieveStartValidTimeOnly(String[] dateArray) {
        return dateArray;
    }

    public String getPathName() {
        return pathName;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }
}
