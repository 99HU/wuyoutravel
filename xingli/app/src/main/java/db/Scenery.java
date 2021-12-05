package db;

public class Scenery {
    String SceneryName;
    String SceneryLevel;
    String SceneryDistrict;

    public Scenery(String sceneryName, String sceneryLevel, String sceneryDistrict) {
        SceneryName = sceneryName;
        SceneryLevel = sceneryLevel;
        SceneryDistrict = sceneryDistrict;
    }

    public String getSceneryName() {
        return SceneryName;
    }

    public void setSceneryName(String sceneryName) {
        SceneryName = sceneryName;
    }

    public String getSceneryLevel() {
        return SceneryLevel;
    }

    public void setSceneryLevel(String sceneryLevel) {
        SceneryLevel = sceneryLevel;
    }

    public String getSceneryDistrict() {
        return SceneryDistrict;
    }

    public void setSceneryDistrict(String sceneryDistrict) {
        SceneryDistrict = sceneryDistrict;
    }
}
