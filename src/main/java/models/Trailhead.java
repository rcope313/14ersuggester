package models;

public class Trailhead {
    private int primaryKey;
    private String name;
    private String coordinates;
    private int roadDifficulty;
    private String roadDescription;
    private String winterAccess;
    private String url;

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getRoadDifficulty() {
        return roadDifficulty;
    }

    public void setRoadDifficulty(int roadDifficulty) {
        this.roadDifficulty = roadDifficulty;
    }

    public String getRoadDescription() {
        return roadDescription;
    }

    public void setRoadDescription(String roadDescription) {
        this.roadDescription = roadDescription;
    }

    public String getWinterAccess() {
        return winterAccess;
    }

    public void setWinterAccess(String winterAccess) {
        this.winterAccess = winterAccess;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
