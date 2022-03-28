package console;

import database.models.ImmutableMountainForecast;
import database.models.ImmutableStoredRouteAndTrailhead;
import models.RouteForecast;
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
    ImmutableStoredRouteAndTrailhead elb, mass;
    ImmutableMountainForecast
            elb12am, elb1Am, elb2Am, elb3Am, elb4Am, elb5Am, elb6Am, elb7Am, elb8Am, elb9Am, elb10Am, elb11Am,
            elb12Pm, elb1Pm, elb2Pm, elb3Pm, elb4Pm, elb5Pm, elb6Pm, elb7Pm, elb8Pm, elb9Pm, elb10Pm, elb11Pm,
            mass12am, mass1Am, mass2Am, mass3Am, mass4Am, mass5Am, mass6Am, mass7Am, mass8Am, mass9Am, mass10Am, mass11Am,
            mass12Pm, mass1Pm, mass2Pm, mass3Pm, mass4Pm, mass5Pm, mass6Pm, mass7Pm, mass8Pm, mass9Pm, mass10Pm, mass11Pm;
    ArrayList<ImmutableMountainForecast> elbOneDayForecast, massOneDayForecast;
    RouteForecast elbForecast, massForecast;

    @Before
    public void initData() {
        mass = ImmutableStoredRouteAndTrailhead.builder()
                .mountainName("Mt. Massive")
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
                .routeId(666)
                .routeUrl("url.com")
                .routeUpdateDate("06/06/66")
                .trailheadCoordinates("666.666 666.666")
                .roadDifficulty(6)
                .roadDescription("The fiery road to hell")
                .winterAccess("There is no winter in hell")
                .trailheadCoordinates("trailhead.com")
                .trailheadUpdateDate("06/06/66")
                .build();

        elb = ImmutableStoredRouteAndTrailhead.builder()
                .mountainName("Mt. Elbert")
                .routeName("Easy Peasy Route")
                .grade(1)
                .gradeQuality("")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .startElevation(13000)
                .summitElevation(14000)
                .totalGain(1000)
                .routeLength(2)
                .rockfallPotential("Low")
                .routeFinding("Low")
                .commitment("Low")
                .exposure("Low")
                .hasMultipleRoutes(false)
                .trailhead("Heaven's Tummy")
                .routeId(7)
                .routeUrl("url.com")
                .routeUpdateDate("07/07/77")
                .trailheadCoordinates("777.77 77.77")
                .roadDifficulty(0)
                .roadDescription("Rainbow to the pearly gates")
                .winterAccess("Only sunshine in heaven")
                .trailheadCoordinates("trailhead.com")
                .trailheadUpdateDate("07/07/77")
                .build();

        elb12am = ImmutableMountainForecast.builder()
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
        elb1Am = elb12am.withDate("2022-04-21T01:00:00").withWindChill("40").withWindSpeed("0");
        elb2Am = elb12am.withDate("2023-04-21T02:00:00").withWindChill("40").withWindSpeed("0");
        elb3Am = elb12am.withDate("2022-04-21T03:00:00").withWindChill("45").withWindSpeed("0");
        elb4Am = elb12am.withDate("2023-04-21T04:00:00").withWindChill("45").withWindSpeed("0");
        elb5Am = elb12am.withDate("2022-04-21T05:00:00").withWindChill("50").withWindSpeed("0");
        elb6Am = elb12am.withDate("2023-04-21T06:00:00").withWindChill("50").withWindSpeed("0");
        elb7Am = elb12am.withDate("2022-04-21T07:00:00").withWindChill("55").withWindSpeed("5");
        elb8Am = elb12am.withDate("2023-04-21T08:00:00").withWindChill("55").withWindSpeed("5");
        elb9Am = elb12am.withDate("2022-04-21T09:00:00").withWindChill("60").withWindSpeed("5");
        elb10Am = elb12am.withDate("2023-04-21T10:00:00").withWindChill("60").withWindSpeed("10");
        elb11Am = elb12am.withDate("2022-04-21T11:00:00").withWindChill("60").withWindSpeed("10");
        elb12Pm = elb12am.withDate("2022-04-21T12:00:00").withWindChill("60").withWindSpeed("20");
        elb1Pm = elb12am.withDate("2022-04-21T13:00:00").withPrecipProbability("5").withWindChill("60").withWindSpeed("20");
        elb2Pm = elb12am.withDate("2023-04-21T14:00:00").withPrecipProbability("5").withWindChill("50").withWindSpeed("25");
        elb3Pm = elb12am.withDate("2022-04-21T15:00:00").withPrecipProbability("5").withWindChill("50").withWindSpeed("20");
        elb4Pm = elb12am.withDate("2023-04-21T16:00:00").withPrecipProbability("5").withWindChill("50").withWindSpeed("20");
        elb5Pm = elb12am.withDate("2022-04-21T17:00:00").withPrecipProbability("20").withWindChill("60").withWindSpeed("10");
        elb6Pm = elb12am.withDate("2023-04-21T18:00:00").withPrecipProbability("20").withWindChill("50").withWindSpeed("10");
        elb7Pm = elb12am.withDate("2022-04-21T19:00:00").withPrecipProbability("50").withWindChill("50").withWindSpeed("5");
        elb8Pm = elb12am.withDate("2023-04-21T20:00:00").withPrecipProbability("20").withWindChill("50").withWindSpeed("5");
        elb9Pm = elb12am.withDate("2022-04-21T21:00:00").withPrecipProbability("5").withWindChill("45").withWindSpeed("0");
        elb10Pm = elb12am.withDate("2023-04-21T22:00:00").withWindChill("45").withWindSpeed("0");
        elb11Pm = elb12am.withDate("2022-04-21T23:00:00").withWindChill("45").withWindSpeed("0");

        mass12am = ImmutableMountainForecast.builder()
                .date("2022-03-21T00:00:00")
                .temperature("50")
                .windChill("40")
                .windSpeed("0")
                .windDirection("300")
                .humidity("75")
                .cloudCover("25")
                .precipProbability("0")
                .precipAmount("0")
                .build();
        mass1Am = mass12am.withDate("2022-04-21T01:00:00").withPrecipProbability("5").withWindChill("40").withWindSpeed("0");
        mass2Am = mass12am.withDate("2023-04-21T02:00:00").withPrecipProbability("5").withWindChill("40").withWindSpeed("0");
        mass3Am = mass12am.withDate("2022-04-21T03:00:00").withPrecipProbability("5").withWindChill("45").withWindSpeed("10");
        mass4Am = mass12am.withDate("2023-04-21T04:00:00").withPrecipProbability("5").withWindChill("45").withWindSpeed("20");
        mass5Am = mass12am.withDate("2022-04-21T05:00:00").withPrecipProbability("5").withWindChill("50").withWindSpeed("20");
        mass6Am = mass12am.withDate("2023-04-21T06:00:00").withWindChill("50").withWindSpeed("10");
        mass7Am = mass12am.withDate("2022-04-21T07:00:00").withWindChill("55").withWindSpeed("5");
        mass8Am = mass12am.withDate("2023-04-21T08:00:00").withWindChill("55").withWindSpeed("5");
        mass9Am = mass12am.withDate("2022-04-21T09:00:00").withWindChill("60").withWindSpeed("5");
        mass10Am = mass12am.withDate("2023-04-21T10:00:00").withWindChill("60").withWindSpeed("0");
        mass11Am = mass12am.withDate("2022-04-21T11:00:00").withWindChill("60").withWindSpeed("0");
        mass12Pm = mass12am.withDate("2022-04-21T12:00:00").withWindChill("60").withWindSpeed("0");
        mass1Pm = mass12am.withDate("2022-04-21T13:00:00").withWindChill("60").withWindSpeed("0");
        mass2Pm = mass12am.withDate("2023-04-21T14:00:00").withWindChill("50").withWindSpeed("0");
        mass3Pm = mass12am.withDate("2022-04-21T15:00:00").withWindChill("50").withWindSpeed("0");
        mass4Pm = mass12am.withDate("2023-04-21T16:00:00").withWindChill("50").withWindSpeed("0");
        mass5Pm = mass12am.withDate("2022-04-21T17:00:00").withWindChill("60").withWindSpeed("0");
        mass6Pm = mass12am.withDate("2023-04-21T18:00:00").withWindChill("50").withWindSpeed("5");
        mass7Pm = mass12am.withDate("2022-04-21T19:00:00").withWindChill("50").withWindSpeed("5");
        mass8Pm = mass12am.withDate("2023-04-21T20:00:00").withWindChill("50").withWindSpeed("5");
        mass9Pm = mass12am.withDate("2022-04-21T21:00:00").withWindChill("45").withWindSpeed("0");
        mass10Pm = mass12am.withDate("2023-04-21T22:00:00").withWindChill("45").withWindSpeed("0");
        mass11Pm = mass12am.withDate("2022-04-21T23:00:00").withWindChill("45").withWindSpeed("0");

        elbOneDayForecast = new ArrayList<>(Arrays.asList(elb12am, elb1Am, elb2Am, elb3Am, elb4Am, elb5Am, elb6Am, elb7Am, elb8Am,
                                                        elb9Am, elb10Am, elb11Am, elb12Pm, elb1Pm, elb2Pm, elb3Pm, elb4Pm, elb5Pm, elb6Pm,
                                                        elb7Pm, elb8Pm, elb9Pm, elb10Pm, elb11Pm));

        massOneDayForecast = new ArrayList<>(Arrays.asList(mass12am, mass1Am, mass2Am, mass3Am, mass4Am, mass5Am, mass6Am, mass7Am, mass8Am,
                mass9Am, mass10Am, mass11Am, mass12Pm, mass1Pm, mass2Pm, mass3Pm, mass4Pm, mass5Pm, mass6Pm,
                mass7Pm, mass8Pm, mass9Pm, mass10Pm, mass11Pm));

        elbForecast = new RouteForecast(elb, elbOneDayForecast);
        massForecast = new RouteForecast(mass, massOneDayForecast);
    }

    @Test
    public void itBuildsCliTable() {

    }

    @Test
    public void itBuildsCliTableWeatherInputData() {
        List<RouteForecast> forecasts1 = new ArrayList<>(Arrays.asList(elbForecast, massForecast));
        List<TimeScore> bestTimes = CliSuggestOutput.getBestTimesOfAllRoutes(forecasts1);
        CliSuggestOutput.buildCliTableWeatherInputData(bestTimes);
    }

    @Test
    public void itGetsBestTimesOfAllRoutes() {
        TimeScore timeScoreMass6am = new TimeScore(massForecast, 6, 40);
        TimeScore timeScoreElb5am = new TimeScore(elbForecast, 5, 40);
        TimeScore timeScoreElb6am = new TimeScore(elbForecast, 6, 40);
        TimeScore timeScoreElb7am = new TimeScore(elbForecast, 7, 20);
        List<RouteForecast> forecasts1 = new ArrayList<>(Arrays.asList(elbForecast, massForecast));
        assertThat(CliSuggestOutput.getBestTimesOfAllRoutes(forecasts1))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>(Arrays.asList(timeScoreElb5am, timeScoreElb6am, timeScoreMass6am, timeScoreElb7am)));
    }

    @Test
    public void itGetsBestThreeTimesOfOneRouteHighConsequence() {
        assertThat(CliSuggestOutput.getBestThreeTimesOfOneRouteHighConsequence(mass, massOneDayForecast))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>(List.of(new TimeScore(massForecast, 6, 40))));
    }

    @Test
    public void itGetsBestThreeTimesOfOneRouteLowConsequence() {
        TimeScore timeScore5am = new TimeScore(elbForecast, 5, 40);
        TimeScore timeScore6am = new TimeScore(elbForecast, 6, 40);
        TimeScore timeScore7am = new TimeScore(elbForecast, 7, 20);
        assertThat(CliSuggestOutput.getBestThreeTimesOfOneRouteLowConsequence(elb, elbOneDayForecast))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>(Arrays.asList(timeScore5am, timeScore6am, timeScore7am)));
    }

    @Test
    public void itTransformsSortedIndexesToListOfBestTimes() {
        ArrayList<Integer> indexesLow = new ArrayList<>(Arrays.asList(5,9,7,6,8));
        ArrayList<Map.Entry<Integer, Integer>> entries = CliSuggestOutput.sortLowConsequenceEntriesByWindSpeedAndWindChill(indexesLow, elbOneDayForecast);
        List<TimeScore> bestTimesList = CliSuggestOutput.transformSortedEntriesToListOfBestTimes(entries, elbOneDayForecast, elb);
        assertThat(bestTimesList.get(0)).usingRecursiveComparison().isEqualTo(new TimeScore(elbForecast,5, 40));
        assertThat(bestTimesList.get(1)).usingRecursiveComparison().isEqualTo(new TimeScore(elbForecast, 6, 40));
        assertThat(bestTimesList.get(2)).usingRecursiveComparison().isEqualTo(new TimeScore(elbForecast, 7, 20));
        assertThat(bestTimesList.get(3)).usingRecursiveComparison().isEqualTo(new TimeScore(elbForecast, 8, 20));
        assertThat(bestTimesList.get(4)).usingRecursiveComparison().isEqualTo(new TimeScore(elbForecast, 9, 10));
    }

    @Test
    public void itGetsMaxWindChill() {
        assertThat(CliSuggestOutput.getMaxWindChill(5,11, elbOneDayForecast)).isEqualTo(60);
        assertThat(CliSuggestOutput.getMaxWindChill(0,6, elbOneDayForecast)).isEqualTo(50);
        assertThat(CliSuggestOutput.getMaxWindChill(1,13, elbOneDayForecast)).isEqualTo(60);
    }

    @Test
    public void itGetsMaxWindSpeed() {
        assertThat(CliSuggestOutput.getMaxWindSpeed(5,11, elbOneDayForecast)).isEqualTo(10);
        assertThat(CliSuggestOutput.getMaxWindSpeed(1,13, elbOneDayForecast)).isEqualTo(20);
    }

    @Test
    public void itGetsListOfIndexesByHighConsequence() {
        ArrayList<Integer> dayTimeTwelveHourBlocksByIndex = CliSuggestOutput.getAllDayTimeTwelveHourBlocks(elbOneDayForecast.get(0).getDate());
        assertThat(CliSuggestOutput.getListOfIndexesByHighConsequence(dayTimeTwelveHourBlocksByIndex, elbOneDayForecast))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>());
    }

    @Test
    public void itGetsListOfIndexesByLowConsequence() {
        ArrayList<Integer> dayTimeSixHourBlocksByIndex = CliSuggestOutput.getAllDayTimeSixHourBlocks(elbOneDayForecast.get(0).getDate());
        assertThat(CliSuggestOutput.getListOfIndexesByLowConsequence(dayTimeSixHourBlocksByIndex, elbOneDayForecast))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>(Arrays.asList(5,6,7,8,9)));
    }

    @Test
    public void itChecksPrecipProbabilityByLowConsequenceHelper() {
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(elbOneDayForecast, 0, 5)).isTrue();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(elbOneDayForecast, 9, 14)).isTrue();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(elbOneDayForecast, 10, 15)).isFalse();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(elbOneDayForecast, 11, 16)).isFalse();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(elbOneDayForecast, 17, 22)).isFalse();
    }

    @Test
    public void itChecksPrecipProbabilityByHighConsequenceHelper() {
        assertThat(CliSuggestOutput.checkPrecipProbabilityByHighConsequence(elbOneDayForecast, 0, 11)).isTrue();
        assertThat(CliSuggestOutput.checkPrecipProbabilityByHighConsequence(elbOneDayForecast, 5, 16)).isFalse();
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
