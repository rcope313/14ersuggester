package models;

import java.util.Map;

public class FourteenerRoute {
    private String mountainName;
    private String routeName;
    private boolean snowRouteOnly;
    private Map<Integer, String> grade;
    private String exposure;
    private String rockfallPotential;
    private String routeFinding;
    private String commitment;
    private String trailhead;
    private int startElevation;
    private int summitElevation;
    private Map<Integer, String> totalGain;
    private Map<Double, String> routeLength;

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

    public Map<Integer, String> getGrade() {
        return grade;
    }

    public boolean isSnowRouteOnly() {
        return snowRouteOnly;
    }

    public void setSnowRouteOnly(boolean snowRouteOnly) {
        this.snowRouteOnly = snowRouteOnly;
    }

    public void setGrade(Map<Integer, String> grade) {
        this.grade = grade;
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

    public Map<Integer, String> getTotalGain() {
        return totalGain;
    }

    public void setTotalGain(Map<Integer, String> totalGain) {
        this.totalGain = totalGain;
    }

    public Map<Double, String> getRouteLength() {
        return routeLength;
    }

    public void setRouteLength(Map<Double, String> routeLength) {
        this.routeLength = routeLength;
    }
}
