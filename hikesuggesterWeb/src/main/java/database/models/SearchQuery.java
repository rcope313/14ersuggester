package database.models;

import java.util.ArrayList;

public class SearchQuery {
    private ArrayList<String> mountainNames;
    private ArrayList<String> routeNames;
    private boolean isStandardRoute;
    private boolean isSnowRoute;
    private ArrayList<Integer> grades;
    private ArrayList<String> gradeQualities;
    private ArrayList<String> trailheads;
    private int startElevation;
    private int summitElevation;
    private int totalGain;
    private double routeLength;
    private String exposure;
    private String rockfallPotential;
    private String routeFinding;
    private String commitment;
    private boolean hasMultipleRoutes;
    private ArrayList<String> routeUrls;
    private ArrayList<String> trrailheadCoordinates;
    private ArrayList<Integer> roadDifficulties;
    private ArrayList<String> trailheadUrls;

    public ArrayList<String> getMountainNames() {
        return mountainNames;
    }

    public void setMountainNames(ArrayList<String> mountainNames) {
        this.mountainNames = mountainNames;
    }

    public ArrayList<String> getRouteNames() {
        return routeNames;
    }

    public void setRouteNames(ArrayList<String> routeNames) {
        this.routeNames = routeNames;
    }

    public boolean isStandardRoute() {
        return isStandardRoute;
    }

    public void setStandardRoute(boolean standardRoute) {
        isStandardRoute = standardRoute;
    }

    public boolean isSnowRoute() {
        return isSnowRoute;
    }

    public void setSnowRoute(boolean snowRoute) {
        isSnowRoute = snowRoute;
    }

    public ArrayList<Integer> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<Integer> grades) {
        this.grades = grades;
    }

    public ArrayList<String> getGradeQualities() {
        return gradeQualities;
    }

    public void setGradeQualities(ArrayList<String> gradeQualities) {
        this.gradeQualities = gradeQualities;
    }

    public ArrayList<String> getTrailheads() {
        return trailheads;
    }

    public void setTrailheads(ArrayList<String> trailheads) {
        this.trailheads = trailheads;
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

    public boolean isHasMultipleRoutes() {
        return hasMultipleRoutes;
    }

    public void setHasMultipleRoutes(boolean hasMultipleRoutes) {
        this.hasMultipleRoutes = hasMultipleRoutes;
    }

    public ArrayList<String> getRouteUrls() {
        return routeUrls;
    }

    public void setRouteUrls(ArrayList<String> routeUrls) {
        this.routeUrls = routeUrls;
    }

    public ArrayList<String> getTrrailheadCoordinates() {
        return trrailheadCoordinates;
    }

    public void setTrrailheadCoordinates(ArrayList<String> trrailheadCoordinates) {
        this.trrailheadCoordinates = trrailheadCoordinates;
    }

    public ArrayList<Integer> getRoadDifficulties() {
        return roadDifficulties;
    }

    public void setRoadDifficulties(ArrayList<Integer> roadDifficulties) {
        this.roadDifficulties = roadDifficulties;
    }

    public ArrayList<String> getTrailheadUrls() {
        return trailheadUrls;
    }

    public void setTrailheadUrls(ArrayList<String> trailheadUrls) {
        this.trailheadUrls = trailheadUrls;
    }
}
