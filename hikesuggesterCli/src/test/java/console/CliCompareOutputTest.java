package console;

import database.dao.RoutesTrailheadsDao;
import database.models.CompareQuery;
import database.models.ImmutableStoredRouteAndTrailhead;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CliCompareOutputTest {
    @Mock private RoutesTrailheadsDao dbMock1, dbMock2;
    ImmutableStoredRouteAndTrailhead elb, mass;
    CompareQuery query = new CompareQuery();
    String differenceString;

    @Before
    public void initData() {
        differenceString =
                "Standard Route: false, true\n" +
                "Grade: 5, 1\n" +
                "Start Elevation: 0, 13000\n" +
                "Total Gain: 14000, 1000\n" +
                "Route Length: 3.0, 2.0\n" +
                "Exposure: Extreme, Low\n" +
                "Rockfall Potential: Extreme, Low\n" +
                "Route Finding: Extreme, Low\n" +
                "Commitment: Extreme, Low\n" +
                "Trailhead: Devil's Gates, \nHeaven's Tummy\n" +
                "Road Difficulty: 6, 0\n" +
                "Road Description: The fiery road to hell, \nRainbow to the pearly gates\n" +
                "Winter Access: There is no winter in hell, \nOnly sunshine in heaven";

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
                .trailheadUrl("trailhead.com")
                .trailheadUpdateDate("07/07/77")
                .build();
    }

    @Test
    public void itHandlesErrorOnBuildCliTable() {
        when(dbMock2.get(query)).thenReturn(new ArrayList<>(List.of(elb)));
        CliCompareOutput output = new CliCompareOutput(dbMock2);
        assertThatThrownBy(() -> output.buildCliTable(query)).hasMessage("Query did not yield two results. Please use route urls as parameters.");
    }

    @Test
    public void itBuildsCliTable() {
        when(dbMock1.get(query)).thenReturn(new ArrayList<>(Arrays.asList(elb, mass)));
        CliCompareOutput output = new CliCompareOutput(dbMock1);
        output.buildCliTable(query);
    }

    @Test
    public void itCreatesDifferenceString() {
        assertThat(CliCompareOutput.createDifferenceString(mass, elb)).isEqualTo(differenceString);
    }
}
