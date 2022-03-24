package models;

import database.models.ImmutableMountainForecast;
import database.models.ImmutableStoredRouteAndTrailhead;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;

public class RouteForecastTest {
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
    public void itInitializesRouteAsHighConsequence() {
        assertThat(massForecast.isHighConsequence()).isTrue();
        assertThat(elbForecast.isHighConsequence()).isFalse();
    }

}
