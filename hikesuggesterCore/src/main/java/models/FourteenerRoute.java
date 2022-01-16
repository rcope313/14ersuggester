package models;

public class FourteenerRoute {
    private int fourteenerRouteId;
    private String mountainName;
    private String routeName;
    private boolean isSnowRoute;
    private boolean isStandardRoute;
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
    private boolean hasMultipleRoutes = false;
    private String url;

    public FourteenerRoute() {}

    public int getFourteenerRouteId() {
        return fourteenerRouteId;
    }

    public void setFourteenerRouteId(int fourteenerRouteId) {
        this.fourteenerRouteId = fourteenerRouteId;
    }

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

    public boolean isSnowRoute() {
        return isSnowRoute;
    }

    public void setSnowRoute(boolean snowRoute) {
        this.isSnowRoute = snowRoute;
    }

    public boolean isStandardRoute() {
        return isStandardRoute;
    }

    public void setStandardRoute(boolean standardRoute) {
        this.isStandardRoute = standardRoute;
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

    public boolean hasMultipleRoutes() {
        return hasMultipleRoutes;
    }

    public void setHasMultipleRoutes(boolean hasMultipleRoutes) {
        this.hasMultipleRoutes = hasMultipleRoutes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
