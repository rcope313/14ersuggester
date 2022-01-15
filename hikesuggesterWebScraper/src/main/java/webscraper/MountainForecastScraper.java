package webscraper;

import models.MountainForecast;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.assertj.core.util.VisibleForTesting;
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

    private Document buildXMLDocument() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(getPathName()));
        document.getDocumentElement().normalize();
        return document;
    }

    @VisibleForTesting
    String[] parseElements(Document document, String expression) throws XPathExpressionException {
        XPath xPath =  XPathFactory.newInstance().newXPath();
        NodeList nodelist = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
        return ((DTMNodeList) nodelist).getDTMIterator().toString().split("\n");
    }

    public String getPathName() {
        return pathName;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }
}
