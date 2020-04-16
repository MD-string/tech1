package cn.hand.tech.utils;

import java.io.Serializable;

public class LatLng implements Serializable {
    double  lat;
    double lon;
    public LatLng() {
       super();
    }
    public LatLng(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
