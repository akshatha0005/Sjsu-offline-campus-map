package edu.sjsu.sjsumap.service;

/**
 * Created by Akshatha Anantharamu on 11/1/16.
 */
public class LatLong {
    double latitude;
    double longitude;

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Lat:" + latitude + "\tlng:"+longitude;
    }
}

