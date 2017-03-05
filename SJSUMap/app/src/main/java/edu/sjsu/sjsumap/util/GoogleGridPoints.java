package edu.sjsu.sjsumap.util;

/**
 * Created by Akshatha Anantharamu on 10/30/16.
 */

public class GoogleGridPoints {
    Double latitude;
    Double longitude;

    public GoogleGridPoints(Double lat,Double lng){
        this.latitude = lat;
        this.longitude = lng;
    }

    public Double getLatitude() {
        return latitude;
    }


    public Double getLongitude() {
        return longitude;
    }
}
