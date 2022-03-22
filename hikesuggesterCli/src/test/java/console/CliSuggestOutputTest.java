package console;

import database.models.ImmutableMountainForecast;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

public class CliSuggestOutputTest {
    ImmutableMountainForecast
            twelveAm, oneAm, twoAm, threeAm, fourAm, fiveAm, sixAm, sevenAm, eightAm, nineAm, tenAm, elevenAm,
            twelvePm, onePm, twoPm, threePm, fourPm, fivePm, sixPm, sevenPm, eightPm, ninePm, tenPm, elevenPm;
    ArrayList<ImmutableMountainForecast> oneDayForecast;

    @Before
    public void initData() {
        twelveAm = ImmutableMountainForecast.builder()
                .date("2022-03-21T00:00:00")
                .temperature("50")
                .windChill("40")
                .windSpeed("20")
                .windDirection("300")
                .humidity("75")
                .cloudCover("25")
                .precipProbability("0")
                .precipAmount("0")
                .build();

        oneAm = twelveAm.withDate("2022-04-21T01:00:00");
        twoAm = twelveAm.withDate("2023-04-21T02:00:00");
        threeAm = twelveAm.withDate("2022-04-21T03:00:00");
        fourAm = twelveAm.withDate("2023-04-21T04:00:00");
        fiveAm = twelveAm.withDate("2022-04-21T05:00:00");
        sixAm = twelveAm.withDate("2023-04-21T06:00:00");
        sevenAm = twelveAm.withDate("2022-04-21T07:00:00");
        eightAm = twelveAm.withDate("2023-04-21T08:00:00");
        nineAm = twelveAm.withDate("2022-04-21T09:00:00");
        tenAm = twelveAm.withDate("2023-04-21T10:00:00");
        elevenAm = twelveAm.withDate("2022-04-21T11:00:00");
        twelvePm = twelveAm.withDate("2022-04-21T12:00:00");
        onePm = twelveAm.withDate("2022-04-21T13:00:00").withPrecipProbability("5");
        twoPm = twelveAm.withDate("2023-04-21T14:00:00").withPrecipProbability("5");
        threePm = twelveAm.withDate("2022-04-21T15:00:00").withPrecipProbability("5");
        fourPm = twelveAm.withDate("2023-04-21T16:00:00").withPrecipProbability("5");
        fivePm = twelveAm.withDate("2022-04-21T17:00:00").withPrecipProbability("20");
        sixPm = twelveAm.withDate("2023-04-21T18:00:00").withPrecipProbability("20");
        sevenPm = twelveAm.withDate("2022-04-21T19:00:00").withPrecipProbability("50");
        eightPm = twelveAm.withDate("2023-04-21T20:00:00").withPrecipProbability("20");
        ninePm = twelveAm.withDate("2022-04-21T21:00:00").withPrecipProbability("5");
        tenPm = twelveAm.withDate("2023-04-21T22:00:00");
        elevenPm = twelveAm.withDate("2022-04-21T23:00:00");

        oneDayForecast = new ArrayList<>(Arrays.asList(twelveAm, oneAm, twoAm, threeAm, fourAm, fiveAm, sixAm, sevenAm, eightAm,
                                                        nineAm, tenAm, elevenAm, twelvePm, onePm, twoPm, threePm, fourPm, fivePm, sixPm,
                                                        sevenPm, eightPm, ninePm, tenPm, elevenPm));
    }

    @Test
    public void itChecksPrecipProbabilityByHighConsequence() {
        ArrayList<Integer> dayTimeTwelveHourBlocksByIndex = CliSuggestOutput.getAllDayTimeTwelveHourBlocks(oneDayForecast.get(0).getDate());
        assertThat(CliSuggestOutput.checkPrecipProbabilityByHighConsequence(dayTimeTwelveHourBlocksByIndex, oneDayForecast))
                .usingRecursiveComparison()
                .isEqualTo(new ArrayList<>());
    }

    @Test
    public void itChecksPrecipProbabilityByLowConsequence() {
        ArrayList<Integer> dayTimeSixHourBlocksByIndex = CliSuggestOutput.getAllDayTimeSixHourBlocks(oneDayForecast.get(0).getDate());
        assertThat(CliSuggestOutput.checkPrecipProbabilityByLowConsequence(dayTimeSixHourBlocksByIndex, oneDayForecast))
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
