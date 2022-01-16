package webscraper;

import models.MountainForecast;
import org.junit.Test;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public class MountainForecastScraperTest {
    MountainForecastScraper mountainForecastUrl;
    ArrayList<String> dateList, windSpeedList, cloudCoverList, probOfPrecipList, humidityList, windDirectionList,
            tempList, precipAmountList,  windChillList;

    void initData() throws Exception {

        mountainForecastUrl = new MountainForecastScraper(new URL("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&FcstType=digitalDWML"));

        dateList = mountainForecastUrl.parseElements("//time-layout", "[start-valid-time: null]");
        windSpeedList = mountainForecastUrl.parseElements("//wind-speed[@type='sustained']", "[value: null]");
        cloudCoverList = mountainForecastUrl.parseElements("//cloud-amount", "[value: null]");
        probOfPrecipList = mountainForecastUrl.parseElements("//probability-of-precipitation", "[value: null]");
        humidityList = mountainForecastUrl.parseElements("//humidity", "[value: null]");
        windDirectionList = mountainForecastUrl.parseElements("//direction", "[value: null]");
        tempList = mountainForecastUrl.parseElements("//temperature[@type='hourly']", "[value: null]");
        precipAmountList = mountainForecastUrl.parseElements("//hourly-qpf", "[value: null]");
        windChillList = mountainForecastUrl.parseElements("//temperature[@type='wind chill']", "[value: null]");
    }

    @Test
    public void itBuildsAnArrayListOfMountainForecast() throws Exception {
        this.initData();
        var list = mountainForecastUrl.buildMountainForecasts();
        assertThat(mountainForecastUrl.buildMountainForecasts().size()).isEqualTo(167);
        for (MountainForecast forecast : list) {
            assertThat(forecast).isInstanceOf(MountainForecast.class);
        }
    }

    @Test
    public void itCreatesClassInstanceOfDocumentField() throws Exception {
        this.initData();
        assertThat(mountainForecastUrl.getXmlDocument()).isInstanceOf(Document.class);
    }

    @Test
    public void itParsesXMLDocumentIntoDateList() throws Exception {
        this.initData();
        assertThat(dateList.size()).isEqualTo(168);
        assertThat(dateList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoTemperatureList() throws Exception {
        this.initData();
        assertThat(tempList.size()).isEqualTo(167);
        assertThat(tempList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoWindChillList() throws Exception {
        this.initData();
        assertThat(windChillList.size()).isEqualTo(167);
        assertThat(windChillList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoWindSpeedList() throws Exception {
        this.initData();
        assertThat(windSpeedList.size()).isEqualTo(167);
        assertThat(windSpeedList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoWindDirectionList() throws Exception {
        this.initData();
        assertThat(windDirectionList.size()).isEqualTo(167);
        assertThat(windDirectionList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoHumidityList() throws Exception {
        this.initData();
        assertThat(humidityList.size()).isEqualTo(167);
        assertThat(humidityList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoCloudCoverList() throws Exception {
        this.initData();
        assertThat(cloudCoverList.size()).isEqualTo(167);
        assertThat(cloudCoverList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoPrecipProbabilityList() throws Exception {
        this.initData();
        assertThat(probOfPrecipList.size()).isEqualTo(167);
        assertThat(probOfPrecipList.get(5)).isInstanceOf(String.class);
    }

    @Test
    public void itParsesXMLDocumentIntoPrecipAmountList() throws Exception {
        this.initData();
        assertThat(precipAmountList.size()).isEqualTo(167);
        assertThat(precipAmountList.get(5)).isInstanceOf(String.class);
    }
}
