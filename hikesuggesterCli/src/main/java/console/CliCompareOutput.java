package console;

import database.dao.RoutesTrailheadsDao;
import database.models.CompareQuery;
import database.models.ImmutableStoredRouteAndTrailhead;
import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.StringJoiner;

public class CliCompareOutput extends CliOutput{
    private final RoutesTrailheadsDao dao;
    final private static Logger LOG = LoggerFactory.getLogger(CliCompareOutput.class);

    public CliCompareOutput(RoutesTrailheadsDao dao) {
        this.dao = dao;
    }

    public void buildCliTable(CompareQuery query) {
        ArrayList<ImmutableStoredRouteAndTrailhead> routes = dao.get(query);
        routes.forEach((route) -> {
            try {
                dao.update(route);
            } catch (Exception e) {
                LOG.warn("Unable to update {}" + route.getRouteUrl() + " by exception {}", e.getMessage());
            }
        });
        if (routes.size() != 2) {
            throw new IllegalStateException("Query did not yield two results. Please use route urls as parameters.");
        }
        buildCliTableHeaders(designateColumnFields());
        inputImmutableStoredRouteAndTrailheadsIntoCliTable(routes, designateColumnFields());
        System.out.print("\n" + createDifferenceString(routes.get(0), routes.get(1)));
    }

    @VisibleForTesting
    static String createDifferenceString(ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        StringJoiner differencesStringJoiner = new StringJoiner("\n");

        snowRouteDiff(differencesStringJoiner, route0, route1);
        standardRouteDiff(differencesStringJoiner, route0, route1);
        gradeDiff(differencesStringJoiner, route0, route1);
        gradeQualityDiff(differencesStringJoiner, route0, route1);
        startElevationDiff(differencesStringJoiner, route0, route1);
        summitElevationDiff(differencesStringJoiner, route0, route1);
        totalGainDiff(differencesStringJoiner, route0, route1);
        routeLengthDiff(differencesStringJoiner, route0, route1);
        exposureDiff(differencesStringJoiner, route0, route1);
        rockfallPotentialDiff(differencesStringJoiner, route0, route1);
        routeFindingDiff(differencesStringJoiner, route0, route1);
        commitmentDiff(differencesStringJoiner, route0, route1);
        hasMultipleRoutesDiff(differencesStringJoiner, route0, route1);
        trailheadDiff(differencesStringJoiner, route0, route1);
        roadDifficultyDiff(differencesStringJoiner, route0, route1);
        roadDescriptionDiff(differencesStringJoiner, route0, route1);
        winterAccessDiff(differencesStringJoiner, route0, route1);

        return differencesStringJoiner.toString();
    }

    private static void snowRouteDiff (StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if  (route0.getIsSnowRoute() != route1.getIsSnowRoute()) {
            differencesStringJoiner.add("Snow Route:\n" + route0.getIsSnowRoute() + ", " + route1.getIsSnowRoute() + "\n");
        }
    }

    private static void standardRouteDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getIsStandardRoute() != route1.getIsStandardRoute()) {
            differencesStringJoiner.add("Standard Route:\n" + route0.getIsStandardRoute() + ", " + route1.getIsStandardRoute());
        }
    }

    private static void gradeDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getGrade() != route1.getGrade()) {
            differencesStringJoiner.add("Grade:\n" + route0.getGrade() + ", " + route1.getGrade());
        }
    }

    private static void gradeQualityDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getGradeQuality().equals(route1.getGradeQuality())) {
            differencesStringJoiner.add("Grade Quality:\n" + route0.getGradeQuality() + ", " + route1.getGradeQuality());
        }
    }

    private static void startElevationDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getStartElevation() != route1.getStartElevation()) {
            differencesStringJoiner.add("Start Elevation:\n" + route0.getStartElevation() + ", " + route1.getStartElevation());
        }
    }

    private static void summitElevationDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getSummitElevation() != route1.getSummitElevation()) {
            differencesStringJoiner.add("Summit Elevation:\n" + route0.getSummitElevation() + ", " + route1.getSummitElevation());
        }
    }

    private static void totalGainDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getTotalGain() != route1.getTotalGain()) {
            differencesStringJoiner.add("Total Gain:\n" + route0.getTotalGain() + ", " + route1.getTotalGain());
        }
    }

    static void routeLengthDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getRouteLength() != route1.getRouteLength()) {
            differencesStringJoiner.add("Route Length:\n" + route0.getRouteLength() + ", " + route1.getRouteLength());
        }
    }

    private static void exposureDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getExposure().equals(route1.getExposure())) {
            differencesStringJoiner.add("Exposure:\n" + route0.getExposure() + ", " + route1.getExposure());
        }
    }

    private static void rockfallPotentialDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getRockfallPotential().equals(route1.getRockfallPotential())) {
            differencesStringJoiner.add("Rockfall Potential:\n" + route0.getRockfallPotential() + ", " + route1.getRockfallPotential());
        }
    }

    private static void routeFindingDiff (StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getRouteFinding().equals(route1.getRouteFinding())) {
            differencesStringJoiner.add("Route Finding:\n" + route0.getRouteFinding() + ", " + route1.getRouteFinding());
        }
    }

    private static void commitmentDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getCommitment().equals(route1.getCommitment())) {
            differencesStringJoiner.add("Commitment:\n" + route0.getCommitment() + ", " + route1.getCommitment());
        }
    }

    private static void hasMultipleRoutesDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getHasMultipleRoutes() != route1.getHasMultipleRoutes()) {
            differencesStringJoiner.add("Multiple Routes:\n" + route0.getHasMultipleRoutes() + ", " + route1.getHasMultipleRoutes());
        }
    }

    private static void trailheadDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getTrailhead().equals(route1.getTrailhead())) {
            differencesStringJoiner.add("Trailhead:\n" + route0.getTrailhead()+ ", \n" + route1.getTrailhead());
        }
    }

    private static void roadDifficultyDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (!route0.getTrailhead().equals(route1.getTrailhead()) && route0.getRoadDifficulty().isPresent() && route1.getRoadDifficulty().isPresent()) {
            differencesStringJoiner.add("Road Difficulty:\n" + route0.getRoadDifficulty().get() + ", " + route1.getRoadDifficulty().get());
        }
    }

    private static void roadDescriptionDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getRoadDescription().isPresent() && route1.getRoadDescription().isPresent()) {
            differencesStringJoiner.add("Road Description:\n" + route0.getRoadDescription().get() + ", \n" + route1.getRoadDescription().get());
        }
    }

    private static void winterAccessDiff(StringJoiner differencesStringJoiner, ImmutableStoredRouteAndTrailhead route0, ImmutableStoredRouteAndTrailhead route1) {
        if (route0.getWinterAccess().isPresent() && route1.getWinterAccess().isPresent()) {
            differencesStringJoiner.add("Winter Access:\n" + route0.getWinterAccess().get() + ", \n" + route1.getWinterAccess().get());
        }
    }

}
