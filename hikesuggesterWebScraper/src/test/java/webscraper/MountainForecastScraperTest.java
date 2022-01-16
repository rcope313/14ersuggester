package webscraper;

import models.MountainForecast;
import org.junit.Test;
import org.w3c.dom.Document;

import java.net.URL;

import static org.assertj.core.api.Assertions.*;

public class MountainForecastScraperTest {
    MountainForecast forecastJanuary15th11am, forecastJanuary15th12pm;
    MountainForecastScraper mountainForecastXMLFile, mountainForecastUrl;
    String[] dateArray, windSpeedArray, cloudCoverArray, probOfPrecipArray, humidityArray, windDirectionArray,
            tempArray, precipAmountArray,  windChillArray;

    void initData() throws Exception {

        forecastJanuary15th11am =
                new MountainForecast(
                        "2022-01-15T11:00:00-07:00", 10, 12, 10, 340,
                        33, 3, 0, 0);
        forecastJanuary15th12pm =
                new MountainForecast(
                        "2022-01-15T12:00:00-07:00", 10, 20, 10, 340,
                        28, 3, 0, 0);

        mountainForecastXMLFile = new MountainForecastScraper("/Users/rachelcope/Documents/hikesuggester/hikesuggesterWebScraper/src/main/resources/mountainForecast.xml");
        mountainForecastUrl = new MountainForecastScraper(new URL("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&FcstType=digitalDWML"));

        dateArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//time-layout");
        windSpeedArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//wind-speed[@type='sustained']");
        cloudCoverArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//cloud-amount");
        probOfPrecipArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//probability-of-precipitation");
        humidityArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//humidity");
        windDirectionArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//direction");
        tempArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//temperature[@type='hourly']");
        precipAmountArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//hourly-qpf");
        windChillArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//temperature[@type='wind chill']");
    }

    @Test
    public void itBuildsAnArrayListOfMountainForecast() throws Exception {
        this.initData();
        assertThat(mountainForecastXMLFile.buildMountainForecasts().size()).isEqualTo(168);
        assertThat(mountainForecastXMLFile.buildMountainForecasts().get(0)).usingRecursiveComparison().isEqualTo(forecastJanuary15th11am);
        assertThat(mountainForecastXMLFile.buildMountainForecasts().get(1)).usingRecursiveComparison().isEqualTo(forecastJanuary15th12pm);
    }

    @Test
    public void itBuildsAMountainForecast() throws Exception {
        this.initData();
        assertThat(mountainForecastXMLFile.buildAMountainForecast(1, 2, dateArray, windSpeedArray, cloudCoverArray,
                probOfPrecipArray, humidityArray, windDirectionArray, tempArray, precipAmountArray, windChillArray))
                .usingRecursiveComparison()
                .isEqualTo(forecastJanuary15th11am);
        assertThat(mountainForecastXMLFile.buildAMountainForecast(2, 4, dateArray, windSpeedArray, cloudCoverArray,
                probOfPrecipArray, humidityArray, windDirectionArray, tempArray, precipAmountArray, windChillArray))
                .usingRecursiveComparison()
                .isEqualTo(forecastJanuary15th12pm);
    }

    @Test
    public void itCreatesClassInstanceOfDocumentField() throws Exception {
        this.initData();
        assertThat(mountainForecastXMLFile.getXmlDocument()).isInstanceOf(Document.class);
        assertThat(mountainForecastUrl.getXmlDocument()).isInstanceOf(Document.class);
    }

    @Test
    public void itParsesXMLDocumentIntoDateArray() throws Exception {
        this.initData();
        String[] dateArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//time-layout");
        assertThat(dateArray.length).isEqualTo(338);
        assertThat(dateArray[2]).isEqualTo("2022-01-11T16:00:00-07:00");
        assertThat(dateArray[10]).isEqualTo("2022-01-11T20:00:00-07:00");
    }

    @Test
    public void itParsesXMLDocumentIntoTemperatureArray() throws Exception {
        this.initData();
        String[] tempArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//temperature[@type='hourly']");
        assertThat(tempArray.length).isEqualTo(169);
        assertThat(tempArray[1]).isEqualTo("28");
        assertThat(tempArray[5]).isEqualTo("16");
    }

    @Test
    public void itParsesXMLDocumentIntoWindChillArray() throws Exception {
        this.initData();
        String[] windChillArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//temperature[@type='wind chill']");
        assertThat(windChillArray.length).isEqualTo(169);
        assertThat(windChillArray[1]).isEqualTo("16");
        assertThat(windChillArray[5]).isEqualTo("3");
    }

    @Test
    public void itParsesXMLDocumentIntoWindSpeedArray() throws Exception {
        this.initData();
        String[] windSpeedArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//wind-speed[@type='sustained']");
        assertThat(windSpeedArray.length).isEqualTo(169);
        assertThat(windSpeedArray[1]).isEqualTo("16");
        assertThat(windSpeedArray[5]).isEqualTo("11");
    }

    @Test
    public void itParsesXMLDocumentIntoWindDirectionArray() throws Exception {
        this.initData();
        String[] windDirectionArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//direction");
        assertThat(windDirectionArray.length).isEqualTo(169);
        assertThat(windDirectionArray[1]).isEqualTo("300");
        assertThat(windDirectionArray[5]).isEqualTo("300");
    }

    @Test
    public void itParsesXMLDocumentIntoHumidityArray() throws Exception {
        this.initData();
        String[] humidityArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//humidity");
        assertThat(humidityArray.length).isEqualTo(169);
        assertThat(humidityArray[1]).isEqualTo("29");
        assertThat(humidityArray[5]).isEqualTo("42");
    }

    @Test
    public void itParsesXMLDocumentIntoCloudCoverArray() throws Exception {
        this.initData();
        String[] cloudCoverArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//cloud-amount");
        assertThat(cloudCoverArray.length).isEqualTo(169);
        assertThat(cloudCoverArray[1]).isEqualTo("14");
        assertThat(cloudCoverArray[5]).isEqualTo("4");
    }

    @Test
    public void itParsesXMLDocumentIntoPrecipProbabilityArray() throws Exception {
        this.initData();
        String[] precipProbArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//probability-of-precipitation");
        assertThat(precipProbArray.length).isEqualTo(169);
        assertThat(precipProbArray[1]).isEqualTo("0");
        assertThat(precipProbArray[70]).isEqualTo("30");
    }

    @Test
    public void itParsesXMLDocumentIntoPrecipAmountArray() throws Exception {
        this.initData();
        String[] precipProbArray = mountainForecastXMLFile.parseElements(mountainForecastXMLFile.getXmlDocument(), "//hourly-qpf");
        assertThat(precipProbArray.length).isEqualTo(169);
        assertThat(precipProbArray[1]).isEqualTo("0");
        assertThat(precipProbArray[65]).isEqualTo("0.0033");
    }
}
