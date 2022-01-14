package webscraper;

import models.MountainForecast;
import org.junit.Test;
import org.w3c.dom.Document;
import static org.assertj.core.api.Assertions.*;

public class MountainForecastScraperTest {
    MountainForecast forecastJanuary11th4pm, forecastJanuary11th5pm;
    MountainForecastScraper mountainForecastXMLFile;

    void initData() throws Exception {
        forecastJanuary11th4pm =
                new MountainForecast(
                        "2022-01-11T16:00:00-07:00", 28, 16, 16, 300,
                        29, 14, 0, 0);

        forecastJanuary11th5pm =
                new MountainForecast(
                        "2022-01-11T17:00:00-07:00", 23, 11, 13, 290,
                        36, 5, 0, 0);

        mountainForecastXMLFile = new MountainForecastScraper("/Users/rachelcope/Documents/hikesuggester/hikesuggesterWebScraper/src/main/resources/mountainforecast.xml");
    }

    @Test
    public void itCreatesClassInstanceOfDocumentField() throws Exception {
        this.initData();
        assertThat(mountainForecastXMLFile.getXmlDocument()).isInstanceOf(Document.class);
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
