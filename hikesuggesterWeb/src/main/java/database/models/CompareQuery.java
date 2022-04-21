package database.models;

import java.util.ArrayList;

public class CompareQuery {
    private String mountainName1;
    private String routeName1;
    private String mountainName2;
    private String routeName2;
    private ArrayList<String> routeUrls;

    public String getMountainName1() {
        return mountainName1;
    }

    public void setMountainName1(String mountainName1) {
        this.mountainName1 = mountainName1;
    }

    public String getRouteName1() {
        return routeName1;
    }

    public void setRouteName1(String routeName1) {
        this.routeName1 = routeName1;
    }

    public String getMountainName2() {
        return mountainName2;
    }

    public void setMountainName2(String mountainName2) {
        this.mountainName2 = mountainName2;
    }

    public String getRouteName2() {
        return routeName2;
    }

    public void setRouteName2(String routeName2) {
        this.routeName2 = routeName2;
    }

    public ArrayList<String> getRouteUrls() {
        return routeUrls;
    }

    public void setRouteUrls(ArrayList<String> routeUrls) {
        this.routeUrls = routeUrls;
    }
}
