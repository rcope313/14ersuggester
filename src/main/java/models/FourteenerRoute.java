package models;

import java.util.Map;

public class FourteenerRoute {
    private String mountainName;
    private String routeName;
    private boolean snowRouteOnly;
    private boolean standardRoute;
    private GradeQuality gradeQuality;
    private String trailhead;
    private int startElevation;
    private int summitElevation;
    private int totalGain;
    private double routeLength;
    private String exposure;
    private String rockfallPotential;
    private String routeFinding;
    private String commitment;
    private boolean multipleRoutes = false;
    private String url;


    public FourteenerRoute() {}

    public String getMountainName() {
        return mountainName;
    }

    public void setMountainName(String mountainName) {
        this.mountainName = mountainName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public boolean isSnowRouteOnly() {
        return snowRouteOnly;
    }

    public void setSnowRouteOnly(boolean snowRouteOnly) {
        this.snowRouteOnly = snowRouteOnly;
    }

    public boolean isStandardRoute() {
        return standardRoute;
    }

    public void setStandardRoute(boolean standardRoute) {
        this.standardRoute = standardRoute;
    }

    public GradeQuality getGradeQuality() {
        return gradeQuality;
    }

    public void setGradeQuality(GradeQuality gradeQuality) {
        this.gradeQuality = gradeQuality;
    }

    public String getTrailhead() {
        return trailhead;
    }

    public void setTrailhead(String trailhead) {
        this.trailhead = trailhead;
    }

    public int getStartElevation() {
        return startElevation;
    }

    public void setStartElevation(int startElevation) {
        this.startElevation = startElevation;
    }

    public int getSummitElevation() {
        return summitElevation;
    }

    public void setSummitElevation(int summitElevation) {
        this.summitElevation = summitElevation;
    }

    public int getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(int totalGain) {
        this.totalGain = totalGain;
    }

    public double getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(double routeLength) {
        this.routeLength = routeLength;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public String getRockfallPotential() {
        return rockfallPotential;
    }

    public void setRockfallPotential(String rockfallPotential) {
        this.rockfallPotential = rockfallPotential;
    }

    public String getRouteFinding() {
        return routeFinding;
    }

    public void setRouteFinding(String routeFinding) {
        this.routeFinding = routeFinding;
    }

    public String getCommitment() {
        return commitment;
    }

    public void setCommitment(String commitment) {
        this.commitment = commitment;
    }

    public boolean isMultipleRoutes() {
        return multipleRoutes;
    }

    public void setMultipleRoutes(boolean multipleRoutes) {
        this.multipleRoutes = multipleRoutes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
