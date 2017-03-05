package edu.sjsu.sjsumap.service;

/**
 * Created by Akshatha Anantharamu on 11/1/16.
 */

public class LocationService {
    private static LocationService INSTANCE = null;

    private LatLong currentLocation = null;

    public static LocationService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocationService();
        }
        return INSTANCE;
    }

    private LocationService() {
    }


    public LatLong getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLong currentLocation) {
        this.currentLocation = currentLocation;
    }
}