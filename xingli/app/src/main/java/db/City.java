package db;

import java.util.List;

public class City {
    String cityName;
    List <Scenery>scenery;

    public City(String cityName, List<Scenery> scenery) {
        this.cityName = cityName;
        this.scenery = scenery;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<Scenery> getScenery() {
        return scenery;
    }

    public void setScenery(List<Scenery> scenery) {
        this.scenery = scenery;
    }
}
