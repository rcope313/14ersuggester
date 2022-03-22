package console;

import database.models.ImmutableMountainForecast;
import database.models.ImmutableStoredRoute;
import models.TimeScore;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CliSuggestOutputTest {
    ImmutableStoredRoute elb1;
    ImmutableMountainForecast
            twelveAm, oneAm, twoAm, threeAm, fourAm, fiveAm, sixAm, sevenAm, eightAm, nineAm, tenAm, elevenAm,
            twelvePm, onePm, twoPm, threePm, fourPm, fivePm, sixPm, sevenPm, eightPm, ninePm, tenPm, elevenPm;
    ArrayList<ImmutableMountainForecast> oneDayForecast;

    @Before
    public void initData() {
        elb1 = ImmutableStoredRoute.builder()
                .mountainName("Mt. Elbert")
                .routeName("Super Scary Route")
                .grade(5)
                .gradeQuality("")
                .isSnowRoute(false)
                .isStandardRoute(false)
                .startElevation(0)
                .summitElevation(14000)
                .totalGain(14000)
                .routeLength(3)
                .rockfallPotential("Extreme")
                .routeFinding("Extreme")
                .commitment("Extreme")
                .exposure("Extreme")
                .hasMultipleRoutes(false)
                .trailhead("Devil's Gates")
                .id(666)
                .url("url.com")
                .updateDate("06/06/66")
                .build();

        twelveAm = ImmutableMountainForecast.builder()
                .date("2022-03-21T00:00:00")
                .temperature("50")
                .windChill("40")
                .windSpeed("5")
                .windDirection("300")
                .humidity("75")
                .cloudCover("25")
                .precipProbability("0")
                .precipAmount("0")
                .build();

        oneAm = twelveAm.withDate("2022-04-21T01:00:00").withWindChill("40").withWindSpeed("0");
        twoAm = twelveAm.withDate("2023-04-21T02:00:00").withWindChill("40").withWindSpeed("0");
        threeAm = twelveAm.withDate("2022-04-21T03:00:00").withWindChill("45").withWindSpeed("0");
        fourAm = twelveAm.withDate("2023-04-21T04:00:00").withWindChill("45").withWindSpeed("0");
        fiveAm = twelveAm.withDate("2022-04-21T05:00:00").withWindChill("50").withWindSpeed("0");
        sixAm = twelveAm.withDate("2023-04-21T06:00:00").withWindChill("50").withWindSpeed("0");
        sevenAm = twelveAm.withDate("2022-04-21T07:00:00").withWindChill("55").withWindSpeed("5");
        eightAm = twelveAm.withDate("2023-04-21T08:00:00").withWindChill("55").withWindSpeed("5");
        nineAm = twelveAm.withDate("2022-04-21T09:00:00").withWindChill("60").withWindSpeed("5");
        tenAm = twelveAm.withDate("2023-04-21T10:00:00").withWindChill("60").withWindSpeed("10");
        elevenAm = twelveAm.withDate("2022-04-21T11:00:00").withWindChill("60").withWindSpeed("10");
        twelvePm = twelveAm.withDate("2022-04-21T12:00:00").withWindChill("60").withWindSpeed("20");
        onePm = twelveAm.withDate("2022-04-21T13:00:00").withPrecipProbability("5").withWindChill("60").withWindSpeed("20");
        twoPm = twelveAm.withDate("2023-04-21T14:00:00").withPrecipProbability("5").withWindChill("50").withWindSpeed("25");
        threePm = twelveAm.withDate("2022-04-21T15:00:00").withPrecipProbability("5").withWindChill("50").withWindSpeed("20");
        fourPm = twelveAm.withDate("2023-04-21T16:00:00").withPrecipProbability("5").withWindChill("50").withWindSpeed("20");
        fivePm = twelveAm.withDate("2022-04-21T17:00:00").withPrecipProbability("20").withWindChill("60").withWindSpeed("10");
        sixPm = twelveAm.withDate("2023-04-21T18:00:00").withPrecipProbability("20").withWindChill("50").withWindSpeed("10");
        sevenPm = twelveAm.withDate("2022-04-21T19:00:00").withPrecipProbability("50").withWindChill("50").withWindSpeed("5");
        eightPm = twelveAm.withDate("2023-04-21T20:00:00").withPrecipProbability("20").withWindChill("50").withWindSpeed("5");
        ninePm = twelveAm.withDate("2022-04-21T21:00:00").withPrecipProbability("5").withWindChill("45").withWindSpeed("0");
        tenPm = twelveAm.withDate("2023-04-21T22:00:00").withWindChill("45").withWindSpeed("0");
        elevenPm = twelveAm.withDate("2022-04-21T23:00:00").withWindChill("45").withWindSpeed("0");

        oneDayForecast = new ArrayList<>(Arrays.asList(twelveAm, oneAm, twoAm, threeAm, fourAm, fiveAm, sixAm, sevenAm, eightAm,
                                                        nineAm, tenAm, elevenAm, twelvePm, onePm, twoPm, threePm, fourPm, fivePm, sixPm,
                                                        sevenPm, eightPm, ninePm, tenPm, elevenPm));
    }

    @Test
    public void itTransformsSortedIndexesToListOfBestTimes() {
        ArrayList<Integer> indexesLow = new ArrayList<>(Arrays.asList(5,9,7,6,8));
        ArrayList<Map.Entry<Integer, Integer>> entries = CliSuggestOutput.sortLowConsequenceEntriesByWindSpeedAndWindChill(indexesLow, oneDayForecast);
        List<TimeScore> bestTimesList = CliSuggestOutput.transformSortedEntriesToListOfBestTimes(entries, elb1);
        assertThat(bestTimesList.get(0)).usingRecursiveComparison().isEqualTo(new TimeScore(elb1, 5, 40));
        assertThat(bestTimesList.get(1)).usingRecursiveComparison().isEqualTo(new TimeScore(elb1, 6, 40));
        assertThat(bestTimesList.get(2)).usingRecursiveComparison().isEqualTo(new TimeScore(elb1, 7, 20));
        assertThat(bestTimesList.get(3)).usingRecursiveComparison().isEqualTo(new TimeScore(elb1, 8, 20));
        assertThat(bestTimesList.get(4)).usingRecursiveComparison().isEqualTo(new TimeScore(elb1, 9, 10));
    }

    @Test
    public void itGetsMaxWindChill() {
        assertThat(CliSuggestOutput.getMaxWindChill(5,11, oneDayForecast)).isEqualTo(60);
        assertThat(CliSuggestOutput.getMaxWindChill(0,6, oneDayForecast)).isEqualTo(50);
        assertThat(CliSuggestOutput.getMaxWindChill(1,13, oneDayForecast)).isEqualTo(60);
    }

    @Test
    public void itGetsMaxWindSpeed() {
        assertThat(CliSuggestOutput.getMaxWindSpeed(5,11, oneDayForecast)).isEqualTo(10);
        assertThat(CliSuggestOutput.getMaxWindSpeed(1,13, oneDayForecast)).isEqualTo(20);
    }

    @Test
    public void itGetsListOfIndexesByHighConsequence() {
        ArrayList<Integer> dayTimeTwelveHourBlocksByIndex = CliSuggestOutput.getAllDayTimeTwelveHourBlocks(oneDayForecast.get(0).getDate());
        assertThat(CliSuggestOutput.getListOfIndexesByHighConsequence(dayTimeTwelveHourBlocksByIndex, oneDayForecast))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>());
    }

    @Test
    public void itGetsListOfIndexesByLowConsequence() {
        ArrayList<Integer> dayTimeSixHourBlocksByIndex = CliSuggestOutput.getAllDayTimeSixHourBlocks(oneDayForecast.get(0).getDate());
        assertThat(CliSuggestOutput.getListOfIndexesByLowConsequence(dayTimeSixHourBlocksByIndex, oneDayForecast))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>(Arrays.asList(5,6,7,8,9)));
    }

    @Test
    public void itChecksPrecipProbabilityByLowConsequenceHelper() {
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(oneDayForecast, 0, 5)).isTrue();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(oneDayForecast, 9, 14)).isTrue();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(oneDayForecast, 10, 15)).isFalse();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(oneDayForecast, 11, 16)).isFalse();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(oneDayForecast, 17, 22)).isFalse();
    }

    @Test
    public void itChecksPrecipProbabilityByHighConsequenceHelper() {
        assertThat(CliSuggestOutput.checkPrecipProbabilityByHighConsequence(oneDayForecast, 0, 11)).isTrue();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByHighConsequence(oneDayForecast, 5, 16)).isFalse();
    }

    @Test
    public void itGetsAllDayTimeSixHourBlocks() {
        assertThat(CliSuggestOutput.getAllDayTimeSixHourBlocks("2022-04-21T01:00:00"))
                .isEqualTo(new ArrayList<>(Arrays.asList(4,5,6,7,8,9,28,29,30,31,32,33,52,53,54,55,56,57,76,77,78,79,80,81,100,101,102,103,104,105,124,125,126,127,128,129)));
        assertThat(CliSuggestOutput.getAllDayTimeSixHourBlocks("2022-04-21T23:00:00"))
                .isEqualTo(new ArrayList<>(Arrays.asList(6,7,8,9,10,11,30,31,32,33,34,35,54,55,56,57,58,59,78,79,80,81,82,83,102,103,104,105,106,107,126,127,128,129,130,131)));
    }

    @Test
    public void itGetsAllDayTimeTwelveHourBlocks() {
        assertThat(CliSuggestOutput.getAllDayTimeTwelveHourBlocks("2022-04-21T01:00:00"))
                .isEqualTo(new ArrayList<>(Arrays.asList(3,4,5,27,28,29,51,52,53,75,76,77,99,100,101,123,124,125)));
        assertThat(CliSuggestOutput.getAllDayTimeTwelveHourBlocks("2022-04-21T18:00:00"))
                .isEqualTo(new ArrayList<>(Arrays.asList(10,11,12,34,35,36,58,59,60,82,83,84,106,107,108,130,131,132)));
    }

    @Test
    public void itParsesStrToLocalDateTime() {
        LocalDateTime resultDate = LocalDateTime.of(2022, Month.APRIL, 21, 1, 0, 0);
        assertThat(CliSuggestOutput.parseStrToLocalDateTime("2022-04-21T01:00:00")).isEqualTo(resultDate);
    }
}
