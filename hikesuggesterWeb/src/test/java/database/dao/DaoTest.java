package database.dao;

import database.models.CompareQuery;
import database.models.ImmutableFetchedRoute;
import database.models.ImmutableFetchedTrailhead;
import database.models.ImmutableStoredRoute;
import database.models.ImmutableStoredRouteAndTrailhead;
import database.models.ImmutableStoredTrailhead;
import database.models.SearchQuery;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DaoTest {
    static Connection conn;
    SearchQuery sq0, sq1, sq2, sq3, sq4, sq5, sq6, sq7, sq8;
    CompareQuery cq0, cq1, cq2, cq3, cq4, cq5;
    ImmutableFetchedRoute elbFetched1, graysFetched, elbFetched2;
    ImmutableStoredRoute elbStored1, graysStored, elbStored2;
    ImmutableFetchedTrailhead elbNorthFetched, argentineFetched;
    ImmutableStoredTrailhead elbNorthStored, argentineStored;
    ImmutableStoredRouteAndTrailhead elbAndElbNorth, graysAndArgentine, elbAndElbSouth;

    @BeforeClass
    public static void getConnection() throws SQLException {
        conn = DriverManager.getConnection("jdbc:h2:mem:myDb", "sa", "sa");
        Statement stmt = conn.createStatement();
        String createFourteenerRoutesTableSql =
                "CREATE TABLE fourteener_routes "
                        + "(fourteenerRouteId IDENTITY NOT NULL PRIMARY KEY,\n"
                        + "routeName varchar(225),\n"
                        + "mountainName varchar(225),\n"
                        + "isSnowRoute tinyint,\n"
                        + "isStandardRoute tinyint,\n"
                        + "grade int,\n"
                        + "gradeQuality varchar(45),\n"
                        + "startElevation int,\n"
                        + "summitElevation int,\n"
                        + "totalGain int,\n"
                        + "routeLength double,\n"
                        + "exposure varchar(45),\n"
                        + "rockfallPotential varchar(45),\n"
                        + "routeFinding varchar(45),\n"
                        + "commitment varchar(45),\n"
                        + "hasMultipleRoutes tinyint,\n"
                        + "url varchar(225),\n"
                        + "trailhead varchar(225),\n"
                        + "updateDate date)";
        String createTrailheadsTableSql =
                "CREATE TABLE trailheads "
                        + "(trailheadId IDENTITY NOT NULL PRIMARY KEY,\n"
                        + "name varchar(225),\n"
                        + "coordinates varchar(225),\n"
                        + "roadDifficulty int,\n"
                        + "roadDescription varchar(225),\n"
                        + "winterAccess varchar(225),\n"
                        + "url varchar(225),\n"
                        + "updateDate date)";

        stmt.executeUpdate(createFourteenerRoutesTableSql);
        stmt.executeUpdate(createTrailheadsTableSql);
    }

    @Before
    public void initData() {
        sq0 = new SearchQuery();
        sq1 = new SearchQuery();
            sq1.setMountainNames(new ArrayList<>(List.of("Mt. Elbert")));
        sq2 = new SearchQuery();
            sq2.setMountainNames(new ArrayList<>(List.of("Mt. Washington")));
        sq3 = new SearchQuery();
            sq3.setStandardRoute(true);
            sq3.setTotalGain(2000);
            sq3.setRouteLength(4.5);
        sq4 = new SearchQuery();
            sq4.setTrailheads (new ArrayList<>(Arrays.asList("Mt. Elbert North Trailhead", "Mt. Elbert South Trailhead")));
        sq5 = new SearchQuery();
            sq5.setTrailheads (new ArrayList<>(List.of("Mt. Elbert North Trailhead")));
        sq6 = new SearchQuery();
            sq6.setStartElevation(11100);
            sq6.setSummitElevation(14000);
        sq7 = new SearchQuery();
            sq7.setExposure("low");
            sq7.setRouteFinding("low");
            sq7.setRockfallPotential("low");
            sq7.setCommitment("low");
        sq8 = new SearchQuery();
            sq8.setRoadDifficulties(new ArrayList<>(Arrays.asList(1,2,3)));

        cq0 = new CompareQuery();
        cq1 = new CompareQuery();
            cq1.setMountainName1("Mt. Elbert");
        cq2 = new CompareQuery();
            cq2.setMountainName1("Grays Peak");
            cq2.setMountainName2("Mt. Washington");
            cq2.setRouteName1("Northeast Ridge");
            cq2.setRouteName2("East Ridge");
        cq3 = new CompareQuery();
            cq3.setMountainName1("Mt. Elbert");
            cq3.setMountainName2("Grays Peak");
            cq3.setRouteName1("Northeast Ridge");
            cq3.setRouteName2("North Slopes");
        cq4 = new CompareQuery();
            cq4.setRouteUrls(new ArrayList<>(Arrays.asList("https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=gray1")));
        cq5 = new CompareQuery();
            cq5.setMountainName1("Mt. Elbert");
            cq5.setMountainName2("Grays Peak");
            cq5.setRouteName1("East Ridge");
            cq5.setRouteName2("North Slope");
            cq5.setRouteUrls(new ArrayList<>(Arrays.asList("https://www.14ers.com/route.php?route=elbe1", "https://www.14ers.com/route.php?route=gray1")));

        elbFetched1 = ImmutableFetchedRoute.builder()
                .routeName("Northeast Ridge")
                .mountainName("Mt. Elbert")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .grade(1)
                .gradeQuality("")
                .startElevation(10040)
                .summitElevation(14433)
                .totalGain(4700)
                .routeLength(9.5)
                .exposure("low")
                .rockfallPotential("low")
                .routeFinding("low")
                .commitment("low")
                .hasMultipleRoutes(false)
                .url("https://www.14ers.com/route.php?route=elbe1")
                .trailhead("Mt. Elbert North Trailhead")
                .build();

        elbStored1 = ImmutableStoredRoute.builder()
                .id(1)
                .routeName("Northeast Ridge")
                .mountainName("Mt. Elbert")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .grade(1)
                .gradeQuality("")
                .startElevation(10040)
                .summitElevation(14433)
                .totalGain(4700)
                .routeLength(9.5)
                .exposure("low")
                .rockfallPotential("low")
                .routeFinding("low")
                .commitment("low")
                .hasMultipleRoutes(false)
                .url("https://www.14ers.com/route.php?route=elbe1")
                .trailhead("Mt. Elbert North Trailhead")
                .updateDate(LocalDate.now().toString())
                .build();

        elbFetched2 = elbFetched1
                .withRouteName("East Ridge")
                .withUrl("https://www.14ers.com/route.php?route=elbe2")
                .withTrailhead("Mt. Elbert South Trailhead");
        elbStored2 = elbStored1
                .withRouteName("East Ridge")
                .withUrl("https://www.14ers.com/route.php?route=elbe2")
                .withTrailhead("Mt. Elbert South Trailhead")
                .withId(3);

        graysFetched = ImmutableFetchedRoute.builder()
                .routeName("North Slopes")
                .mountainName("Grays Peak")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .grade(1)
                .gradeQuality("")
                .startElevation(11280)
                .summitElevation(14270)
                .totalGain(3000)
                .routeLength(7.5)
                .exposure("low")
                .rockfallPotential("low")
                .routeFinding("low")
                .commitment("low")
                .hasMultipleRoutes(false)
                .url("https://www.14ers.com/route.php?route=gray1")
                .trailhead("Argentine Pass Trailhead")
                .build();

        graysStored = ImmutableStoredRoute.builder()
                .id(2)
                .routeName("North Slopes")
                .mountainName("Grays Peak")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .grade(1)
                .gradeQuality("")
                .startElevation(11280)
                .summitElevation(14270)
                .totalGain(3000)
                .routeLength(7.5)
                .exposure("low")
                .rockfallPotential("low")
                .routeFinding("low")
                .commitment("low")
                .hasMultipleRoutes(false)
                .url("https://www.14ers.com/route.php?route=gray1")
                .trailhead("Argentine Pass Trailhead")
                .updateDate("2022-01-01")
                .build();

        elbNorthFetched = ImmutableFetchedTrailhead.builder()
                .name("Mt. Elbert North Trailhead")
                .coordinates("39.15216, -106.41235")
                .roadDifficulty(1)
                .roadDescription("2WD easy dirt road all the way to the TH")
                .winterAccess("Usually closed just a short distance up from where the road changes to dirt - approx. 5 miles below the TH.\n")
                .url("https://www.14ers.com/php14ers/trailheadsview.php?thparm=sw01")
                .build();

        elbNorthStored = ImmutableStoredTrailhead.builder()
                .id(1)
                .name("Mt. Elbert North Trailhead")
                .coordinates("39.15216, -106.41235")
                .roadDifficulty(1)
                .roadDescription("2WD easy dirt road all the way to the TH")
                .winterAccess("Usually closed just a short distance up from where the road changes to dirt - approx. 5 miles below the TH.\n")
                .url("https://www.14ers.com/php14ers/trailheadsview.php?thparm=sw01")
                .updateDate(LocalDate.now().toString())
                .build();

        argentineFetched = ImmutableFetchedTrailhead.builder()
                .name("Argentine Pass Trailhead")
                .coordinates("39.60866, -105.79999")
                .roadDifficulty(3)
                .roadDescription("Easy 4WD (good-clearance recommended) up to the gate closure near Shoe Basin mine.")
                .winterAccess("The Peru Creek (260) road is closed at the start.")
                .url("https://www.14ers.com/php14ers/trailheadsview.php?thparm=fr07")
                .build();

        argentineStored = ImmutableStoredTrailhead.builder()
                .id(2)
                .name("Argentine Pass Trailhead")
                .coordinates("39.60866, -105.79999")
                .roadDifficulty(3)
                .roadDescription("Easy 4WD (good-clearance recommended) up to the gate closure near Shoe Basin mine.")
                .winterAccess("The Peru Creek (260) road is closed at the start.")
                .url("https://www.14ers.com/php14ers/trailheadsview.php?thparm=fr07")
                .updateDate("2022-01-01")
                .build();

        elbAndElbNorth = ImmutableStoredRouteAndTrailhead.builder()
                .routeId(1)
                .trailheadId(1)
                .routeName("Northeast Ridge")
                .mountainName("Mt. Elbert")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .grade(1)
                .gradeQuality("")
                .startElevation(10040)
                .summitElevation(14433)
                .totalGain(4700)
                .routeLength(9.5)
                .exposure("low")
                .rockfallPotential("low")
                .routeFinding("low")
                .commitment("low")
                .hasMultipleRoutes(false)
                .routeUrl("https://www.14ers.com/route.php?route=elbe1")
                .trailhead("Mt. Elbert North Trailhead")
                .trailheadCoordinates("39.15216, -106.41235")
                .roadDifficulty(1)
                .roadDescription("2WD easy dirt road all the way to the TH")
                .winterAccess("Usually closed just a short distance up from where the road changes to dirt - approx. 5 miles below the TH.\n")
                .trailheadUrl("https://www.14ers.com/php14ers/trailheadsview.php?thparm=sw01")
                .trailheadUpdateDate(LocalDate.now().toString())
                .routeUpdateDate(LocalDate.now().toString())
                .build();

        graysAndArgentine = ImmutableStoredRouteAndTrailhead.builder()
                .routeId(2)
                .trailheadId(2)
                .routeName("North Slopes")
                .mountainName("Grays Peak")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .grade(1)
                .gradeQuality("")
                .startElevation(11280)
                .summitElevation(14270)
                .totalGain(3000)
                .routeLength(7.5)
                .exposure("low")
                .rockfallPotential("low")
                .routeFinding("low")
                .commitment("low")
                .hasMultipleRoutes(false)
                .routeUrl("https://www.14ers.com/route.php?route=gray1")
                .trailhead("Argentine Pass Trailhead")
                .trailheadCoordinates("39.60866, -105.79999")
                .roadDifficulty(3)
                .roadDescription("Easy 4WD (good-clearance recommended) up to the gate closure near Shoe Basin mine.")
                .winterAccess("The Peru Creek (260) road is closed at the start.")
                .trailheadUrl("https://www.14ers.com/php14ers/trailheadsview.php?thparm=fr07")
                .trailheadUpdateDate("2021/01/01")
                .routeUpdateDate("2021/01/01")
                .build();

        elbAndElbSouth = ImmutableStoredRouteAndTrailhead.builder()
                .routeId(3)
                .routeName("East Ridge")
                .mountainName("Mt. Elbert")
                .isSnowRoute(false)
                .isStandardRoute(true)
                .grade(1)
                .gradeQuality("")
                .startElevation(10040)
                .summitElevation(14433)
                .totalGain(4700)
                .routeLength(9.5)
                .exposure("low")
                .rockfallPotential("low")
                .routeFinding("low")
                .commitment("low")
                .hasMultipleRoutes(false)
                .routeUrl("https://www.14ers.com/route.php?route=elbe2")
                .trailhead("Mt. Elbert South Trailhead")
                .routeUpdateDate(LocalDate.now().toString())
                .build();
    }

    @Test
    public void itRetrievesRouteAndTrailheadBySearchQuery() {
        FourteenerRoutesDao routesDao = new FourteenerRoutesDao(conn);
        TrailheadsDao trailheadsDao = new TrailheadsDao(conn);
        RoutesTrailheadsDao routesTrailheadsDao = new RoutesTrailheadsDao(conn);

        routesDao.insert(elbFetched1);
        routesDao.insert(graysFetched);
        routesDao.insert(elbFetched2);
        trailheadsDao.insert(elbNorthFetched);
        trailheadsDao.insert(argentineFetched);
        ImmutableStoredRouteAndTrailhead graysAndArgentineUpdated = graysAndArgentine.withRouteUpdateDate(LocalDate.now().toString()).withTrailheadUpdateDate(LocalDate.now().toString());

        assertThat(routesTrailheadsDao.get(sq0)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, graysAndArgentineUpdated, elbAndElbSouth)));
        assertThat(routesTrailheadsDao.get(sq1)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, elbAndElbSouth)));
        assertThat(routesTrailheadsDao.get(sq2)).usingRecursiveComparison().isEqualTo(new ArrayList<>());
        assertThat(routesTrailheadsDao.get(sq3)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, graysAndArgentineUpdated, elbAndElbSouth)));
        assertThat(routesTrailheadsDao.get(sq4)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, elbAndElbSouth)));
        assertThat(routesTrailheadsDao.get(sq5)).usingRecursiveComparison().isEqualTo(new ArrayList<>(List.of(elbAndElbNorth)));
        assertThat(routesTrailheadsDao.get(sq6)).usingRecursiveComparison().isEqualTo(new ArrayList<>(List.of(graysAndArgentineUpdated)));
        assertThat(routesTrailheadsDao.get(sq7)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, graysAndArgentineUpdated, elbAndElbSouth)));
        assertThat(routesTrailheadsDao.get(sq8)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, graysAndArgentineUpdated)));
    }

    @Test
    public void itRetrievesRouteAndTrailheadByCompareQuery() {
        FourteenerRoutesDao routesDao = new FourteenerRoutesDao(conn);
        TrailheadsDao trailheadsDao = new TrailheadsDao(conn);
        RoutesTrailheadsDao routesTrailheadsDao = new RoutesTrailheadsDao(conn);

        routesDao.insert(elbFetched1);
        routesDao.insert(graysFetched);
        routesDao.insert(elbFetched2);
        trailheadsDao.insert(elbNorthFetched);
        trailheadsDao.insert(argentineFetched);
        ImmutableStoredRouteAndTrailhead graysAndArgentineUpdated = graysAndArgentine.withRouteUpdateDate(LocalDate.now().toString()).withTrailheadUpdateDate(LocalDate.now().toString());

        assertThatThrownBy(() -> routesTrailheadsDao.get(cq0)).hasMessage("Incomplete query. Please enter both route names and mountain names, or route urls only.");
        assertThatThrownBy(() -> routesTrailheadsDao.get(cq1)).hasMessage("Incomplete query. Please enter both route names and mountain names, or route urls only.");
        assertThat(routesTrailheadsDao.get(cq2)).usingRecursiveComparison().isEqualTo(new ArrayList<>());
        assertThat(routesTrailheadsDao.get(cq3)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, graysAndArgentineUpdated)));
        assertThat(routesTrailheadsDao.get(cq4)).usingRecursiveComparison().isEqualTo(new ArrayList<>(Arrays.asList(elbAndElbNorth, graysAndArgentineUpdated)));
        assertThat(routesTrailheadsDao.get(cq5)).usingRecursiveComparison().isEqualTo(new ArrayList<>(List.of(elbAndElbNorth, graysAndArgentineUpdated)));
    }

    @Test
    public void itInsertsAndRetrievesRouteFromDatabase() {
        FourteenerRoutesDao dao = new FourteenerRoutesDao(conn);
        dao.insert(elbFetched1);
        assertThat(dao.get(elbFetched1)).usingRecursiveComparison().isEqualTo(elbStored1);
        assertThatThrownBy(() -> dao.get(graysFetched)).hasMessage("Route not in database.");

        dao.insert(graysFetched);
        assertThat(dao.get(graysFetched))
                .usingRecursiveComparison()
                .ignoringFields("updateDate")
                .isEqualTo(graysStored); //update
    }

    @Test
    public void itChecksIfRouteUpdateIsRequired() {
        FourteenerRoutesDao dao = new FourteenerRoutesDao(conn);
        dao.insert(elbFetched1);
        dao.insert(graysFetched);
        ImmutableStoredRoute storedElbRoute = dao.get(elbFetched1);
        ImmutableStoredRoute storedGraysRoute = dao.get(graysFetched);
        assertThat(FourteenerRoutesDao.hasUpdateDateOverWeekAgo(storedElbRoute.getUpdateDate())).isFalse();
        assertThat(FourteenerRoutesDao.hasUpdateDateOverWeekAgo(storedGraysRoute.getUpdateDate())).isFalse();
        assertThat(FourteenerRoutesDao.hasUpdateDateOverWeekAgo(graysStored.getUpdateDate())).isTrue();
    }

    @Test
    public void itInsertsAndRetrievesTrailheadFromDatabase() {
        TrailheadsDao dao = new TrailheadsDao(conn);
        dao.insert(elbNorthFetched);
        assertThat(dao.get(elbNorthFetched)).usingRecursiveComparison().isEqualTo(elbNorthStored);
        assertThatThrownBy(() -> dao.get(argentineFetched)).hasMessage("Trailhead not in database.");

        dao.insert(argentineFetched);
        assertThat(dao.get(argentineFetched))
                .usingRecursiveComparison()
                .ignoringFields("updateDate")
                .isEqualTo(argentineStored);
    }

    @Test
    public void itChecksIfTrailheadUpdateIsRequired() {
        TrailheadsDao dao = new TrailheadsDao(conn);
        dao.insert(elbNorthFetched);
        dao.insert(argentineFetched);
        ImmutableStoredTrailhead storedNorthElbTrailhead = dao.get(elbNorthFetched);
        ImmutableStoredTrailhead storedArgentineTrailhead = dao.get(argentineFetched);
        assertThat(FourteenerRoutesDao.hasUpdateDateOverWeekAgo(storedNorthElbTrailhead.getUpdateDate())).isFalse();
        assertThat(FourteenerRoutesDao.hasUpdateDateOverWeekAgo(storedArgentineTrailhead.getUpdateDate())).isFalse();
        assertThat(FourteenerRoutesDao.hasUpdateDateOverWeekAgo(argentineStored.getUpdateDate())).isTrue();
    }
}
