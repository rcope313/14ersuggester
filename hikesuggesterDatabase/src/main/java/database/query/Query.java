package database.query;
import java.util.ArrayList;

  public interface Query {

    String createQuerySyntax();
    
    boolean isVerbose();
    
    void setVerbose(boolean verbose);
    
    String getQuery();
    
    void setQuery(String query);

    ArrayList<String> getMountainNames();
    
    void setMountainNames(ArrayList<String> mountainNames);
    
    ArrayList<String> getRouteNames();
    
    void setRouteNames(ArrayList<String> routeNames);

    boolean isStandardRoute();

    void setStandardRoute(boolean standardRoute);

    boolean isSnowRoute();

    void setSnowRoute(boolean snowRoute);

    ArrayList<Integer> getGrades();

    void setGrades(ArrayList<Integer> grades);

    ArrayList<String> getGradeQualities();

    void setGradeQualities(ArrayList<String> gradeQualities);

    ArrayList<String> getTrailheads();

    void setTrailheads(ArrayList<String> trailheads);

    int getStartElevation();

    void setStartElevation(int startElevation);

    int getSummitElevation();

    void setSummitElevation(int summitElevation);

    int getTotalGain();

    void setTotalGain(int totalGain);

    double getRouteLength();

    void setRouteLength(double routeLength);

    String getExposure();

    void setExposure(String exposure);

    String getRockfallPotential();

    void setRockfallPotential(String rockfallPotential);

    String getRouteFinding();

    void setRouteFinding(String routeFinding);

    String getCommitment();

    void setCommitment(String commitment);

    boolean isHasMultipleRoutes();

    void setHasMultipleRoutes(boolean hasMultipleRoutes);

    ArrayList<String> getRouteUrls();

    void setRouteUrls(ArrayList<String> routeUrls);

    ArrayList<String> getTrailheadCoordinates();

    void setTrailheadCoordinates(ArrayList<String> trailheadCoordinates);

    ArrayList<Integer> getRoadDifficulties();

    void setRoadDifficulties(ArrayList<Integer> roadDifficulties);

    ArrayList<String> getTrailheadUrls();

    void setTrailheadUrls(ArrayList<String> trailheadUrls);

    String getMountainName1();

    void setMountainName1(String mountainName1);

    String getRouteName1();

    void setRouteName1(String routeName1);

    String getMountainName2();

    void setMountainName2(String mountainName2);

    String getRouteName2();

    void setRouteName2(String routeName2);









}
