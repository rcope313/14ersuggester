package webscraper;

import models.FourteenerRoute;
import models.MountainForecast;
import org.junit.Test;
import org.w3c.dom.Document;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

public class MountainForecastScraperTest {
    FourteenerRoute elbe1;
    MountainForecastScraper mountainForecastElbe1;
    ArrayList<String> dateList, windSpeedList, cloudCoverList, probOfPrecipList, humidityList, windDirectionList,
            tempList, precipAmountList,  windChillList;

    void initData() throws Exception {

        elbe1 = new FourteenerRoute();
        elbe1.setUrl("https://www.14ers.com/route.php?route=elbe1");

        mountainForecastElbe1 = new MountainForecastScraper(elbe1);

        dateList = mountainForecastElbe1.parseElements("//time-layout", "[start-valid-time: null]");
        windSpeedList = mountainForecastElbe1.parseElements("//wind-speed[@type='sustained']", "[value: null]");
        cloudCoverList = mountainForecastElbe1.parseElements("//cloud-amount", "[value: null]");
        probOfPrecipList = mountainForecastElbe1.parseElements("//probability-of-precipitation", "[value: null]");
        humidityList = mountainForecastElbe1.parseElements("//humidity", "[value: null]");
        windDirectionList = mountainForecastElbe1.parseElements("//direction", "[value: null]");
        tempList = mountainForecastElbe1.parseElements("//temperature[@type='hourly']", "[value: null]");
        precipAmountList = mountainForecastElbe1.parseElements("//hourly-qpf", "[value: null]");
        windChillList = mountainForecastElbe1.parseElements("//temperature[@type='wind chill']", "[value: null]");
    }

    @Test
    public void itRetrievesNOAAXMLUrl() throws Exception {
        this.initData();
        assertThat(MountainForecastScraper.getNOAAXmlHourlyWeatherForecast("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&unit=0&lg=english&FcstType=graphical"))
                .isEqualTo("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&FcstType=digitalDWML");
    }

    @Test
    public void itRetrievesNOAAHourlyForecast() throws Exception {
        this.initData();
        assertThat(MountainForecastScraper.getNOAAHourlyWeatherForecast("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&lg=english&&FcstType=text"))
                .isEqualTo("https://forecast.weather.gov/MapClick.php?lat=39.2495&lon=-106.2945&unit=0&lg=english&FcstType=graphical");
    }

    @Test
    public void itRetrievesNOAASevenDayForecast() throws Exception {
        this.initData();
        assertThat(mountainForecastElbe1.getNOAASevenDayWeatherForecast()).isEqualTo("http://forecast.weather.gov/MapClick.php?lat=39.118075&lon=-106.445417");
    }

    @Test
    public void itBuildsAnArrayListOfMountainForecast() throws Exception {
        this.initData();
        var list = mountainForecastElbe1.buildMountainForecasts();
        assertThat(mountainForecastElbe1.buildMountainForecasts().size()).isEqualTo(167);
        for (MountainForecast forecast : list) {
            assertThat(forecast).isInstanceOf(MountainForecast.class);
        }
    }

    @Test
    public void itCreatesClassInstanceOfDocumentField() throws Exception {
        this.initData();
        assertThat(mountainForecastElbe1.getXmlDocument()).isInstanceOf(Document.class);
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
